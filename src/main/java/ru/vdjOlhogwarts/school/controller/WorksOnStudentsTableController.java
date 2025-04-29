package ru.vdjOlhogwarts.school.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vdjOlhogwarts.school.model.Student;
import ru.vdjOlhogwarts.school.model.WorksOnStudentsTable;
import ru.vdjOlhogwarts.school.service.StudentService;
import ru.vdjOlhogwarts.school.service.StudentServiceProd;

import java.util.List;

@RestController
public class WorksOnStudentsTableController {

    private final StudentService studentService;

    public WorksOnStudentsTableController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/student-number")
    public Long getNumberOfStudents() {
        return studentService.getNumberOfStudents();
    }

    @GetMapping("/student-avg-age")
    public WorksOnStudentsTable getAverageAgeOfStudents() {
        return studentService.getAverageAgeOfStudents();
    }

    @GetMapping("/student-last-five")
    public List<Student> getLastFiveStudentsInTable() {
        return studentService.getLastFiveStudentsInTable();
    }
}
