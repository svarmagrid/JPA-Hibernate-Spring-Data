package org.example.gettingalongwithjpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.gettingalongwithjpa.entity.Department;
import org.example.gettingalongwithjpa.repository.DepartmentRepository;
import org.example.gettingalongwithjpa.repository.EmployeeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class SaveParentWithExistingIdTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @AfterEach
    void cleanup(){
        departmentRepository.deleteAll();
        employeeRepository.deleteAll();
    }

    @Test
    @DisplayName("7. save() with existing ID")
    void saveExistingId_repositorySave(){
        Department existing = departmentRepository.save(new Department("Original"));

        Department another = new Department();
        another.setId(existing.getId());
        another.setName("Updated");

        Department updated = departmentRepository.save(another);

        assertEquals(existing.getId(), updated.getId());

        Department department = departmentRepository.findById(existing.getId()).orElseThrow();

        assertEquals("Updated",department.getName());
    }

    @Test
    @DisplayName("8. persist() with existing ID")
    void saveExistingId_persist(){
        Department existing = departmentRepository.save(new Department("IT"));

        Department another = new Department();
        another.setId(existing.getId());
        another.setName("Duplicate");

//        entityManager.persist(another);
//        entityManager.flush();

        assertThrows(Exception.class,()->{
            entityManager.persist(another);
            entityManager.flush();
        });
    }

    @Test
    @DisplayName("9. merge() with existing ID")
    void saveExistingId_merge(){
        Department existing = departmentRepository.save(new Department("Initial"));
        Department detached = new Department();
        detached.setId(existing.getId());
        detached.setName("Merged");

        Department merged = entityManager.merge(detached);

        entityManager.flush();
        assertEquals(existing.getId(), merged.getId());

        Department department = departmentRepository.findById(existing.getId()).orElseThrow();

        assertNotSame(detached, merged);
        assertEquals("Merged", department.getName());
    }
}
