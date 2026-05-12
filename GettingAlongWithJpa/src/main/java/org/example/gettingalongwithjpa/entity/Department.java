package org.example.gettingalongwithjpa.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(
            mappedBy = "department",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )

    private List<Employee> employees = new ArrayList<>();

    public Department(){
    }

    public Department(String name){
        this.name = name;
    }

    public void addEmployee(Employee employee){
        employees.add(employee);
        employee.setDepartment(this);
    }

    public void removeEmployee(Employee employee){
        employees.remove(employee);
        employee.setDepartment(null);
    }





}
