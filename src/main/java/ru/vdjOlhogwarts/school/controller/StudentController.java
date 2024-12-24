package ru.vdjOlhogwarts.school.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vdjOlhogwarts.school.model.Student;
import ru.vdjOlhogwarts.school.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
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
        }
        return ResponseEntity.ok(student);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        student.setId(id);
        Student crtdStudent = studentService.updateStudent(student);
        if (crtdStudent == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(crtdStudent);
    }

    @DeleteMapping("{studentId}")
    public ResponseEntity deleteStudent(@PathVariable Long studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/filter/{age}")
    public List<Student> getStudentsByAge(@PathVariable int age) {
        return studentService.findStudentsByAge(age);
    }

    @GetMapping("/filter/{minAge}/{maxAge}")
    public ResponseEntity<List<Student>> getStudentsByAgeBetween(@PathVariable int minAge, @PathVariable int maxAge) {
        List<Student> students = studentService.findStudentsByAgeBetween(minAge, maxAge);
        if (students.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(students);
    }

    @GetMapping("/find/{studentId}")
    public ResponseEntity<String> getFacultyOfStudent(@PathVariable Long studentId) {
        String faculty = studentService.getStudent(studentId).getFaculty().getName();
        if (faculty == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.ok(faculty);
        }
    }

}
