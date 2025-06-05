package ru.vdjOlhogwarts.school.service;

import ru.vdjOlhogwarts.school.model.Student;
import ru.vdjOlhogwarts.school.model.WorksOnStudentsTable;

import java.util.List;

public interface StudentService {

    Student createStudent(Student student);

    Student getStudent(Long studentId);

    Student updateStudent(Student student);

    void deleteStudent(Long studentId);

    List<Student> findStudentsByAge(int age);

    List<Student> findStudentsByAgeBetween(int minAge, int maxAge);

    Long getNumberOfStudents();

    WorksOnStudentsTable getAverageAgeOfStudents();

    List<Student> getLastFiveStudentsInTable();

    List<Student> getStudentByName(String name);


}
