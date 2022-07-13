package com.testTask.university.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testTask.university.dao.GroupRepository;
import com.testTask.university.dto.GroupDto;
import com.testTask.university.entity.Group;
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

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    GroupRepository repository;

    @Test
    public void shouldReturnAllGroups() throws Exception {
        Group group = new Group(1L, 12, new ArrayList<>(), new ArrayList<>(), new Schedule());
        List<Group> list = new ArrayList<>();
        list.add(group);
        when(repository.findAll()).thenReturn(list);
        mockMvc.perform(get("/api/group/all-groups"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnAllGroupsAfterCreateNew() throws Exception {
        GroupDto groupDto = new GroupDto(1L,  12);
        Group group = new Group(1L, 12, new ArrayList<>(), new ArrayList<>(), new Schedule());
        List<Group> list = new ArrayList<>();
        list.add(group);
        when(repository.save(group)).thenReturn(group);
        when(repository.findAll()).thenReturn(list);
        mockMvc.perform(post("/api/group/new-group")
                        .content(objectMapper.writeValueAsString(groupDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnAlreadyExistStatusWhenCreateNewIfExist() throws Exception {
        GroupDto groupDto = new GroupDto(1L,  12);
        when(repository.existsByNumber(anyInt())).thenReturn(true);
        mockMvc.perform(post("/api/group/new-group")
                        .content(objectMapper.writeValueAsString(groupDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("message").value("The group with number '" + groupDto.getNumber() + "' already exist."))
                .andExpect(jsonPath("status").value("409"))
                .andExpect(status().isConflict());
    }

    @Test
    public void shouldReturnEditedGroup() throws Exception {
        GroupDto groupDto = new GroupDto(1L,  12);
        Group group = new Group(1L, 45, new ArrayList<>(), new ArrayList<>(), new Schedule());
        when(repository.findByNumber(anyInt())).thenReturn(group);
        when(repository.save(group)).thenReturn(group);
        mockMvc.perform(put("/api/group/edit-group")
                        .content(objectMapper.writeValueAsString(groupDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("number").value(groupDto.getNumber()))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnStatusNotFoundAndMessageWhenUpdateIfNotExist() throws Exception {
        GroupDto groupDto = new GroupDto(1L,  12);
        mockMvc.perform(put("/api/group/edit-group")
                        .content(objectMapper.writeValueAsString(groupDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("message").value("The group with number '" + groupDto.getNumber() + "' not exist."))
                .andExpect(jsonPath("status").value("404"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnSuccessAfterRemoving() throws Exception {
        String response = "Group with ID: 0 has been successfully deleted.";
        given(repository.existsById(anyLong())).willReturn(true);
        mockMvc.perform(delete("/api/group/remove-group")
                        .param("groupId", String.valueOf(anyLong())))
                .andDo(print())
                .andExpect(content().string(response))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnStatusNotFoundAndMessageAfterRemovingIfNotExist() throws Exception {
        given(repository.existsById(anyLong())).willReturn(false);
        mockMvc.perform(delete("/api/group/remove-group")
                        .param("groupId", String.valueOf(anyLong())))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("message").value("The group with id '0' not exist."))
                .andExpect(jsonPath("status").value("404"))
                .andExpect(status().isNotFound());
    }
}
