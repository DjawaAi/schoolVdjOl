package ru.vdjOlhogwarts.school.Controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.vdjOlhogwarts.school.model.Faculty;
import ru.vdjOlhogwarts.school.model.Student;
import ru.vdjOlhogwarts.school.repository.FacultyRepository;
import ru.vdjOlhogwarts.school.repository.StudentRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class FacultyControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FacultyRepository facultyRepository;


    @Autowired
    private StudentRepository studentRepository;

    private Faculty faculty;
    private Faculty faculty1;
    private Faculty faculty2;
    private Student student;


    @BeforeEach
    void setUP() {
        faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Tj");
        faculty.setColor("Gr");

        faculty1 = new Faculty();
        faculty1.setId(2L);
        faculty1.setName("Sd");
        faculty1.setColor("Yl");

        faculty2 = new Faculty();
        faculty2.setId(3L);
        faculty2.setName("SdI");
        faculty2.setColor("YlG");

        student = new Student();
        student.setId(1L);
        student.setName("Ds");
        student.setAge(30);
        student.setFaculty(faculty1);

        faculty = facultyRepository.save(faculty);
        faculty1 = facultyRepository.save(faculty1);
        facultyRepository.save(faculty2);
        studentRepository.save(student);
    }

    @Test
    void createFacultyTest() {
        Faculty newFaculty = new Faculty();
        newFaculty.setName("Art");
        newFaculty.setColor("Red");

        ResponseEntity<Faculty> response = restTemplate.postForEntity("/faculty", newFaculty, Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Art");
        assertThat(response.getBody().getColor()).isEqualTo("Red");
    }

    @Test
    void getFacultyByColor() {
        ResponseEntity<Faculty> response = restTemplate.getForEntity("/faculty/filter/{color}", Faculty.class, "Gr");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getColor()).isEqualTo("Gr");
    }

    @Test
    void getFaculty() {
        ResponseEntity<Faculty> response = restTemplate.getForEntity("/faculty/{facultyId}", Faculty.class, faculty.getId());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo(faculty.getName());
    }

    @Test
    void updateFaculty() {
        faculty.setName("Mg");

        HttpEntity<Faculty> requestEntity = new HttpEntity<>(faculty);
        ResponseEntity<Faculty> response = restTemplate.exchange("/faculty", HttpMethod.PUT, requestEntity, Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("Mg");
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void getFacultiesByNameOrColor() {
        // По имени
        ResponseEntity<List<Faculty>> responseByName = restTemplate.exchange(
                "/faculty/filter/nameOrColor?name={name}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Faculty>>() {
                }, "Sd");
        assertThat(responseByName.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseByName.getBody()).isNotNull();
        assertThat(responseByName.getBody().size()).isEqualTo(1);

        // По цвету
        ResponseEntity<List<Faculty>> responseByColor = restTemplate.exchange(
                "/faculty/filter/nameOrColor?color={color}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Faculty>>() {
                },
                "Yl"
        );
        assertThat(responseByColor.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseByColor.getBody()).isNotNull();
        assertThat(responseByColor.getBody().size()).isEqualTo(1);

        // По имени и цвету
        ResponseEntity<List<Faculty>> responseByNameAndColor = restTemplate.exchange(
                "/faculty/filter/nameOrColor?name={name}&color={color}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Faculty>>() {
                },
                "Sd", "Yl"
        );
        assertThat(responseByNameAndColor.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseByNameAndColor.getBody()).isNotNull();
        assertThat(responseByNameAndColor.getBody().size()).isEqualTo(1);
    }

    @Test
    void deleteFaculty() {
        ResponseEntity<Void> deleteResponse = restTemplate.exchange("/faculty/{facultyId}", HttpMethod.DELETE, null, Void.class, faculty2.getId());
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<Faculty> getResponse = restTemplate.getForEntity("/faculty/{facultyId}", Faculty.class, faculty2.getId());
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void getFacultyStudents_ReturnsStudents_WhenFacultyExists() {
        Long facultyId = 2L;

        ResponseEntity<List<Student>> response = restTemplate.getForEntity("/faculty/find/{facultyId}",
                (Class<List<Student>>) (Class<?>) List.class, facultyId);

        // Проверка статуса ответа
        assertThat(response.getStatusCode()).isEqualTo(OK);

        // Проверка содержимого
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isGreaterThan(0); // Проверка на наличие студентов
    }

    @Test
    void getFacultyStudents_ReturnsNotFound_WhenFacultyDoesNotExist() {
        Long invalidFacultyId = 999L; // Убедитесь, что такого факультета нет

        ResponseEntity<List<Student>> response = restTemplate.exchange(
                "/faculty/find/{facultyId}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {
                },
                invalidFacultyId);

        // Проверка статуса ответа
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}