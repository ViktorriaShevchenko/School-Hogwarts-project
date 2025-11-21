package ru.hogwarts.school;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerMvcTests {

    private static final String BASE_URL = "http://localhost:";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FacultyController facultyController;

    @Test
    void contextLoads() {
        assertNotNull(facultyController);
    }

    @Test
    void getFacultyTest() {
        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                BASE_URL + port + "/faculty/1",
                Faculty.class
        );
        assertNotNull(response.getBody());
    }

    @Test
    void getFacultyNotFoundTest() {
        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                BASE_URL + port + "/faculty/999",
                Faculty.class
        );
        assertNotEquals(200, response.getStatusCodeValue());
    }

    @Test
    void createFacultyTest() {
        Faculty faculty = new Faculty();
        faculty.setName("Тест Факультет");
        faculty.setColor("синий");

        ResponseEntity<String> response = restTemplate.postForEntity(
                BASE_URL + port + "/faculty",
                faculty,
                String.class
        );
        assertNotNull(response.getBody());
    }

    @Test
    void editFacultyTest() {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Обновленный Факультет");
        faculty.setColor("красный");

        restTemplate.put(BASE_URL + port + "/faculty", faculty);
        assertNotNull(restTemplate);
    }

    @Test
    void deleteFacultyTest() {
        restTemplate.delete(BASE_URL + port + "/faculty/1");
        assertNotNull(restTemplate);
    }

    @Test
    void getAllFacultiesTest() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                BASE_URL + port + "/faculty",
                String.class
        );
        assertNotNull(response.getBody());
    }

    @Test
    void findFacultiesByColorTest() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                BASE_URL + port + "/faculty?color=синий",
                String.class
        );
        assertNotNull(response.getBody());
    }

    @Test
    void searchFacultiesTest() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                BASE_URL + port + "/faculty/search?search=тест",
                String.class
        );
        assertNotNull(response.getBody());
    }

    @Test
    void getFacultyStudentsTest() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                BASE_URL + port + "/faculty/1/students",
                String.class
        );
        assertNotNull(response.getBody());
    }
}
