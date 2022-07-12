package com.testTask.university.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testTask.university.dao.GroupRepository;
import com.testTask.university.dao.ScheduleRepository;
import com.testTask.university.dao.StudentRepository;
import com.testTask.university.dto.ScheduleDto;
import com.testTask.university.entity.Group;
import com.testTask.university.entity.Schedule;
import com.testTask.university.entity.Student;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    ScheduleRepository repository;

    @MockBean
    GroupRepository groupRepository;

    @MockBean
    StudentRepository studentRepository;

    @Test
    public void shouldReturnListWithSchedules() throws Exception {
        List<Schedule> list = new ArrayList<>();
        list.add(new Schedule(1L, "2022-07-13",
                new Group(1L, 12, new ArrayList<>(), new ArrayList<>(), new Schedule())));
        list.add(new Schedule(2L, "2022-07-13",
                new Group(1L, 12, new ArrayList<>(), new ArrayList<>(), new Schedule())));
        list.add(new Schedule(3L, "2022-07-13",
                new Group(1L, 12, new ArrayList<>(), new ArrayList<>(), new Schedule())));
        given(repository.findAll()).willReturn(list);
        mockMvc.perform(get("/api//schedule/all-schedules"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnStudentScheduleByDate() throws Exception {
        Group group = new Group(1L, 12, new ArrayList<>(), new ArrayList<>(), new Schedule());
        Schedule schedule = new Schedule(1L, "2022-07-13", group);
        Student student = new Student(1L, "name", "lastname", group);
        given(groupRepository.findByNumber(anyInt())).willReturn(group);
        given(repository.findByGroup_IdAndDate(anyLong(), anyString())).willReturn(schedule);
        given(studentRepository.findById(anyLong())).willReturn(Optional.of(student));
        given(repository.save(schedule)).willReturn(schedule);
        mockMvc.perform(get("/api//schedule/student-schedule?studentId=1&scheduleDate=2022-07-13"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("date").value(schedule.getDate()))
                .andExpect(jsonPath("groupNumber").value(schedule.getGroup().getNumber()))
                .andExpect(jsonPath("scheduleId").value(schedule.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldCreateAndReturnStudentScheduleByDateIfNotExist() throws Exception {
        Group group = new Group(1L, 12, new ArrayList<>(), new ArrayList<>(), new Schedule());
        Schedule schedule = new Schedule(1L, "2022-07-13", group);
        Student student = new Student(1L, "name", "lastname", group);
        given(groupRepository.findByNumber(anyInt())).willReturn(group);
        given(repository.findByGroup_IdAndDate(anyLong(), anyString())).willReturn(null);
        given(studentRepository.findById(anyLong())).willReturn(Optional.of(student));
        given(repository.save(schedule)).willReturn(schedule);
        mockMvc.perform(get("/api//schedule/student-schedule?studentId=1&scheduleDate=2022-07-13"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("date").value(schedule.getDate()))
                .andExpect(jsonPath("groupNumber").value(schedule.getGroup().getNumber()))
                .andExpect(jsonPath("scheduleId").value(schedule.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnAllSchedulesAfterCreateNew() throws Exception {
        List<Schedule> list = new ArrayList<>();
        list.add(new Schedule(1L, "2022-07-13",
                new Group(1L, 12, new ArrayList<>(), new ArrayList<>(), new Schedule())));
        ScheduleDto schedule = new ScheduleDto(0, "2022-07-13", 12, new ArrayList<>());

        given(groupRepository.findByNumber(anyInt())).willReturn(new Group());
        given(repository.findAll()).willReturn(list);
        mockMvc.perform(post("/api//schedule/new-schedule")
                        .content(objectMapper.writeValueAsString(schedule))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnEditedScheduleAfterEdit() throws Exception {
        Schedule scheduleE = new Schedule(1L, "2022-07-13", new Group(1L, 12, new ArrayList<>(), new ArrayList<>(), new Schedule()));
        ScheduleDto schedule = new ScheduleDto(1L, "2022-07-24", 12, new ArrayList<>());
        Group group = new Group(1L, 12, new ArrayList<>(), new ArrayList<>(), new Schedule());

        given(groupRepository.findByNumber(anyInt())).willReturn(group);
        given(repository.findById(anyLong())).willReturn(Optional.of(scheduleE));
        given(repository.save(scheduleE)).willReturn(scheduleE);
        mockMvc.perform(put("/api//schedule/edit-schedule")
                        .content(objectMapper.writeValueAsString(schedule))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("date").value("2022-07-24"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnNotFoundStatusAndMessageAfterEditIsNoSchedule() throws Exception {
        Schedule scheduleE = new Schedule(1L, "2022-07-13", new Group(1L, 12, new ArrayList<>(), new ArrayList<>(), new Schedule()));
        ScheduleDto schedule = new ScheduleDto(1L, "2022-07-24", 12, new ArrayList<>());
        Group group = new Group(1L, 12, new ArrayList<>(), new ArrayList<>(), new Schedule());

        given(groupRepository.findByNumber(anyInt())).willReturn(group);
        given(repository.save(scheduleE)).willReturn(scheduleE);
        mockMvc.perform(put("/api//schedule/edit-schedule")
                        .content(objectMapper.writeValueAsString(schedule))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("message").value("Schedule with ID: 1 not found."))
                .andExpect(jsonPath("status").value("404"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnSuccessAfterRemoving() throws Exception {
        String response = "Schedule with ID: 0 has been successfully deleted.";
        given(repository.existsById(anyLong())).willReturn(true);
        mockMvc.perform(delete("/api//schedule/remove-schedule")
                        .param("scheduleId", String.valueOf(anyLong())))
                .andDo(print())
                .andExpect(content().string(response))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnErrorCodeNotFoundAndMessageAfterRemovingIfNotExist() throws Exception {
        given(repository.existsById(anyLong())).willReturn(false);
        mockMvc.perform(delete("/api//schedule/remove-schedule")
                        .param("scheduleId", String.valueOf(anyLong())))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("message").value("Schedule with ID: 0 not found."))
                .andExpect(jsonPath("status").value("404"))
                .andExpect(status().isNotFound());
    }


}
