package com.example.employee_management.security;

import org.springframework.http.server.PathContainer;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class AuthEndpointMatcher implements RequestMatcher {

    private static final PathPattern AUTH_PATH = PathPatternParser.defaultInstance.parse("/api/auth/{action}");

    @Override
    public boolean matches(HttpServletRequest request) {
        if (!"POST".equals(request.getMethod())) {
            return false;
        }

        String contextPath = request.getContextPath();
        String requestPath = request.getRequestURI().substring(contextPath.length());
        PathPattern.PathMatchInfo match = AUTH_PATH.matchAndExtract(PathContainer.parsePath(requestPath));
        if (match == null) {
            return false;
        }
        String action = match.getUriVariables().get("action");
        return "register".equals(action) || "login".equals(action);
    }
}
