package com.testTask.university.services;

import com.testTask.university.dao.GroupRepository;
import com.testTask.university.dao.LectureRepository;
import com.testTask.university.dao.ScheduleRepository;
import com.testTask.university.dao.StudentRepository;
import com.testTask.university.dto.ScheduleDto;
import com.testTask.university.entity.*;
import com.testTask.university.exceptions.AlreadyExistException;
import com.testTask.university.exceptions.NotExistException;
import com.testTask.university.exceptions.WrongInputDataException;
import com.testTask.university.service.ScheduleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest

public class ScheduleServiceTest {

    @Autowired
    private ScheduleService service;

    @MockBean
    private ScheduleRepository repository;
    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private LectureRepository lectureRepository;

    @MockBean
    private GroupRepository groupRepository;

    @Test
    void shouldReturnListWithSchedules() {
        List<Schedule> schedules = new ArrayList<>();
        schedules.add(new Schedule(1L, "2022-07-15", new Group()));
        schedules.add(new Schedule(2L, "2022-07-15", new Group()));
        schedules.add(new Schedule(3L, "2022-07-15", new Group()));
        schedules.add(new Schedule(4L, "2022-07-15", new Group()));
        when(repository.findAll()).thenReturn(schedules);
        List<ScheduleDto> schedulesFromDb = service.getAllSchedules();
        assertEquals(4, schedulesFromDb.size());
    }

    @Test
    void shouldReturnEmptyListWithSchedulesIfThereIsNoSchedules() {
        List<Schedule> schedules = new ArrayList<>();
        when(repository.findAll()).thenReturn(schedules);
        List<ScheduleDto> lecturesFromDb = service.getAllSchedules();
        assertTrue(lecturesFromDb.isEmpty());
    }

    @Test
    void shouldReturnScheduleIfStudentExist() throws Exception {
        List<Lecture> lectures = new ArrayList<>();
        lectures.add(new Lecture(1L, "lecture1", "2022-07-12", new Audience(), new Group()));
        lectures.add(new Lecture(2L, "lecture2", "2022-07-12", new Audience(), new Group()));
        Schedule schedule = new Schedule(1L, "2022-07-15", new Group());
        Group group = new Group(1L, 1, new ArrayList<>(), new ArrayList<>(), new Schedule());
        Student student = new Student(1L, "name", "lastname", group);
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student));
        when(repository.findByGroup_IdAndDate(anyLong(), anyString())).thenReturn(schedule);
        when(lectureRepository.findAllByGroup_NumberAndAndDate(anyInt(), anyString())).thenReturn(lectures);
        ScheduleDto scheduleFromDb = service.getStudentSchedule(anyLong(), "2022-07-15");
        assertNotNull(scheduleFromDb);
        assertEquals("lecture1", scheduleFromDb.getLectures().get(0).getLectureName());
    }

    @Test
    void shouldReturnNotExistExceptionIfStudentNotExist() {
        try {
            service.getStudentSchedule(1L, "2022-07-15");
        } catch (Exception e) {
            assertTrue(e instanceof NotExistException);
            assertTrue(e.getMessage().contains("Student"));
        }
    }

    @Test
    void shouldReturnAllSchedulesAfterCreateNew() throws Exception {
        Schedule schedule = new Schedule(1L, "2022-07-15", new Group());
        ScheduleDto scheduleDto = new ScheduleDto(1L, "2022-07-15", 1, new ArrayList<>());
        List<Schedule> list = new ArrayList<>();
        list.add(schedule);
        when(repository.findAll()).thenReturn(list);
        when(groupRepository.findByNumber(anyInt())).thenReturn(new Group());
        List<ScheduleDto> schedules = service.createNewSchedule(scheduleDto);
        assertFalse(schedules.isEmpty());
        assertEquals("2022-07-15", schedules.get(0).getDate());
    }

    @Test
    void shouldThrownAlreadyExistExceptionIfExistWhenCreateNewSchedule() {
        when(groupRepository.findByNumber(anyInt())).thenReturn(new Group());
        when(repository.findByGroup_NumberAndDate(anyInt(), anyString())).thenReturn(new Schedule());
        try {
            service.createNewSchedule(new ScheduleDto(1L, "2022-07-15", 1, new ArrayList<>()));
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof AlreadyExistException);
            assertNotEquals("", e.getMessage());
        }
    }

    @Test
    void shouldThrownWrongInputDataExceptionWhenCreateNewScheduleIfWrongDate() {
        try {
            service.createNewSchedule(new ScheduleDto(1L, "2022-07-155", 1, new ArrayList<>()));
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof WrongInputDataException);
            assertTrue(e.getMessage().contains("date"));
        }
    }

    @Test
    void shouldThrownWrongInputDataExceptionWhenCreateNewScheduleIfWrongGroupNumber() {
        try {
            service.createNewSchedule(new ScheduleDto(1L, "2022-07-15", -1, new ArrayList<>()));
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof WrongInputDataException);
            assertTrue(e.getMessage().contains("group"));
        }
    }

    @Test
    void shouldReturnEditedScheduleAfterEdit() throws NotExistException {
        when(groupRepository.findByNumber(anyInt())).thenReturn(new Group());
        Schedule editedSchedule = new Schedule(1L, "2022-07-15", new Group());
        ScheduleDto scheduleToEdit = new ScheduleDto(1L, "2022-07-25", 1, new ArrayList<>());
        when(repository.save(editedSchedule)).thenReturn(editedSchedule);
        when(repository.findById(anyLong())).thenReturn(Optional.of(editedSchedule));
        ScheduleDto scheduleFromDb = service.editSchedule(scheduleToEdit);
        assertNotNull(scheduleFromDb);
        assertEquals("2022-07-25", scheduleFromDb.getDate());
    }

    @Test
    void shouldThrownAnNotExistExceptionIfThereIsNoScheduleWhenEdit() {
        try {
            service.editSchedule(new ScheduleDto(1L, "2022-07-25", 1, new ArrayList<>()));
            fail();
        } catch (NotExistException e) {
            System.out.println(e.getMessage());
            assertTrue(e.getMessage().contains("Schedule"));
        }
    }

    @Test
    void shouldReturnStringAfterRemove() throws NotExistException {
        when(repository.existsById(anyLong())).thenReturn(true);
        String response = service.removeSchedule(anyLong());
        assertTrue(response.contains("success"));
    }

    @Test
    void shouldReturnNotExistExceptionWhenRemoveIfNotExist() {
        when(repository.existsById(anyLong())).thenReturn(false);
        try {
            service.removeSchedule(anyLong());
        } catch (NotExistException e) {
            assertTrue(e.getMessage().contains("Schedule"));
        }
    }
}
