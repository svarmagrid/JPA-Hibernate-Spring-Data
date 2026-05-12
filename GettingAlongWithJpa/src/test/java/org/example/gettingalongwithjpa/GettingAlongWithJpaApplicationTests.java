package org.example.gettingalongwithjpa;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.example.gettingalongwithjpa.entity.Department;
import org.example.gettingalongwithjpa.entity.Employee;
import org.example.gettingalongwithjpa.repository.DepartmentRepository;
import org.example.gettingalongwithjpa.repository.EmployeeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GettingAlongWithJpaApplicationTests {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PlatformTransactionManager transactionManager;

    private TransactionTemplate transactionTemplate;

    @BeforeEach
    void setup() {
        transactionTemplate = new TransactionTemplate(transactionManager);
    }

    @AfterEach
    void cleanup(){
        employeeRepository.deleteAll();
        departmentRepository.deleteAll();
    }

   // 1. Save Parent without ID

    @Test
    @DisplayName("1. reposity.save() without ID")
    void saveWithoutId_repositorySave(){
        Department d = new Department();
        d.setName("IT");

        Department saved = departmentRepository.save(d);

        assertNotNull(saved.getId());
        assertEquals(1, departmentRepository.count());
    }

    @Test
    @Transactional
    @DisplayName("2. entityManager.persist() without ID")
    void savedWithoutId_persist(){
        Department d = new Department();
        d.setName("HR");

        entityManager.persist(d);
        entityManager.flush();

//        assertThrows(Exception.class,()->{
//            entityManager.persist(d);
//            entityManager.flush();
//        });

        assertNotNull(d.getId());
        assertEquals(1, departmentRepository.count());
    }

    @Test
    @DisplayName("2. Corrected entityManager.persist() without ID")
    void savedWithoutId_persist2() {

        transactionTemplate.executeWithoutResult(status -> {

            Department d = new Department();
            d.setName("HR");

            entityManager.persist(d);
            entityManager.flush();

            assertNotNull(d.getId());
        });

        assertEquals(1, departmentRepository.count());
    }

    @Test
    @Transactional
    @DisplayName("3. entityManager.merge() without ID")
    void saveWithoutId_merge(){
        Department d = new Department();
        d.setName("Finance");

        Department merged = entityManager.merge(d);
        entityManager.flush();

        assertNotNull(merged.getId());
        assertEquals(1, departmentRepository.count());
    }

    @Test
    @DisplayName("3. Corrected entityManager.merge() without ID")
    void saveWithoutId_merge2(){
        transactionTemplate.executeWithoutResult( Status ->{
            Department department = new Department();
            department.setName("Finance");

            Department merged = entityManager.merge(department);
            entityManager.flush();
            assertNotNull(merged.getId());
                }
        );
        assertEquals(1, departmentRepository.count());
    }

    // =========================================================
    // SAVE PARENT WITH INITIALIZED ID
    // =========================================================

    @Test
    @DisplayName("4. repository.save() with initialized ID")
    void saveWithId_repositorySave() {

        Department d = new Department();
        d.setId(100L);
        d.setName("Marketing");

//        Department saved = departmentRepository.save(d);
//
//        assertNotNull(saved);

        assertThrows(Exception.class,()->{
            Department saved = departmentRepository.save(d);
            assertNotNull(saved);
        });
    }

    @Test
    @DisplayName("5. persist() with initialized ID")
    void saveWithId_persist() {

        Department d = new Department();
        d.setId(200L);
        d.setName("Sales");

        assertThrows(Exception.class, () -> {
            entityManager.persist(d);
            entityManager.flush();
        });
    }

    @Test
    @DisplayName("6. merge() with initialized ID")
    void saveWithId_merge() {

        Department d = new Department();
        d.setId(300L);
        d.setName("Operations");

//        Department merged = entityManager.merge(d);
//
//        entityManager.flush();
//
//        assertNotNull(merged);

        assertThrows(Exception.class,()->{
            Department merged = entityManager.merge(d);
            entityManager.flush();
            assertNotNull(merged);
        });
    }

    // =========================================================
    // EXISTING ID
    // =========================================================

    @Test
    @DisplayName("7. save() with existing ID")
    void saveExistingId_repositorySave() {

        Department existing =
                departmentRepository.save(new Department("Original"));

        Department another = new Department();
        another.setId(existing.getId());
        another.setName("Updated");

        Department updated = departmentRepository.save(another);

        assertEquals(existing.getId(), updated.getId());

        Department db =
                departmentRepository.findById(existing.getId()).orElseThrow();

        assertEquals("Updated", db.getName());
    }

    @Test
    @DisplayName("8. persist() with existing ID")
    void saveExistingId_persist() {

        Department existing =
                departmentRepository.save(new Department("IT"));

        Department another = new Department();
        another.setId(existing.getId());
        another.setName("Duplicate");

        assertThrows(Exception.class, () -> {
            entityManager.persist(another);
            entityManager.flush();
        });
    }

    @Test
    @Transactional
    @DisplayName("9. merge() with existing ID")
    void saveExistingId_merge() {

        Department existing =
                departmentRepository.save(new Department("Initial"));

        Department detached = new Department();
        detached.setId(existing.getId());
        detached.setName("Merged");

        Department merged = entityManager.merge(detached);

        entityManager.flush();

        assertEquals(existing.getId(), merged.getId());

        Department db =
                departmentRepository.findById(existing.getId()).orElseThrow();

        assertEquals("Merged", db.getName());
    }

    // =========================================================
    // PARENT WITH NEW CHILDREN
    // =========================================================

    @Test
    @Transactional
    @DisplayName("10. save parent with new children")
    void saveParentWithNewChildren() {

        Department d = new Department("Engineering");

        Employee e1 = new Employee("John");
        Employee e2 = new Employee("Jane");

        d.addEmployee(e1);
        d.addEmployee(e2);

        Department saved = departmentRepository.save(d);

        entityManager.flush();

        assertNotNull(saved.getId());
        assertEquals(2, employeeRepository.count());
    }

    // =========================================================
    // CHILD WITHOUT PARENT
    // =========================================================

    @Test
    @Transactional
    @DisplayName("11. save child without parent")
    void saveChildWithoutParent() {

        Employee e = new Employee("Solo");

        Employee saved = employeeRepository.save(e);

        entityManager.flush();

        assertNotNull(saved.getId());
    }

    // =========================================================
    // CHILD WITH NON-EXISTING PARENT
    // =========================================================

    @Test
    @DisplayName("12. child with non-existing parent")
    void childWithNonExistingParent() {

        Department fake = new Department();
        fake.setId(999L);

        Employee e = new Employee("Ghost");
        e.setDepartment(fake);

        assertThrows(Exception.class, () -> {
            employeeRepository.saveAndFlush(e);
        });
    }

    // =========================================================
    // DETACHED PARENT
    // =========================================================

    @Test
    @Transactional
    @DisplayName("13. child with detached parent")
    void childWithDetachedParent() {

        Department department =
                departmentRepository.save(new Department("Detached"));

        entityManager.flush();
        entityManager.clear();

        Department detached =
                departmentRepository.findById(department.getId()).orElseThrow();

        entityManager.detach(detached);

        Employee employee = new Employee("DetachedEmployee");
        employee.setDepartment(detached);

        Employee saved = employeeRepository.save(employee);

        entityManager.flush();

        assertNotNull(saved.getId());
    }

    // =========================================================
    // DIRTY CHECKING
    // =========================================================

    @Test
    @Transactional
    @DisplayName("14. dirty checking")
    void dirtyChecking() {

        Department d = new Department("Before");
        Department saved = departmentRepository.save(d);

        entityManager.flush();
        entityManager.clear();

        Department found =
                departmentRepository.findById(saved.getId()).orElseThrow();

        found.setName("After");

        entityManager.flush();

        entityManager.clear();

        Department updated =
                departmentRepository.findById(saved.getId()).orElseThrow();

        assertEquals("After", updated.getName());
    }

    // =========================================================
    // MERGE RETURNS DIFFERENT INSTANCE
    // =========================================================

    @Test
    @Transactional
    @DisplayName("15. merge returns different managed instance")
    void mergeReturnsDifferentInstance() {

        Department d = new Department();
        d.setName("MergeTest");

        Department merged = entityManager.merge(d);

        assertNotSame(d, merged);
    }
}
