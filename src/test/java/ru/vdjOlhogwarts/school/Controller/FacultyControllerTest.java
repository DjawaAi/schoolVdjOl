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
import ru.vdjOlhogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class FacultyControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FacultyService facultyService;


    @Autowired
    private FacultyRepository facultyRepository;

    private Faculty faculty;
    private Faculty faculty1;

    @BeforeEach
    void setUp() {
        faculty = new Faculty();
        faculty.setId(703L);
        faculty.setName("Густеван");
        faculty.setColor("КишМиш");

        faculty1 = new Faculty();
        faculty1.setId(653L);
        faculty1.setName("Густеван1");
        faculty1.setColor("КишМиш1");

        //  facultyService.createFaculty(faculty);
        //  facultyService.createFaculty(faculty1);
        faculty = facultyRepository.save(faculty);
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
    void getFaculty() {
        ResponseEntity<Faculty> response = restTemplate.getForEntity("/faculty/{facultyId}", Faculty.class, faculty.getId());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo(faculty.getName());
    }

    @Test
    void updateFaculty() {
        faculty.setName("Updated Name");

        HttpEntity<Faculty> requestEntity = new HttpEntity<>(faculty);
        ResponseEntity<Faculty> response = restTemplate.exchange("/faculty", HttpMethod.PUT, requestEntity, Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("Updated Name");
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void deleteFaculty() {
        ResponseEntity<Void> deleteResponse = restTemplate.exchange("/faculty/{facultyId}", HttpMethod.DELETE, null, Void.class, faculty.getId());
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<Faculty> getResponse = restTemplate.getForEntity("/faculty/{facultyId}", Faculty.class, faculty.getId());
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void getFacultyByColor() {

        // Выполняем GET запрос к контроллеру
        ResponseEntity<Faculty> response = restTemplate.getForEntity("/faculty/filter/{color}",
                Faculty.class, "КишМиш");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getColor()).isEqualTo("КишМиш");

    }

    @Test
    void getFacultiesByNameOrColor() {
        // По имени
        ResponseEntity<Collection<Faculty>> responseByName = restTemplate.getForEntity("/faculty/filter/nameOrColor?", Collection<faculty>, "Густеван");
        assertThat(responseByName.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseByName.getBody().size()).isEqualTo(1);

        // По цвету
        ResponseEntity<Collection<Faculty>> responseByColor = restTemplate.getForEntity("/faculty/filter/nameOrColor?color={color}", Collection.class, "Blue");
        assertThat(responseByColor.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseByColor.getBody().size()).isEqualTo(1);

        // По имени и цвету
        ResponseEntity<Collection<Faculty>> responseByNameAndColor = restTemplate.getForEntity("/faculty/filter/nameOrColor?", Collection.class, "Science", "Green");
        assertThat(responseByNameAndColor.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseByNameAndColor.getBody().size()).isEqualTo(1);
    }

    @Test
    void getFacultyStudents() {
        ResponseEntity<List<Student>> response = restTemplate.exchange("/faculty/find/{facultyId}",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Student>>() {
                }, faculty.getId());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}