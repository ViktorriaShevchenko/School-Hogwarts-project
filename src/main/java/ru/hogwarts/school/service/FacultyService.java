package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;

@Service
public class FacultyService {

    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    private static final String FACULTY_NOT_FOUND = "Faculty not found";

    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    public Faculty addFaculty(Faculty faculty) {
        logger.info("Was invoked method for create faculty");
        logger.debug("Creating faculty with name: {}", faculty.getName());
        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(Long id) {
        logger.info("Was invoked method for find faculty by id: {}", id);
        return facultyRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("There is not faculty with id = " + id);
                    return new RuntimeException(FACULTY_NOT_FOUND);
                });
    }

    public Faculty editFaculty(Faculty faculty) {
        logger.info("Was invoked method for edit faculty");
        if (facultyRepository.existsById(faculty.getId())) {
            logger.debug("Editing faculty with id: {}", faculty.getId());
            return facultyRepository.save(faculty);
        }
        logger.error("Cannot edit faculty. Faculty with id = {} not found", faculty.getId());
        throw new RuntimeException(FACULTY_NOT_FOUND);
    }

    public void deleteFaculty(Long id) {
        logger.info("Was invoked method for delete faculty");
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Cannot delete faculty. Faculty with id = {} not found", id);
                    return new RuntimeException(FACULTY_NOT_FOUND);
                });
        logger.debug("Deleting faculty with id: {}", id);
        facultyRepository.deleteById(id);
    }

    public List<Faculty> findByColor(String color) {
        logger.info("Was invoked method for find faculties by color: {}", color);
        logger.debug("Searching faculties with color: {}", color);
        return facultyRepository.findByColor(color);
    }

    public List<Faculty> findByNameOrColor(String name, String color) {
        logger.info("Was invoked method for find faculties by name or color");
        logger.debug("Searching faculties with name: {} or color: {}", name, color);
        return facultyRepository.findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(name, color);
    }

    public List<Student> getFacultyStudents(Long facultyId) {
        logger.info("Was invoked method for get faculty students");
        logger.debug("Getting students for faculty id: {}", facultyId);
        return studentRepository.findByFacultyId(facultyId);
    }

    public List<Faculty> getAllFaculties() {
        logger.info("Was invoked method for get all faculties");
        logger.debug("Retrieving all faculties from database");
        return facultyRepository.findAll();
    }

    public String getLongestFacultyName() {
        logger.info("Was invoked method for get longest faculty name");

        return facultyRepository.findAll().stream()
                .map(Faculty::getName)
                .max(Comparator.comparingInt(String::length))
                .orElse("");
    }
}
