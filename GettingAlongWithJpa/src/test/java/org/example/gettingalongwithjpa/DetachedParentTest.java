package org.example.gettingalongwithjpa;

import jakarta.persistence.EntityManager;
import org.example.gettingalongwithjpa.entity.Department;
import org.example.gettingalongwithjpa.entity.Employee;
import org.example.gettingalongwithjpa.repository.DepartmentRepository;
import org.example.gettingalongwithjpa.repository.EmployeeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class DetachedParentTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    @DisplayName("13. Child with detached parent")
    void ildWithDetachedParent(){
        Department department = departmentRepository.save(new Department("Detached"));

        entityManager.flush();
        entityManager.clear();

        Department detached = departmentRepository.findById(department.getId()).orElseThrow();
        entityManager.detach(detached);

        Employee employee = new Employee("DetachedEmployee");

        employee.setDepartment(detached);

        Employee saved = employeeRepository.save(employee);

        entityManager.flush();

        assertNotNull(saved.getId());
    }
}
