package ru.hogwarts.school.webMvcTest;

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
public class FacultyControllerWebMvcTest {

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
    void deleteFacultyTest() throws Exception {
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
    }

    @Test
    void getLongestFacultyNameTest() throws Exception {
        when(facultyService.getLongestFacultyName()).thenReturn("Гриффиндор");

        mockMvc.perform(get("/faculty/longest-name"))
                .andExpect(status().isOk())
                .andExpect(content().string("Гриффиндор"));
    }

    @Test
    void getLongestFacultyNameWhenEmptyTest() throws Exception {
        when(facultyService.getLongestFacultyName()).thenReturn("");

        mockMvc.perform(get("/faculty/longest-name"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }
}
