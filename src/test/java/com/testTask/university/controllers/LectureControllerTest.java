package com.testTask.university.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testTask.university.dao.AudienceRepository;
import com.testTask.university.dao.GroupRepository;
import com.testTask.university.dao.LectureRepository;
import com.testTask.university.dto.LectureDto;
import com.testTask.university.entity.Audience;
import com.testTask.university.entity.Group;
import com.testTask.university.entity.Lecture;
import com.testTask.university.entity.Schedule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LectureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    LectureRepository repository;

    @MockBean
    GroupRepository groupRepository;

    @MockBean
    AudienceRepository audienceRepository;

    @Test
    public void shouldReturnAllLectures() throws Exception {
        Audience audience = new Audience(1L, 24, new ArrayList<>());
        Group group = new Group(1L, 43, new ArrayList<>(), new ArrayList<>(), new Schedule());
        Lecture lecture = new Lecture(1L, "name", "2022-11-23", audience, group);
        List<Lecture> list = new ArrayList<>();
        list.add(lecture);
        when(repository.findAll()).thenReturn(list);
        mockMvc.perform(get("/api/lecture/all-lectures"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnAllLecturesAfterCreateNew() throws Exception {
        Audience audience = new Audience(1L, 24, new ArrayList<>());
        Group group = new Group(1L, 43, new ArrayList<>(), new ArrayList<>(), new Schedule());
        LectureDto lectureDto = new LectureDto(1L,  "name2", "2022-09-23", 12, 25);
        Lecture lecture = new Lecture(1L, "name", "2022-11-23", audience, group);
        List<Lecture> list = new ArrayList<>();
        list.add(lecture);
        when(groupRepository.findByNumber(anyInt())).thenReturn(group);
        when(audienceRepository.findByNumber(anyInt())).thenReturn(audience);
        when(repository.save(lecture)).thenReturn(lecture);
        when(repository.findAll()).thenReturn(list);
        mockMvc.perform(post("/api/lecture/new-lecture")
                        .content(objectMapper.writeValueAsString(lectureDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnAlreadyExistWhenCreateNewIfExist() throws Exception {
        LectureDto lectureDto = new LectureDto(1L,  "name2", "2022-09-23", 12, 25);
        Lecture lecture = new Lecture(1L, "name", "2022-11-23", new Audience(), new Group());
        when(repository.findByNameAndDate(anyString(), anyString())).thenReturn(lecture);
        mockMvc.perform(post("/api/lecture/new-lecture")
                        .content(objectMapper.writeValueAsString(lectureDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("message").value("Lecture with name and date '" + lectureDto.getLectureName() + " " + lectureDto.getLectureDate() + "' already exist."))
                .andExpect(jsonPath("status").value("409"))
                .andExpect(status().isConflict());
    }

    @Test
    public void shouldReturnEditedLecture() throws Exception {
        LectureDto lectureDto = new LectureDto(1L,  "name2", "2022-09-23", 12, 25);
        Audience audience = new Audience(1L, 24, new ArrayList<>());
        Group group = new Group(1L, 43, new ArrayList<>(), new ArrayList<>(), new Schedule());
        Lecture lecture = new Lecture(1L, "name", "2022-11-23", audience, group);
        when(groupRepository.findByNumber(anyInt())).thenReturn(group);
        when(audienceRepository.findByNumber(anyInt())).thenReturn(audience);
        when(repository.findById(anyLong())).thenReturn(Optional.of(lecture));
        when(repository.save(lecture)).thenReturn(lecture);
        mockMvc.perform(put("/api/lecture/edit-lecture")
                        .content(objectMapper.writeValueAsString(lectureDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("lectureName").value(lectureDto.getLectureName()))
                .andExpect(jsonPath("lectureDate").value(lectureDto.getLectureDate()))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnStatusNotFoundAndMessageWhenUpdateIfNotExist() throws Exception {
        LectureDto lectureDto = new LectureDto(1L,  "name", "2022-11-23", 12, 25);
        mockMvc.perform(put("/api/lecture/edit-lecture")
                        .content(objectMapper.writeValueAsString(lectureDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("message").value("Lecture with ID '" + lectureDto.getLectureId() + "' not exist."))
                .andExpect(jsonPath("status").value("404"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnSuccessAfterRemoving() throws Exception {
        String response = "Lecture with ID '0' has been successfully deleted.";
        given(repository.existsById(anyLong())).willReturn(true);
        mockMvc.perform(delete("/api/lecture/remove-lecture")
                        .param("lectureId", String.valueOf(anyLong())))
                .andDo(print())
                .andExpect(content().string(response))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnErrorCodeNotFoundAndMessageAfterRemovingIfNotExist() throws Exception {
        given(repository.existsById(anyLong())).willReturn(false);
        mockMvc.perform(delete("/api/lecture/remove-lecture")
                        .param("lectureId", String.valueOf(anyLong())))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("message").value("Lecture with ID '0' not exist."))
                .andExpect(jsonPath("status").value("404"))
                .andExpect(status().isNotFound());
    }
}
