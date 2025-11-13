package ru.hogwarts.school;

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
public class StudentControllerTests {

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
    void testGetStudent() {
        ResponseEntity<Student> response = restTemplate.getForEntity(
                BASE_URL + port + "/student/1",
                Student.class
        );
        assertNotNull(response.getBody());
    }

    @Test
    void testGetStudentNotFound() {
        ResponseEntity<Student> response = restTemplate.getForEntity(
                BASE_URL + port + "/student/999",
                Student.class
        );
        assertNotEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testCreateStudent() {
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
    void testEditStudent() {
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
    void testDeleteStudent() {
        restTemplate.delete(BASE_URL + port + "/student/1");
        assertNotNull(restTemplate);
    }

    @Test
    void testGetAllStudents() {
        ResponseEntity<Student[]> response = restTemplate.getForEntity(
                BASE_URL + port + "/student",
                Student[].class
        );
        assertNotNull(response.getBody());
    }

    @Test
    void testFindStudentsByAge() {
        ResponseEntity<Student[]> response = restTemplate.getForEntity(
                BASE_URL + port + "/student?age=20",
                Student[].class
        );
        assertNotNull(response.getBody());
    }

    @Test
    void testGetStudentsByAgeRange() {
        ResponseEntity<Student[]> response = restTemplate.getForEntity(
                BASE_URL + port + "/student/age-between?minAge=18&maxAge=25",
                Student[].class
        );
        assertNotNull(response.getBody());
    }

    @Test
    void testGetStudentFaculty() {
        ResponseEntity<Object> response = restTemplate.getForEntity(
                BASE_URL + port + "/student/1/faculty",
                Object.class
        );
        assertNotNull(response);
    }
}
