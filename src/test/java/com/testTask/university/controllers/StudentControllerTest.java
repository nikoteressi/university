package com.testTask.university.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testTask.university.dao.GroupRepository;
import com.testTask.university.dao.StudentRepository;
import com.testTask.university.dto.StudentDto;
import com.testTask.university.entity.Group;
import com.testTask.university.entity.Schedule;
import com.testTask.university.entity.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    StudentRepository repository;

    @MockBean
    GroupRepository groupRepository;


    @Test
    public void shouldReturnListWithStudents() throws Exception {
        List<Student> list = new ArrayList<>();
        list.add(new Student(1L, "name1", "lastname", new Group()));
        list.add(new Student(1L, "name2", "lastname", new Group()));
        list.add(new Student(1L, "name3", "lastname", new Group()));
        given(repository.findAll()).willReturn(list);
        mockMvc.perform(get("/api/student/all-students"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnAllStudentsAfterCreateNew() throws Exception {
        Group group = new Group(1L, 12, new ArrayList<>(), new ArrayList<>(), new Schedule());
        Student student = new Student(1L, "name1", "lastname", group);
        List<Student> list = new ArrayList<>();
        list.add(student);
        StudentDto studentDto = new StudentDto(0, "name1", "lastname", 12);
        given(groupRepository.findByNumber(anyInt())).willReturn(group);
        given(repository.findAll()).willReturn(list);
        given(repository.save(student)).willReturn(student);
        mockMvc.perform(post("/api/student/new-student")
                        .content(objectMapper.writeValueAsString(studentDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnEditedStudentWhenUpdate() throws Exception {
        Group group = new Group(1L, 12, new ArrayList<>(), new ArrayList<>(), new Schedule());
        Student student = new Student(1L, "name1", "lastname", group);
        StudentDto studentDto = new StudentDto(1L, "name4", "lastname35", 12);
        given(groupRepository.findByNumber(anyInt())).willReturn(group);
        given(repository.findById(anyLong())).willReturn(Optional.of(student));
        given(repository.save(student)).willReturn(student);
        mockMvc.perform(put("/api/student/edit-student")
                        .content(objectMapper.writeValueAsString(studentDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("firstName").value(studentDto.getFirstName()))
                .andExpect(jsonPath("lastName").value(studentDto.getLastName()))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnStatusNotFoundAndMessageWhenUpdateIfNotExist() throws Exception {
        Group group = new Group(1L, 12, new ArrayList<>(), new ArrayList<>(), new Schedule());
        StudentDto studentDto = new StudentDto(1L, "name4", "lastname35", 12);
        given(groupRepository.findByNumber(anyInt())).willReturn(group);
        mockMvc.perform(put("/api/student/edit-student")
                        .content(objectMapper.writeValueAsString(studentDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("message").value("The Student with ID '1' not exist."))
                .andExpect(jsonPath("status").value("404"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnSuccessAfterRemoving() throws Exception {
        String response = "Student with ID '0' has been successfully deleted.";
        given(repository.existsById(anyLong())).willReturn(true);
        mockMvc.perform(delete("/api/student/remove-student")
                        .param("studentId", String.valueOf(anyLong())))
                .andDo(print())
                .andExpect(content().string(response))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnErrorCodeNotFoundAndMessageAfterRemovingIfNotExist() throws Exception {
        given(repository.existsById(anyLong())).willReturn(false);
        mockMvc.perform(delete("/api/student/remove-student")
                        .param("studentId", String.valueOf(anyLong())))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("message").value("The Student with ID '0' not exist."))
                .andExpect(jsonPath("status").value("404"))
                .andExpect(status().isNotFound());
    }
}
