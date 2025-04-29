package ru.vdjOlhogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vdjOlhogwarts.school.model.Faculty;
import ru.vdjOlhogwarts.school.model.Student;
import ru.vdjOlhogwarts.school.repository.FacultyRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class FacultyService {

    Logger logger = LoggerFactory.getLogger(FacultyService.class);

    @Autowired
    private FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        logger.info("Вызван метод для создания факультета");
        logger.info("Создан новый факультет: {}", faculty);
        return facultyRepository.save(faculty);
    }

    public Faculty getFaculty(Long facultyId) {
        logger.info("Вызван метод для получения данных факультета с ID: {}", facultyId);
        Optional<Faculty> facultyOptional = facultyRepository.findById(facultyId);
        if (facultyOptional.isPresent()) {
            logger.info("Факультет найден: {}", facultyOptional.get().getName());
            return facultyOptional.get();
        } else {
            logger.error("Факультет с таким идентификатором осутствует: {}", facultyId);
            return null;
        }
    }

    public Faculty updateFaculty(Long facultyId, Faculty faculty) {
        logger.info("Вызван метод для обновления даных факультета с ID: {}", facultyId);
        Optional<Faculty> existingFaculty = facultyRepository.findById(facultyId);
        if (existingFaculty.isEmpty()) {
            logger.error("Факультет: {} для обновления не найден", facultyId);
            return null;
        } else {
            logger.info("Факультет успешно обновлён: {}", faculty);
            return facultyRepository.save(faculty);
        }
    }

    public void deleteFaculty(Long facultyId) {
        logger.info("Вызван метод для удаления факультета: {}", facultyId);
        facultyRepository.deleteById(facultyId);
    }

    public Faculty findByColor(String color) {
        logger.info("Вызван метод для поиска факультета по цвету");
        return facultyRepository.findByColorIgnoreCase(color);
    }

    public Faculty findByName(String name) {
        logger.info("Вызван метод для поиска факультета по имени");
        return facultyRepository.findByNameIgnoreCase(name);
    }

    public Collection<Faculty> findByNameOrColor(String name, String color) {
        logger.info("Вызван метод для поиска факультета по имени или цвету");
        return facultyRepository.findFacultiesByNameOrColorIgnoreCase(name, color);
    }

    public List<Student> getStudentsByFacultyId(Long facultyId) {
        logger.info("Вызван метод для поиска студентов из факультета");
        Optional<Faculty> facultyOptional = facultyRepository.findById(facultyId);
        if (facultyOptional.isPresent()) {
            Faculty faculty = facultyOptional.get();
            return facultyRepository.findById(facultyId).get().getStudents();
        } else {
            return Collections.emptyList();
        }
    }
}
