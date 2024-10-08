package ru.vdjOlhogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vdjOlhogwarts.school.model.Faculty;
import ru.vdjOlhogwarts.school.service.FacultyService;

import java.util.List;

@RequestMapping("{faculty}")
@RestController
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;

    }

    @PostMapping
    public ResponseEntity<Faculty> createFaculty(@RequestBody Faculty faculty) {
        Faculty crtdFaculty = facultyService.createFaculty(faculty);
        return ResponseEntity.ok(crtdFaculty);
    }

    @GetMapping("{facultyId}")
    public ResponseEntity<Faculty> getFaculty(@PathVariable Long facultyId) {
        Faculty faculty = facultyService.getFaculty(facultyId);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    @PutMapping
    public ResponseEntity<Faculty> updateFaculty(@RequestBody Faculty faculty) {
        Faculty crtdFaculty = facultyService.updateFaculty(faculty.getId(), faculty);
        if (crtdFaculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(crtdFaculty);
    }

    @DeleteMapping("{facultyId}")
    public ResponseEntity<Faculty> deleteFaculty(@PathVariable Long facultyId) {
        Faculty delFaculty = facultyService.deleteFaculty(facultyId);
        return ResponseEntity.ok(delFaculty);
    }

    @GetMapping("{filter}")
    public List<Faculty> getFacultiesByColor(@RequestParam String color) {
        return facultyService.findByColor(color);
    }
}
