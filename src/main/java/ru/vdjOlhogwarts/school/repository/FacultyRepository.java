package ru.vdjOlhogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vdjOlhogwarts.school.model.Faculty;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
}
