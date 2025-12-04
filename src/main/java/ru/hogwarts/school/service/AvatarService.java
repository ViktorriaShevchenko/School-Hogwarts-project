package ru.hogwarts.school.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarService {

    private static final Logger logger = LoggerFactory.getLogger(AvatarService.class);
    private static final String STUDENT_NOT_FOUND = "Student not found";
    private static final String AVATAR_NOT_FOUND = "Avatar not found";


    @Value("${path.to.avatars.folder}")
    private String avatarsDir;

    private final AvatarRepository avatarRepository;
    private final StudentRepository studentRepository;

    public AvatarService(AvatarRepository avatarRepository, StudentRepository studentRepository) {
        this.avatarRepository = avatarRepository;
        this.studentRepository = studentRepository;
    }

    public Long uploadAvatar(Long studentId, MultipartFile file) throws IOException {
        logger.info("Was invoked method for upload avatar");
        logger.debug("Uploading avatar for student id: {}, file size: {}", studentId, file.getSize());

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> {
                    logger.error("Student with id = {} not found for avatar upload", studentId);
                    return new RuntimeException(STUDENT_NOT_FOUND);
                });

        String extension = getExtension(file.getOriginalFilename());
        Path filePath = Path.of(avatarsDir, "student-" + studentId + "." + extension);
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (InputStream is = file.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)) {
            bis.transferTo(bos);
        }

        Avatar avatar = findOrCreateAvatar(studentId);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(file.getBytes());

        Avatar savedAvatar = avatarRepository.save(avatar);
        logger.info("Avatar uploaded successfully for student id: {}, avatar id: {}", studentId, savedAvatar.getId());
        return savedAvatar.getId();
    }

    public Avatar findAvatar(Long studentId) {
        logger.info("Was invoked method for find avatar");
        logger.debug("Looking for avatar of student id: {}", studentId);
        return avatarRepository.findByStudentId(studentId)
                .orElseThrow(() -> {
                    logger.error("There is no avatar for student id = " + studentId);
                    return new RuntimeException(AVATAR_NOT_FOUND);
                });
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public Avatar findOrCreateAvatar(Long studentId) {
        logger.debug("Finding or creating avatar for student id: {}", studentId);
        return avatarRepository.findByStudentId(studentId)
                .orElseGet(() -> {
                    logger.info("Creating new avatar for student id: {}", studentId);
                    Avatar newAvatar = new Avatar();
                    Student student = studentRepository.findById(studentId)
                            .orElseThrow(() -> {
                                logger.error("Student not found while creating avatar for id = " + studentId);
                                return new RuntimeException("Student not found");
                            });
                    newAvatar.setStudent(student);
                    return avatarRepository.save(newAvatar);
                });
    }

    public Page<Avatar> getAllAvatars(int page, int size) {
        logger.info("Was invoked method for get all avatars with pagination");
        logger.debug("Getting avatars page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Avatar> avatars = avatarRepository.findAll(pageable);
        logger.debug("Found {} avatars on page {}", avatars.getNumberOfElements(), page);
        return avatars;
    }
}
