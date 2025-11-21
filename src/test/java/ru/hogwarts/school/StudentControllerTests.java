package ru.hogwarts.school;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.exception.StudentHasNoFacultyException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
public class StudentControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @Test
    void getStudentTest() throws Exception {
        Student student = new Student(1L, "Гарри Поттер", 11);
        when(studentService.findStudent(1L)).thenReturn(student);

        mockMvc.perform(get("/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Гарри Поттер"));
    }

    @Test
    void createStudentTest() throws Exception {
        Student student = new Student(1L, "Гермиона Грейнджер", 12);
        when(studentService.addStudent(any(Student.class))).thenReturn(student);

        String studentJson = "{\"name\": \"Гермиона Грейнджер\", \"age\": 12}";

        mockMvc.perform(post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentJson))
                .andExpect(status().isOk());
    }

    @Test
    void editStudentTest() throws Exception {
        Student student = new Student(1L, "Рон Уизли", 12);
        when(studentService.editStudent(any(Student.class))).thenReturn(student);

        String studentJson = "{\"id\": 1, \"name\": \"Рон Уизли\", \"age\": 12}";

        mockMvc.perform(put("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Рон Уизли"));
    }

    @Test
    void deleteStudentTest() throws Exception {
        mockMvc.perform(delete("/student/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAllStudentsTest() throws Exception {
        List<Student> students = List.of(
                new Student(1L, "Гарри Поттер", 11),
                new Student(2L, "Гермиона Грейнджер", 12)
        );
        when(studentService.getAllStudents()).thenReturn(students);

        mockMvc.perform(get("/student"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void findStudentsByAgeTest() throws Exception {
        List<Student> students = List.of(new Student(1L, "Гарри Поттер", 11));
        when(studentService.findByAge(11)).thenReturn(students);

        mockMvc.perform(get("/student").param("age", "11"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getStudentsByAgeRangeTest() throws Exception {
        List<Student> students = List.of(
                new Student(1L, "Гарри Поттер", 11),
                new Student(2L, "Гермиона Грейнджер", 12)
        );
        when(studentService.findByAgeBetween(11, 13)).thenReturn(students);

        mockMvc.perform(get("/student/age-between")
                        .param("minAge", "11")
                        .param("maxAge", "13"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getStudentFacultyTest() throws Exception {
        Faculty faculty = new Faculty(1L, "Гриффиндор", "красный");
        when(studentService.getStudentFaculty(1L)).thenReturn(faculty);

        mockMvc.perform(get("/student/1/faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Гриффиндор"));
    }

    @Test
    void getStudentFacultyNotFoundTest() throws Exception {
        when(studentService.getStudentFaculty(1L))
                .thenThrow(new StudentHasNoFacultyException("Student has no faculty"));

        mockMvc.perform(get("/student/1/faculty"))
                .andExpect(status().isNotFound());
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
public class StudentControllerRestTemplateTest {

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
