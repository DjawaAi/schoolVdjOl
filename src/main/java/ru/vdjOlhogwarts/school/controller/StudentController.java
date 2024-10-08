package ru.vdjOlhogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vdjOlhogwarts.school.model.Student;
import ru.vdjOlhogwarts.school.service.StudentService;

import java.util.List;

@RequestMapping("/student")
@RestController
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;

    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @GetMapping("{studentId}")
    public ResponseEntity<Student> getStudent(@PathVariable Long studentId) {
        Student student = studentService.getStudent(studentId);
        if (student == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(student);
        }
    }

    @PutMapping
    public ResponseEntity<Student> updateStudent(@RequestBody Student student) {
        Student crtdStudent = studentService.updateStudent(student.getId(), student);
        if (crtdStudent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(crtdStudent);
    }

    @DeleteMapping("{studentId}")
    public Student deleteStudent(@PathVariable Long studentId) {
        return studentService.deleteStudent(studentId);
    }

    @GetMapping("{filter}")
    public List<Student> getStudentsByAge(@RequestParam int age) {
        return studentService.findStudentsByAge(age);
    }
}
