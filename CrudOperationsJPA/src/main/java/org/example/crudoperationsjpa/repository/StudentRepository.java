package org.example.crudoperationsjpa.repository;

import org.example.crudoperationsjpa.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
