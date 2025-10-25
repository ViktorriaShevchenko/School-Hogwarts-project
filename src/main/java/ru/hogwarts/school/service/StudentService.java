package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    public Optional<Student> findStudent(long id) {
        return studentRepository.findById(id);
    }

    public Optional<Student> editStudent(Student student) {
        if (studentRepository.existsById(student.getId())) {
            return Optional.of(studentRepository.save(student));
        }
        return Optional.empty();
    }

    public Optional<Student> deleteStudent(long id) {
        Optional<Student> student = studentRepository.findById(id);
        student.ifPresent(s -> studentRepository.deleteById(id));
        return student;
    }

    public Collection<Student> findByAge(int age) {
        return studentRepository.findByAge(age);
    }
}
