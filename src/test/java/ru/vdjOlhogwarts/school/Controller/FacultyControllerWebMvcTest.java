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
import ru.vdjOlhogwarts.school.controller.FacultyController;
import ru.vdjOlhogwarts.school.model.Faculty;
import ru.vdjOlhogwarts.school.model.Student;
import ru.vdjOlhogwarts.school.service.FacultyService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FacultyController.class)
public class FacultyControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyService facultyService;

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    private Student student;
    private Student student1;
    private Faculty faculty;
    private Faculty faculty1;
    private Faculty faculty2;
    private List<Student> testStudents;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

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
        student.setFaculty(faculty);

        student1 = new Student();
        student1.setId(2L);
        student1.setName("FE");
        student1.setAge(27);
        student1.setFaculty(faculty);
    }

    @Test
    void createFaculty() throws Exception {
        given(facultyService.createFaculty(any(Faculty.class))).willReturn(faculty);

        mockMvc.perform(post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Tj"))
                .andExpect(jsonPath("$.color").value("Gr"));
    }

    @Test
    void getFaculty() throws Exception {
        when(facultyService.getFaculty(anyLong())).thenReturn(faculty);

        mockMvc.perform(get("/faculty/{facultyId}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Tj"))
                .andExpect(jsonPath("$.color").value("Gr"));
    }

    @Test
    void getFacultyNotFound() throws Exception {
        when(facultyService.getFaculty(anyLong())).thenReturn(null);

        mockMvc.perform(get("/faculty/{facultyId}", 5L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateFaculty() throws Exception {
        when(facultyService.updateFaculty(any(Long.class), any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders.put("/faculty", faculty.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Tj"))
                .andExpect(jsonPath("$.color").value("Gr"));
    }

    @Test
    void updateFaculty_NotFound() throws Exception {
        when(facultyService.updateFaculty(any(Long.class), any(Faculty.class))).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.put("/faculty", 5L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteFaculty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/faculty/{facultyId}", faculty2.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void getFacultyByColor() throws Exception {
        when(facultyService.findByColor(anyString())).thenReturn(faculty);

        mockMvc.perform(get("/faculty/filter/{color}", "Gr")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Tj"))
                .andExpect(jsonPath("$.color").value("Gr"));
    }

    @Test
    void getFacultyByColor_NotFound() throws Exception {
        when(facultyService.findByColor(anyString())).thenReturn(null);

        mockMvc.perform(get("/faculty/filter/{color}", "Color")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getFacultiesByNameOrColorN() throws Exception {
        List<Faculty> faculties = new ArrayList<>();
        faculties.add(faculty);

        when(facultyService.findByNameOrColor("Tj", null)).thenReturn(faculties);
        mockMvc.perform(get("/faculty/filter/nameOrColor")
                        .param("name", "Tj")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Tj"))
                .andExpect(jsonPath("$[0].color").value("Gr"));
    }

    @Test
    void getFacultiesByNameNuOrColor() throws Exception {
        List<Faculty> faculties = new ArrayList<>();
        faculties.add(faculty1);

        when(facultyService.findByNameOrColor(null, "Yl")).thenReturn(faculties);
        mockMvc.perform(get("/faculty/filter/nameOrColor")
                        .param("color", "Yl")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2L))
                .andExpect(jsonPath("$[0].name").value("Sd"))
                .andExpect(jsonPath("$[0].color").value("Yl"));
    }

    @Test
    void getFacultiesByNameAndColor() throws Exception {
        List<Faculty> faculties = new ArrayList<>();
        faculties.add(faculty);
        faculties.add(faculty1);

        when(facultyService.findByNameOrColor("Tj", "Gr")).thenReturn(faculties);

        // Выполнение GET-запроса
        mockMvc.perform(get("/faculty/filter/nameOrColor")
                        .param("name", "Tj")
                        .param("color", "Gr")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Tj"))
                .andExpect(jsonPath("$[0].color").value("Gr"));
    }

    @Test
    void getFacultiesByNoParams_BadRequest() throws Exception {
        mockMvc.perform(get("/faculty/filter/nameOrColor")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getFacultyStudents() throws Exception {
        List<Student> students = new ArrayList<>();
        students.add(student);
        students.add(student1);

        when(facultyService.getStudentsByFacultyId(1L)).thenReturn(students);

        mockMvc.perform(get("/faculty/find/{facultyId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Ds"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("FE"));
    }

    @Test
    void getFacultyStudents_NotFound() throws Exception {
        when(facultyService.getStudentsByFacultyId(anyLong())).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/faculty/find/{facultyId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
