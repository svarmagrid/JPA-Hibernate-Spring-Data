package org.example.gettingalongwithjpa.repository;

import org.example.gettingalongwithjpa.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
