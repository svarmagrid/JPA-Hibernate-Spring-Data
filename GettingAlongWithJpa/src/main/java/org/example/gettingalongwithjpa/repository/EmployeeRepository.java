package org.example.gettingalongwithjpa.repository;

import org.example.gettingalongwithjpa.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
