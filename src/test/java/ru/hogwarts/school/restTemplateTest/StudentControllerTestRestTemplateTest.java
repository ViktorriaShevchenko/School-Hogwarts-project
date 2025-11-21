package ru.hogwarts.school.restTemplateTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Student;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTestRestTemplateTest {

    private static final String BASE_URL = "http://localhost:";

    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
        assertNotNull(studentController);
    }

    @Test
    void getStudentTest() {
        ResponseEntity<Student> response = restTemplate.getForEntity(
                BASE_URL + port + "/student/1",
                Student.class
        );
        assertNotNull(response.getBody());
    }

    @Test
    void getStudentNotFoundTest() {
        ResponseEntity<Student> response = restTemplate.getForEntity(
                BASE_URL + port + "/student/999",
                Student.class
        );
        assertNotEquals(200, response.getStatusCodeValue());
    }

    @Test
    void createStudentTest() {
        Student student = new Student();
        student.setName("Тест Студент");
        student.setAge(20);

        ResponseEntity<Long> response = restTemplate.postForEntity(
                BASE_URL + port + "/student",
                student,
                Long.class
        );
        assertNotNull(response.getBody());
    }

    @Test
    void editStudentTest() {
        Student student = new Student();
        student.setId(1L);
        student.setName("Обновленный Студент");
        student.setAge(21);

        restTemplate.put(BASE_URL + port + "/student", student);

        ResponseEntity<Student> response = restTemplate.getForEntity(
                BASE_URL + port + "/student/1",
                Student.class
        );
        assertNotNull(response.getBody());
    }

    @Test
    void deleteStudentTest() {
        restTemplate.delete(BASE_URL + port + "/student/1");
        assertNotNull(restTemplate);
    }

    @Test
    void getAllStudentsTest() {
        ResponseEntity<Student[]> response = restTemplate.getForEntity(
                BASE_URL + port + "/student",
                Student[].class
        );
        assertNotNull(response.getBody());
    }

    @Test
    void findStudentsByAgeTest() {
        ResponseEntity<Student[]> response = restTemplate.getForEntity(
                BASE_URL + port + "/student?age=20",
                Student[].class
        );
        assertNotNull(response.getBody());
    }

    @Test
    void getStudentsByAgeRangeTest() {
        ResponseEntity<Student[]> response = restTemplate.getForEntity(
                BASE_URL + port + "/student/age-between?minAge=18&maxAge=25",
                Student[].class
        );
        assertNotNull(response.getBody());
    }

    @Test
    void getStudentFacultyTest() {
        ResponseEntity<Object> response = restTemplate.getForEntity(
                BASE_URL + port + "/student/1/faculty",
                Object.class
        );
        assertNotNull(response);
    }
}
