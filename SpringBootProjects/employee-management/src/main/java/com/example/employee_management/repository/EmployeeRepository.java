package com.example.employee_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.employee_management.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @EntityGraph(attributePaths = "department")
    @Query("select e from Employee e")
    List<Employee> findAllWithDepartment();

    @EntityGraph(attributePaths = "department")
    List<Employee> findByNameContainingIgnoreCase(String name);

    @EntityGraph(attributePaths = "department")
    List<Employee> findByDepartment_Id(Long departmentId);

    @EntityGraph(attributePaths = "department")
    List<Employee> findByNameContainingIgnoreCaseAndDepartment_Id(String name, Long departmentId);

    @Query("select e from Employee e join fetch e.department where e.id = :id")
    Optional<Employee> findByIdWithDepartment(@Param("id") Long id);
}
