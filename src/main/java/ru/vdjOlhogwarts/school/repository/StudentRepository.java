package ru.vdjOlhogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vdjOlhogwarts.school.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
