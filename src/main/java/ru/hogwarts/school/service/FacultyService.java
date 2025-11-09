package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;

@Service
public class FacultyService {

    private static final String FACULTY_NOT_FOUND = "Faculty not found";

    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    public Faculty addFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(Long id) {
        return facultyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(FACULTY_NOT_FOUND));
    }

    public Faculty editFaculty(Faculty faculty) {
        if (facultyRepository.existsById(faculty.getId())) {
            return facultyRepository.save(faculty);
        }
        throw new RuntimeException(FACULTY_NOT_FOUND);
    }

    public void deleteFaculty(Long id) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(FACULTY_NOT_FOUND));
        facultyRepository.deleteById(id);
    }

    public List<Faculty> findByColor(String color) {
        List<Faculty> faculties = facultyRepository.findByColor(color);
        return faculties != null ? faculties : Collections.emptyList();
    }

    public List<Faculty> findByNameOrColor(String name, String color) {
        List<Faculty> faculties = facultyRepository.findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(name, color);
        return faculties != null ? faculties : Collections.emptyList();
    }

    public List<Student> getFacultyStudents(Long facultyId) {
        List<Student> students = studentRepository.findByFacultyId(facultyId);
        return students != null ? students : Collections.emptyList();
    }

    public List<Faculty> getAllFaculties() {
        List<Faculty> faculties = facultyRepository.findAll();
        return faculties != null ? faculties : Collections.emptyList();
    }
}
