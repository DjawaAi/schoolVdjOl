package ru.vdjOlhogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.vdjOlhogwarts.school.model.Faculty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    private final Map<Long, Faculty> faculties = new HashMap<>();
    private Long countId = 0L;

    public Faculty createFaculty(Faculty faculty) {
        faculties.put(countId, faculty);
        countId++;
        return faculty;
    }

    public Faculty getFaculty(Long facultyId) {
        return faculties.get(facultyId);
    }

    public Faculty updateFaculty(Long facultyId, Faculty faculty) {
        faculties.put(facultyId, faculty);
        return faculty;
    }

    public Faculty deleteFaculty(Long facultyId) {
        return faculties.remove(facultyId);
    }

    public List<Faculty> findByColor(String color) {
        return faculties.values()
                .stream()
                .filter(faculty -> faculty.getColor().equalsIgnoreCase(color))
                .collect(Collectors.toList());
    }
}
