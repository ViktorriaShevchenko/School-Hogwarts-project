package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/faculty")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("{id}")
    public Faculty getFaculty(@PathVariable Long id) {
        return facultyService.findFaculty(id);
    }

    @PostMapping
    public Long createFaculty(@RequestBody Faculty faculty) {
        Faculty savedFaculty = facultyService.addFaculty(faculty);
        return savedFaculty.getId();
    }

    @PutMapping
    public Faculty editFaculty(@RequestBody Faculty faculty) {
        return facultyService.editFaculty(faculty);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
    }

    @GetMapping
    public List<Faculty> findFaculties(@RequestParam(required = false) String color) {
        if (color != null && !color.isBlank()) {
            return facultyService.findByColor(color);
        }
        return facultyService.getAllFaculties();
    }

    @GetMapping("/search")
    public List<Faculty> searchFaculties(@RequestParam String search) {
        return facultyService.findByNameOrColor(search, search);
    }

    @GetMapping("{id}/students")
    public List<Student> getFacultyStudents(@PathVariable Long id) {
        List<Student> students = facultyService.getFacultyStudents(id);
        return students != null ? students : Collections.emptyList();
    }

    @GetMapping("/longest-name")
    public String getLongestFacultyName() {
        return facultyService.getLongestFacultyName();
    }
}
