package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.*;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    @Autowired
    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty addFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Optional<Faculty> findFaculty(long id) {
        return facultyRepository.findById(id);
    }

    public Optional<Faculty> editFaculty(Faculty faculty) {
        if (facultyRepository.existsById(faculty.getId())) {
            return Optional.of(facultyRepository.save(faculty));
        }
        return Optional.empty();
    }

    public Optional<Faculty> deleteFaculty(long id) {
        Optional<Faculty> faculty = facultyRepository.findById(id);
        faculty.ifPresent(f -> facultyRepository.deleteById(id));
        return faculty;
    }

    public Collection<Faculty> findByColor(String color) {
        return facultyRepository.findByColor(color);
    }
}
