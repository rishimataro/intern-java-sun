package com.example.employee_management.component;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.example.employee_management.security.AuthEndpointMatcher;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 20)
public class RequestRateLimitFilter extends OncePerRequestFilter {

    private static final int REQUESTS_PER_MINUTE = 120;
    private static final int AUTH_REQUESTS_PER_MINUTE = 10;
    private static final Cache<String, AtomicInteger> REQUEST_COUNTS = Caffeine.newBuilder()
            .maximumSize(10_000)
            .expireAfterWrite(Duration.ofMinutes(1))
            .build();

    private final AuthEndpointMatcher authEndpointMatcher;

    public RequestRateLimitFilter(AuthEndpointMatcher authEndpointMatcher) {
        this.authEndpointMatcher = authEndpointMatcher;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String clientAddress = request.getRemoteAddr();
        String authorization = request.getHeader("Authorization");
        boolean basicAuthentication = authorization != null && StringUtils.startsWithIgnoreCase(
                authorization.trim(), BasicAuthenticationConverter.AUTHENTICATION_SCHEME_BASIC);
        boolean authRequest = authEndpointMatcher.matches(request) || basicAuthentication;
        int requestCount = count("all:" + clientAddress);
        int authRequestCount = authRequest ? count("auth:" + clientAddress) : 0;
        if (requestCount > REQUESTS_PER_MINUTE || authRequestCount > AUTH_REQUESTS_PER_MINUTE) {
            response.setStatus(429);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"code\":\"RATE_LIMITED\",\"message\":\"Too many requests\"}");
            return;
        }
        filterChain.doFilter(request, response);
    }

    private int count(String key) {
        return REQUEST_COUNTS.get(key, ignored -> new AtomicInteger()).incrementAndGet();
    }
}
