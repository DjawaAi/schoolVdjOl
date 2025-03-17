package ru.vdjOlhogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.vdjOlhogwarts.school.model.Student;
import ru.vdjOlhogwarts.school.model.WorksOnStudentsTable;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByAge(int age);

    List<Student> findByAgeBetween(int minAge, int maxAge);

    @Query(value = "SELECT COUNT(id) FROM student", nativeQuery = true)
    Long getStudentsNumber();

    @Query(value = "SELECT AVG(age) AS ageAVG FROM student", nativeQuery = true)
    WorksOnStudentsTable getAverageAgeOfStudents();

    @Query(value = "SELECT * FROM (SELECT * FROM student ORDER BY id DESC LIMIT 5) AS lastStudents ORDER BY id ASC", nativeQuery = true)
    List<Student> getLastFiveStudentsInTable();
}
