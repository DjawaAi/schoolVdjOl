package ru.vdjOlhogwarts.school.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.vdjOlhogwarts.school.controller.StudentController;
import ru.vdjOlhogwarts.school.model.Faculty;
import ru.vdjOlhogwarts.school.model.Student;
import ru.vdjOlhogwarts.school.service.StudentService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasLength;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
class StudentControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Student student;
    private Faculty faculty;
    private List<Student> testStudents;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        student = new Student();
        student.setId(2L);
        student.setName("Germi");
        student.setAge(25);

        testStudents = new ArrayList<>();
        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("Hari");
        student1.setAge(20);

        testStudents.add(student1);
        testStudents.add(student);
    }

    @Test
    void createStudent() throws Exception {
        given(studentService.createStudent(any(Student.class))).willReturn(student);

        mockMvc.perform(post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Germi"))
                .andExpect(jsonPath("$.age").value(25));
    }

    @Test
    void getStudent() throws Exception {
        when(studentService.getStudent(anyLong())).thenReturn(student);

        mockMvc.perform(get("/student/{studentId}", 2L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.name").value("Germi"))
                .andExpect(jsonPath("$.age").value(25));
    }

    @Test
    void getStudentNotFound() throws Exception {
        when(studentService.getStudent(anyLong())).thenReturn(null);

        mockMvc.perform(get("/student/{studentId}", 5L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateStudent() throws Exception {
        when(studentService.updateStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders.put("/student/{id}", student.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.name").value("Germi"))
                .andExpect(jsonPath("$.age").value(25)); // Проверяем возраст
    }

    @Test
    void updateStudent_NotFound() throws Exception {
        when(studentService.updateStudent(any(Student.class))).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.put("/student/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteStudent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/student/{studentId}", student.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void getStudentsByAge() throws Exception {
        // Подготовка: имитация результата метода findStudentsByAge
        when(studentService.findStudentsByAge(anyInt())).thenReturn(testStudents);

        // Выполнение запроса
        mockMvc.perform(get("/student/filter/{age}", 25)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Ожидаем статус 200 OK
                .andExpect(jsonPath("$.length()").value(2)) // Проверяем, что получен список из 2 студентов
                .andExpect(jsonPath("$[0].name").value("Hari")) // Проверяем имя первого студента
                .andExpect(jsonPath("$[1].name").value("Germi")); // Проверяем имя второго студента
    }

    @Test
    void getStudentsByAgeBetween() throws Exception {
        // Подготовка: имитация результата метода findStudentsByAgeBetween
        when(studentService.findStudentsByAgeBetween(anyInt(), anyInt())).thenReturn(testStudents);

        // Выполнение запроса
        mockMvc.perform(get("/student/filter/{minAge}/{maxAge}", 20, 30)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Ожидаем статус 200 OK
                .andExpect(jsonPath("$.length()").value(2)); // Проверяем, что получен список из 2 студентов
    }

    @Test
    void getFacultyOfStudent() throws Exception {
        // Создаем студента с факультетом
        Student student = new Student();
        student.setId(1L);
        student.setName("John Doe");
        student.setAge(20);
        Faculty faculty = new Faculty();
        faculty.setName("Целителей");
        faculty.setColor("Green");
        faculty.setId(1L);
        student.setFaculty(faculty);

        // Подготовка: имитация результата метода getStudent
        when(studentService.getStudent(anyLong())).thenReturn(student);

        // Выполнение запроса
        mockMvc.perform(get("/student/find/{studentId}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Ожидаем статус 200 OK
                .andExpect(jsonPath("$").value(hasLength(9)))
                .andExpect(jsonPath("$").value("Целителей")); // Проверяем название факультета
    }
}