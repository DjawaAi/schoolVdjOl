package ru.vdjOlhogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.vdjOlhogwarts.school.model.Student;

import java.util.HashMap;
import java.util.Map;

@Service
public class StudentService {
    private final Map<Long, Student> students = new HashMap<>();
    private Long countId = 1L;

    public Student createStudent(Student Student) {
        students.put(countId, Student);
        countId++;
        return Student;
    }

    public Student getStudent(Long StudentId) {
        return students.get(StudentId);
    }

    public Student updateStudent(Long StudentId, Student Student) {
        students.put(StudentId, Student);
        return Student;
    }

    public Student deleteStudent(Long StudentId) {
        return students.remove(StudentId);
    }
}

