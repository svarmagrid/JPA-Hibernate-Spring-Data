package org.example.gettingalongwithjpa;

import jakarta.persistence.EntityManager;
import org.example.gettingalongwithjpa.entity.Department;
import org.example.gettingalongwithjpa.repository.DepartmentRepository;
import org.example.gettingalongwithjpa.repository.EmployeeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class SaveParentWithIdTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private EmployeeRepository employeeRepository;

    @AfterEach
    void cleanup(){
        employeeRepository.deleteAll();
        departmentRepository.deleteAll();
    }

    // Save Parent with initialized ID

    @Test
    @DisplayName("4. repository.save() with initialized ID")
    void saveWithId_repositorySave(){
        Department department = new Department();
        department.setId(100L);
        department.setName("Sales");

//        Department saved = departmentRepository.save(department);
//        assertNotNull(saved);


        assertThrows(Exception.class,()->{
            Department saved = departmentRepository.save(department);
            assertNotNull(saved);
        });
    }


    @Test
    @DisplayName("5. persist() with initialized ID")
    void saveWithId_persist(){
        Department department = new Department();
        department.setId(200L);
        department.setName("Marketing");

        assertThrows(Exception.class, () -> {
            entityManager.persist(department);
            entityManager.flush();
        });
    }

    @Test
    @DisplayName("6. merge() with initialized ID")
    void saveWithId_merge(){
        Department department = new Department();
        department.setId(300L);
        department.setName("Operations");

//        Department merged = entityManager.merge(department);
//        entityManager.flush();
        assertThrows(Exception.class,()->{
            Department merged = entityManager.merge(department);
            entityManager.flush();
            assertNotNull(merged);
        });

    }
}
