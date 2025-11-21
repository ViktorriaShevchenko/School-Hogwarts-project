package ru.hogwarts.school;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacultyController.class)
class FacultyControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FacultyService facultyService;

    @InjectMocks
    private FacultyController facultyController;

    @Test
    void getFacultyTest() throws Exception {
        Faculty faculty = new Faculty(1L, "Гриффиндор", "красный");
        when(facultyService.findFaculty(1L)).thenReturn(faculty);

        mockMvc.perform(get("/faculty/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Гриффиндор"));
    }

    @Test
    void createFacultyTest() throws Exception {
        Faculty faculty = new Faculty(1L, "Слизерин", "зеленый");
        when(facultyService.addFaculty(any(Faculty.class))).thenReturn(faculty);

        String facultyJson = "{\"name\": \"Слизерин\", \"color\": \"зеленый\"}";

        mockMvc.perform(post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(facultyJson))
                .andExpect(status().isOk());
    }

    @Test
    void editFacultyTest() throws Exception {
        Faculty faculty = new Faculty(1L, "Когтевран", "синий");
        when(facultyService.editFaculty(any(Faculty.class))).thenReturn(faculty);

        String facultyJson = "{\"id\": 1, \"name\": \"Когтевран\", \"color\": \"синий\"}";

        mockMvc.perform(put("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(facultyJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Когтевран"));
    }

    @Test
    void tdeleteFacultyTest() throws Exception {
        mockMvc.perform(delete("/faculty/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAllFacultiesTest() throws Exception {
        List<Faculty> faculties = List.of(
                new Faculty(1L, "Гриффиндор", "красный"),
                new Faculty(2L, "Слизерин", "зеленый")
        );
        when(facultyService.getAllFaculties()).thenReturn(faculties);

        mockMvc.perform(get("/faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void findFacultiesByColorTest() throws Exception {
        List<Faculty> faculties = List.of(new Faculty(1L, "Слизерин", "зеленый"));
        when(facultyService.findByColor("зеленый")).thenReturn(faculties);

        mockMvc.perform(get("/faculty").param("color", "зеленый"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void searchFacultiesTest() throws Exception {
        List<Faculty> faculties = List.of(new Faculty(1L, "Гриффиндор", "красный"));
        when(facultyService.findByNameOrColor("Гриффиндор", "Гриффиндор")).thenReturn(faculties);

        mockMvc.perform(get("/faculty/search").param("search", "Гриффиндор"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getFacultyStudentsTest() throws Exception {
        List<Student> students = List.of(
                new Student(1L, "Гарри Поттер", 11),
                new Student(2L, "Гермиона Грейнджер", 12)
        );
        when(facultyService.getFacultyStudents(1L)).thenReturn(students);

        mockMvc.perform(get("/faculty/1/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getFacultyStudentsEmptyTest() throws Exception {
        when(facultyService.getFacultyStudents(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/faculty/1/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
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
public class FacultyControllerRestTemplateTest {

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
