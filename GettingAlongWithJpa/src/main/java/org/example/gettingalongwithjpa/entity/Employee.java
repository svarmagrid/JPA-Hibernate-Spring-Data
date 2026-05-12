package org.example.gettingalongwithjpa.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    private Department department;

    public Employee() {
    }

    public Employee(String name) {
        this.name = name;
    }
}
