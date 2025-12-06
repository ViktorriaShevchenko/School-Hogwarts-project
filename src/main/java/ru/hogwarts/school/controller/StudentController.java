package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    private static final String STUDENT_HAS_NO_FACULTY = "Student has no faculty";

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("{id}")
    public Student getStudent(@PathVariable Long id) {
        return studentService.findStudent(id);
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.addStudent(student);
    }

    @PutMapping
    public Student editStudent(@RequestBody Student student) {
        return studentService.editStudent(student);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }

    @GetMapping
    public List<Student> findStudents(@RequestParam(required = false) Integer age) {
        if (age != null && age > 0) {
            return studentService.findByAge(age);
        }
        return studentService.getAllStudents();
    }

    @GetMapping("/age-between")
    public List<Student> getStudentsByAgeRange(
            @RequestParam int minAge,
            @RequestParam int maxAge) {
        return studentService.findByAgeBetween(minAge, maxAge);
    }

    @GetMapping("{id}/faculty")
    public Faculty getStudentFaculty(@PathVariable Long id) {
        return studentService.getStudentFaculty(id);
    }

    @GetMapping("/count")
    public long countStudents() {
        return studentService.countAllStudents();
    }

    @GetMapping("/average-age-sql")
    public double getAverageAge() {
        return studentService.findAverageAge();
    }

    @GetMapping("/last-five")
    public List<Student> getLastFiveStudents() {
        return studentService.findLastFiveStudents();
    }

    @GetMapping("/names-starting-with-a")
    public List<String> getStudentsNamesStartingWithA() {
        return studentService.getStudentNamesStartingWithA();
    }

    @GetMapping("/average-age-stream")
    public Double getAverageAgeAllStudents() {
        return studentService.getAverageAgeAllStudent();
    }

    @GetMapping("/print-parallel")
    public void printStudentParallel() {
        List<Student> students = studentService.getFirstSixStudents();

        if (students.size() < 6) {
            return;
        }

        System.out.println(Thread.currentThread().getName() + ": " + students.get(0).getName());
        System.out.println(Thread.currentThread().getName() + ": " + students.get(1).getName());

        Thread thread1 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + ": " + students.get(2).getName());
            System.out.println(Thread.currentThread().getName() + ": " + students.get(3).getName());
        });

        Thread thread2 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + ": " + students.get(4).getName());
            System.out.println(Thread.currentThread().getName() + ": " + students.get(5).getName());
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @GetMapping("/print-synchronized")
    public void printStudentsSynchronized() {
        List<Student> students = studentService.getFirstSixStudents();

        if (students.size() < 6) {
            return;
        }

        studentService.printStudentNameSync(students.get(0));
        studentService.printStudentNameSync(students.get(1));

        Thread thread1 = new Thread(() -> {
            studentService.printStudentNameSync(students.get(2));
            studentService.printStudentNameSync(students.get(3));
        });

        Thread thread2 = new Thread(() -> {
            studentService.printStudentNameSync(students.get(4));
            studentService.printStudentNameSync(students.get(5));
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
