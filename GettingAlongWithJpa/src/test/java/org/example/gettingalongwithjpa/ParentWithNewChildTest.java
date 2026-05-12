package org.example.gettingalongwithjpa;

import jakarta.persistence.EntityManager;
import org.example.gettingalongwithjpa.entity.Department;
import org.example.gettingalongwithjpa.entity.Employee;
import org.example.gettingalongwithjpa.repository.DepartmentRepository;
import org.example.gettingalongwithjpa.repository.EmployeeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ParentWithNewChildTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EntityManager entityManager;

    @AfterEach
    void cleanup(){
        departmentRepository.deleteAll();
        employeeRepository.deleteAll();
    }

    @Test
    @DisplayName("10. save parent with new children")
    void saveParentWithNewChildren(){
        Department department = new Department("Engineering");

        Employee e1 = new Employee("John");
        Employee e2 = new Employee("Jane");

        department.addEmployee(e1);
        department.addEmployee(e2);

        Department saved = departmentRepository.save(department);
//        entityManager.flush();

        assertNotNull(saved.getId());
        assertEquals(2, employeeRepository.count());

    }

    @Test
    @DisplayName("11. save child without parent")
    void saveChildWithoutParent(){
        Employee e = new Employee("Solo");

        Employee saved = employeeRepository.save(e);

//        entityManager.flush();
        assertNotNull(saved.getId());
    }

    @Test
    @DisplayName("12. child with non-existing parent")
    void childWithNonExistingParent(){
        Department fake = new Department();
        fake.setId(999L);

        Employee e = new Employee("Ghost");
        e.setDepartment(fake);
        assertThrows(Exception.class,()->{
            employeeRepository.saveAndFlush(e);
        });
    }
}
