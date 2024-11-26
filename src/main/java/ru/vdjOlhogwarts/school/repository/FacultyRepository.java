package ru.vdjOlhogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vdjOlhogwarts.school.model.Faculty;

import java.util.Collection;


public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    Faculty findByColorIgnoreCase(String color);

    Faculty findByNameIgnoreCase(String name);

    Collection<Faculty> findFacultiesByNameOrColorIgnoreCase(String name, String color);
}
