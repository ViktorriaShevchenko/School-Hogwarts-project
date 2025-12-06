package ru.hogwarts.school.webMvcTest;

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

import java.util.Arrays;
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
    }

    @Test
    void getStudentsNamesStartingWithATest() throws Exception {

        List<String> names = Arrays.asList("АННА", "АЛЕКСЕЙ", "АЛЕКСАНДР");
        when(studentService.getStudentNamesStartingWithA()).thenReturn(names);

        mockMvc.perform(get("/student/names-starting-with-a"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("АННА"))
                .andExpect(jsonPath("$[1]").value("АЛЕКСЕЙ"))
                .andExpect(jsonPath("$[2]").value("АЛЕКСАНДР"))
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void getAverageAgeTest() throws Exception {
        when(studentService.getAverageAgeAllStudent()).thenReturn(20.5);

        mockMvc.perform(get("/student/average-age-stream"))
                .andExpect(status().isOk())
                .andExpect(content().string("20.5"));
    }
}
