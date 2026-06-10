package org.example.crudoperationsjpa.service;

import org.example.crudoperationsjpa.entity.Student;
import org.example.crudoperationsjpa.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    public void saveStudent(Student student){
        studentRepository.save(student);
    }

    public List<Student> findAll(){
        return studentRepository.findAll();
    }

    public Student findById(Long id){
        return studentRepository.findById(id).orElseThrow();
    }

    public Student update(Long id,Student student){
        Student student1 = studentRepository.findById(id).orElseThrow(()-> new RuntimeException("Student not found"));
        student.setName(student1.getName());
        student.setAge(student1.getAge());
        return studentRepository.save(student);
    }

    public void deleteById(Long id){
        studentRepository.deleteById(id);
    }
}
