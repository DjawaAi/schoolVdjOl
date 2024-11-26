package ru.vdjOlhogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vdjOlhogwarts.school.model.Faculty;
import ru.vdjOlhogwarts.school.model.Student;
import ru.vdjOlhogwarts.school.repository.FacultyRepository;

import java.util.Collection;
import java.util.List;


@Service
public class FacultyService {
    @Autowired
    private FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }


    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty getFaculty(Long facultyId) {
        return facultyRepository.findById(facultyId).get();
    }

    public Faculty updateFaculty(Long facultyId, Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(Long facultyId) {
        facultyRepository.deleteById(facultyId);
    }

    public Faculty findByColor(String color) {
        return facultyRepository.findByColorIgnoreCase(color);
    }

    public Faculty findByName(String name) {
        return facultyRepository.findByNameIgnoreCase(name);
    }

    public Collection<Faculty> findByNameOrColor(String name, String color) {
        return facultyRepository.findFacultiesByNameOrColorIgnoreCase(name, color);
    }

    public List<Student> getStudentsByFacultyId(Long facultyId) {
        return facultyRepository.findById(facultyId).get().getStudents();
    }
}
