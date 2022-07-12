package com.testTask.university.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testTask.university.dao.AudienceRepository;
import com.testTask.university.dto.AudienceDto;
import com.testTask.university.entity.Audience;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AudienceControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    AudienceRepository repository;

    @Test
    public void shouldReturnAllAudiences() throws Exception {
        Audience audience = new Audience(1L, 45, new ArrayList<>());
        List<Audience> list = new ArrayList<>();
        list.add(audience);
        when(repository.findAll()).thenReturn(list);
        mockMvc.perform(get("/api/audience/all-audiences"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnAllAudiencesAfterCreateNew() throws Exception {
        AudienceDto audienceDto = new AudienceDto(1L, 12);
        Audience audience = new Audience(1L, 45, new ArrayList<>());
        List<Audience> list = new ArrayList<>();
        list.add(audience);
        when(repository.save(audience)).thenReturn(audience);
        when(repository.findAll()).thenReturn(list);
        mockMvc.perform(post("/api/audience/new-audience")
                        .content(objectMapper.writeValueAsString(audienceDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnAlreadyExistWhenCreateNewIfExist() throws Exception {
        AudienceDto audienceDto = new AudienceDto(1L, 12);
        when(repository.existsByNumber(anyInt())).thenReturn(true);
        mockMvc.perform(post("/api/audience/new-audience")
                        .content(objectMapper.writeValueAsString(audienceDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("message").value("The audience with number '" + audienceDto.getNumber() + "' already exist."))
                .andExpect(jsonPath("status").value("409"))
                .andExpect(status().isConflict());
    }

    @Test
    public void shouldReturnEditedAudience() throws Exception {
        AudienceDto audienceDto = new AudienceDto(1L, 12);
        Audience audience = new Audience(1L, 45, new ArrayList<>());
        when(repository.findByNumber(anyInt())).thenReturn(audience);
        when(repository.save(audience)).thenReturn(audience);
        mockMvc.perform(put("/api/audience/edit-audience")
                        .content(objectMapper.writeValueAsString(audienceDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("number").value(audienceDto.getNumber()))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnStatusNotFoundAndMessageWhenUpdateIfNotExist() throws Exception {
        AudienceDto audienceDto = new AudienceDto(1L, 12);
        mockMvc.perform(put("/api/audience/edit-audience")
                        .content(objectMapper.writeValueAsString(audienceDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("message").value("The audience with number '12' not exist."))
                .andExpect(jsonPath("status").value("404"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnSuccessAfterRemoving() throws Exception {
        String response = "Audience with ID: 0 has been successfully deleted.";
        given(repository.existsById(anyLong())).willReturn(true);
        mockMvc.perform(delete("/api/audience/remove-audience")
                        .param("audienceId", String.valueOf(anyLong())))
                .andDo(print())
                .andExpect(content().string(response))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnErrorCodeNotFoundAndMessageAfterRemovingIfNotExist() throws Exception {
        given(repository.existsById(anyLong())).willReturn(false);
        mockMvc.perform(delete("/api/audience/remove-audience")
                        .param("audienceId", String.valueOf(anyLong())))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("message").value("The audience with id '0' not exist."))
                .andExpect(jsonPath("status").value("404"))
                .andExpect(status().isNotFound());
    }
}
