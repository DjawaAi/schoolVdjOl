package ru.vdjOlhogwarts.school.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.vdjOlhogwarts.school.model.Faculty;
import ru.vdjOlhogwarts.school.model.Student;
import ru.vdjOlhogwarts.school.model.WorksOnStudentsTable;

import java.util.List;

@Service
@Profile("development")
public class StudentServiceTest implements StudentService {

    @Override
    public Student createStudent(Student student) {
        return student;
    }

    @Override
    public Student getStudent(Long studentId) {
        return null;
    }

    @Override
    public Student updateStudent(Student student) {
        return null;
    }

    @Override
    public void deleteStudent(Long studentId) {

    }

    @Override
    public List<Student> findStudentsByAge(int age) {
        return List.of();
    }

    @Override
    public List<Student> findStudentsByAgeBetween(int minAge, int maxAge) {
        return List.of();
    }

    @Override
    public Long getNumberOfStudents() {
        return 0L;
    }

    @Override
    public WorksOnStudentsTable getAverageAgeOfStudents() {
        return null;
    }

    @Override
    public List<Student> getLastFiveStudentsInTable() {
        return List.of();
    }

    @Override
    public List<Student> getStudentByName(String name) {
        return List.of();
    }
}
