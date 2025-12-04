package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.StudentHasNoFacultyException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private static final String STUDENT_NOT_FOUND = "Student not found";

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student addStudent(Student student) {
        logger.info("Was invoked method for create student");
        logger.debug("Creating student with name: {}", student.getName());
        return studentRepository.save(student);
    }

    public Student findStudent(Long id) {
        logger.info("Was invoked method for find student by id: {}", id);
        logger.debug("Looking for student with id: {}", id);
        return studentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("There is not student with id = " + id);
                    return new RuntimeException(STUDENT_NOT_FOUND);
                });
    }

    public Student editStudent(Student student) {
        logger.info("Was invoked method for edit student");
        Long studentId = student.getId();
        if (!studentRepository.existsById(studentId)) {
            logger.error("Cannot edit student. Student with id = {} not found", studentId);
            throw new RuntimeException(STUDENT_NOT_FOUND);
        }
        logger.debug("Editing student with id: {}", studentId);
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        logger.info("Was invoked method for delete student");
        if (!studentRepository.existsById(id)) {
            logger.warn("Attempt to delete non-existing student with id: {}", id);
            throw new RuntimeException(STUDENT_NOT_FOUND);
        }
        logger.debug("Deleting student with id: {}", id);
        studentRepository.deleteById(id);
    }

    public List<Student> findByAge(int age) {
        logger.info("Was invoked method for find students by age: {}", age);
        if (age > 0) {
            logger.debug("Searching students with age: {}", age);
            return studentRepository.findByAge(age);
        }
        logger.warn("Invalid age parameter: {}", age);
        return Collections.emptyList();
    }

    public List<Student> findByAgeBetween(int minAge, int maxAge) {
        logger.info("Was invoked method for find students by age range: {} - {}", minAge, maxAge);
        if (minAge > 0 && maxAge > minAge) {
            logger.debug("Searching students with age between {} and {}", minAge, maxAge);
            return studentRepository.findByAgeBetween(minAge, maxAge);
        }
        logger.warn("Invalid age range parameters: min={}, max={}", minAge, maxAge);
        return Collections.emptyList();
    }

    public Faculty getStudentFaculty(Long studentId) {
        logger.info("Was invoked method for get student faculty");
        Student student = findStudent(studentId);
        Faculty faculty = student.getFaculty();
        if (faculty == null) {
            logger.warn("Student with id = {} has no faculty", studentId);
            throw new StudentHasNoFacultyException("Student has no faculty");
        }
        logger.debug("Found faculty for student id {}: {}", studentId, faculty.getName());
        return faculty;
    }

    public List<Student> getAllStudents() {
        logger.info("Was invoked method for get all students");
        logger.debug("Retrieving all students from database");
        return studentRepository.findAll();
    }

    public long countAllStudents() {
        logger.info("Was invoked method for count all students");
        return studentRepository.countAllStudents();
    }

    public double findAverageAge() {
        logger.info("Was invoked method for find average age");
        return studentRepository.findAverageAge();
    }

    public List<Student> findLastFiveStudents() {
        logger.info("Was invoked method for find last five students");
        logger.debug("Retrieving last five students ordered by id");
        return studentRepository.findLastFiveStudents();
    }

    public List<String> getStudentNamesStartingWithA() {
        logger.info("Was invokes method for get students names starting with A");

        return studentRepository.findAll().stream()
                .map(Student::getName)
                .map(String::toUpperCase)
                .filter(name -> name.startsWith("A") || name.startsWith("А"))
                .sorted()
                .collect(Collectors.toList());
    }

    public Double getAverageAgeAllStudent() {
        logger.info("Was invoked method for get average age of all students");

        return studentRepository.findAll().stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0.0);
    }
}
