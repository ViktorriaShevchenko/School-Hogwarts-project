package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.StudentHasNoFacultyException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;

@Service
public class StudentService {

    private static final String STUDENT_NOT_FOUND = "Student not found";

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student findStudent(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(STUDENT_NOT_FOUND));
    }

    public Student editStudent(Student student) {
        Long studentId = student.getId();
        if (!studentRepository.existsById(studentId)) {
            throw new RuntimeException(STUDENT_NOT_FOUND);
        }
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new RuntimeException(STUDENT_NOT_FOUND);
        }
        studentRepository.deleteById(id);
    }

    public List<Student> findByAge(int age) {
        if (age > 0) {
            return studentRepository.findByAge(age);
        }
        return Collections.emptyList();
    }

    public List<Student> findByAgeBetween(int minAge, int maxAge) {
        if (minAge > 0 && maxAge > minAge) {
            return studentRepository.findByAgeBetween(minAge, maxAge);
        }
        return Collections.emptyList();
    }

    public Faculty getStudentFaculty(Long studentId) {
        Student student = findStudent(studentId);
        Faculty faculty = student.getFaculty();
        if (faculty == null) {
            throw new StudentHasNoFacultyException("Student has no faculty");
        }
        return faculty;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
}
