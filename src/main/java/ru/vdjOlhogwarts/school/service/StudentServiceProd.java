package ru.vdjOlhogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.vdjOlhogwarts.school.model.Student;
import ru.vdjOlhogwarts.school.model.WorksOnStudentsTable;
import ru.vdjOlhogwarts.school.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

@Service
@Profile("production")
public class StudentServiceProd implements StudentService {

    
    Logger logger = LoggerFactory.getLogger(StudentServiceProd.class);

    @Autowired
    private StudentRepository studentRepository;

    public StudentServiceProd(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        logger.info("Вызван метод для добавления студента");
        logger.info("Добавлен новый студент: {}", student);
        return studentRepository.save(student);
    }

    public Student getStudent(Long studentId) {
        logger.info("Вызван метод для получения данных о студенте с ID: {}", studentId);
        Optional<Student> studentOptional = studentRepository.findById(studentId);
        if (studentOptional.isPresent()) {
            logger.info("Студент найден: {}", studentOptional.get().getName());
            return studentOptional.get();
        } else {
            logger.error("Студента с таким идентификатором нет: {}", studentId);
            return null;
        }
    }

    public Student updateStudent(Student student) {
        logger.info("Вызван метод для обновления даных студента с ID: {}", student.getId());
        if (!studentRepository.existsById(student.getId())) {
            logger.error("Студента: {} для обновления не найден", student.getId());
            return null;
        }
        logger.info("Студент успешно обновлён: {}", student);
        return studentRepository.save(student);
    }

    public void deleteStudent(Long studentId) {
        logger.info("Вызван метод для удаления студента: {}", studentId);
        studentRepository.deleteById(studentId);
    }

    public List<Student> findStudentsByAge(int age) {
        logger.info("The method for selecting students by age has been called");
        return studentRepository.findByAge(age);
    }

    public List<Student> findStudentsByAgeBetween(int minAge, int maxAge) {
        logger.info("The method for selecting students aged from {} to {} years has been called", minAge, maxAge);
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    public Long getNumberOfStudents() {
        return studentRepository.getStudentsNumber();
    }

    public WorksOnStudentsTable getAverageAgeOfStudents() {
        return studentRepository.getAverageAgeOfStudents();
    }

    public List<Student> getLastFiveStudentsInTable() {
        return studentRepository.getLastFiveStudentsInTable();
    }

    public List<Student> getStudentByName(String name) {
        logger.info("A method has been called to search for a student by name: {}", name);
        if (!studentRepository.existsByName(name)) {
            logger.error("Student with the name {} not found", name);
            return null;
        } else {
            logger.info("Student with the name {} found", name);
            return studentRepository.getStudentByName(name);
        }
    }
}
