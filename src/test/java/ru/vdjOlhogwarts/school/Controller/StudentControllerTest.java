package ru.vdjOlhogwarts.school.Controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import ru.vdjOlhogwarts.school.controller.StudentController;
import ru.vdjOlhogwarts.school.model.Faculty;
import ru.vdjOlhogwarts.school.model.Student;
import ru.vdjOlhogwarts.school.service.StudentServiceProd;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentServiceProd studentService;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate restTemplate;

    private Student student;
    private Student student2;
    private Faculty faculty;


    @BeforeEach
    public void setUp() {
        faculty = new Faculty();
        faculty.setId(5L);
        faculty.setName("ghhhkj");
        faculty.setColor("ggrgjj");

        student = new Student();
        student.setId(402);
        student.setName("Germi");
        student.setAge(25);
        student.setFaculty(faculty);

        student2 = new Student();
        student2.setId(403);
        student2.setName("Porsh");
        student2.setAge(0);
    }

    @Test
    void createStudent() throws Exception {
        when(studentService.createStudent(any(Student.class))).thenReturn(student);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Student> requestEntity = new HttpEntity<>(student, headers);
        ResponseEntity<Student> response = restTemplate.exchange("/student", HttpMethod.POST, requestEntity, Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Germi");
        assertThat(response.getBody().getAge()).isEqualTo(25);
    }

    @Test
    void getStudent() {
        when(studentService.getStudent(anyLong())).thenReturn(student);
        ResponseEntity<Student> response = restTemplate.getForEntity("/student/402", Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(402L);
        assertThat(response.getBody().getName()).isEqualTo("Germi");
        assertThat(response.getBody().getAge()).isEqualTo(25);
    }

    @Test
    void getStudentReturnsNotFound() {
        when(studentService.getStudent(anyLong())).thenReturn(null);
        ResponseEntity<Student> response = restTemplate.getForEntity("/students/999", Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void updateStudent() throws Exception {
        when(studentService.updateStudent(any(Student.class))).thenReturn(student);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Student> requestEntity = new HttpEntity<>(student, httpHeaders);
        ResponseEntity<Student> response = restTemplate.exchange("/student/1", HttpMethod.PUT, requestEntity, Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Germi");
        assertThat(response.getBody().getAge()).isEqualTo(25);
    }

    @Test
    void updateStudentNotFound() {
        when(studentService.updateStudent(any(Student.class))).thenReturn(null);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Student> requestEntity = new HttpEntity<>(student, headers);
        ResponseEntity<Student> response = restTemplate.exchange("/student/1", HttpMethod.PUT, requestEntity, Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void deleteStudent() {
        Long studentId = 402L;
        doNothing().when(studentService).deleteStudent(studentId);
        ResponseEntity<Void> response = restTemplate.exchange("/student/" + studentId, HttpMethod.DELETE, null, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(studentService).deleteStudent(studentId);
    }


    @Test
    void getStudentsByAge() {
        when(studentService.findStudentsByAge(anyInt())).thenReturn(Arrays.asList(student, student2));
        ResponseEntity<List> response = restTemplate.getForEntity("/student/filter/25", List.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(2);
        ObjectMapper objectMapper = new ObjectMapper();
        List<Student> studentsFromResponse = objectMapper.convertValue(response.getBody(), new TypeReference<List<Student>>() {
        });
        assertThat(studentsFromResponse).containsExactlyInAnyOrder(student, student2);
    }


    @Test
    void getStudentsByAgeBetween() {

        when(studentService.findStudentsByAgeBetween(18, 30)).thenReturn(Arrays.asList(student, student2));
        ResponseEntity<List> response = restTemplate.getForEntity("/student/filter/18/30", List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2);

        ObjectMapper objectMapper = new ObjectMapper();
        List<Student> studentsFromResponse = objectMapper.convertValue(response.getBody(), new TypeReference<List<Student>>() {
        });
        assertThat(studentsFromResponse).containsExactlyInAnyOrder(student, student2);
    }

    @Test
    void getFacultyOfStudent() {
        when(studentService.getStudent(anyLong())).thenReturn(student);

        ResponseEntity<String> response = restTemplate.getForEntity("/student/find/402", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("ghhhkj");
    }
}