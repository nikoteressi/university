package com.testTask.university.services;

import com.testTask.university.dao.ScheduleRepository;
import com.testTask.university.dao.StudentRepository;
import com.testTask.university.dto.LectureDto;
import com.testTask.university.dto.ScheduleDto;
import com.testTask.university.entity.Lecture;
import com.testTask.university.entity.Schedule;
import com.testTask.university.entity.Student;
import com.testTask.university.exceptions.AlreadyExistException;
import com.testTask.university.exceptions.NotExistException;
import com.testTask.university.service.ScheduleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.ignoreCase;

@SpringBootTest

public class ScheduleServiceTest {

    @Autowired
    private ScheduleService service;

    @MockBean
    private ScheduleRepository repository;
    @MockBean
    private StudentRepository studentRepository;

    @Test
    void shouldReturnListWithSchedules() {
        List<Schedule> schedules = new ArrayList<>();
        schedules.add(new Schedule(1L, "schedule1", new ArrayList<>()));
        schedules.add(new Schedule(2L, "schedule2"));
        schedules.add(new Schedule(3L, "schedule3"));
        schedules.add(new Schedule(4L, "schedule4"));

        when(repository.findAll()).thenReturn(schedules);
        List<ScheduleDto> schedulesFromDb = service.getAllSchedules();
        verify(repository, times(1)).findAll();
        verifyNoMoreInteractions(repository);
        assertEquals(4, schedulesFromDb.size());
        assertEquals("schedule4", schedulesFromDb.get(3).getName());
    }

    @Test
    void shouldReturnEmptyListWithSchedulesIfThereIsNoSchedules() {
        List<Schedule> schedules = new ArrayList<>();
        when(repository.findAll()).thenReturn(schedules);
        List<ScheduleDto> lecturesFromDb = service.getAllSchedules();
        verify(repository, times(1)).findAll();
        verifyNoMoreInteractions(repository);
        assertTrue(lecturesFromDb.isEmpty());
    }

    @Test
    void shouldReturnScheduleIfStudentExist() throws NotExistException {
        List<Lecture> lectures = new ArrayList<>();
        lectures.add(new Lecture(1L, "lecture1", new Date()));
        lectures.add(new Lecture(2L, "lecture2", new Date()));
        Schedule schedule = new Schedule(1L, "schedule1", lectures);
        Student student = new Student(1L, "Igor", "Zagorka", schedule);
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student));
        ScheduleDto scheduleFromDb = service.getStudentSchedule(anyLong());
        verify(studentRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(studentRepository);
        assertNotNull(scheduleFromDb);
        assertEquals("schedule1", scheduleFromDb.getName());
        assertEquals("lecture1", scheduleFromDb.getLectures().get(0).getLectureName());
    }

    @Test
    void shouldReturnAllSchedulesAfterCreateNew() throws AlreadyExistException {
        List<LectureDto> lectures = new ArrayList<>();
        lectures.add(new LectureDto(1L, "lecture1", new Date()));
        lectures.add(new LectureDto(2L, "lecture2", new Date()));
        Schedule schedule = new Schedule(1L, "schedule1", new ArrayList<>());
        ScheduleDto scheduleDto = new ScheduleDto(1L, "schedule1", lectures);
        List<Schedule> list = new ArrayList<>();
        list.add(schedule);
        when(repository.findAll()).thenReturn(list);
        List<ScheduleDto> schedules = service.createNewSchedule(scheduleDto);
        verify(repository, times(1)).save(new Schedule());
        verify(repository, times(1)).findAll();
        verifyNoMoreInteractions(repository);
        assertFalse(schedules.isEmpty());
    }

    @Test
    void shouldThrownAlreadyExistExceptionIfExistWhenCreateNewSchedule() throws AlreadyExistException {
        ExampleMatcher nameMatcher = ExampleMatcher.matching()
                .withIgnorePaths("id")
                .withMatcher("name", ignoreCase());
        Example<Schedule> example = Example.of(new Schedule(), nameMatcher);
        when(repository.exists(example)).thenReturn(true);

        try {
            service.createNewSchedule(new ScheduleDto());
            fail();
        } catch (AlreadyExistException e) {
            assertNotEquals("", e.getMessage());
        }
        verify(repository, times(1)).exists(example);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldReturnEditedScheduleAfterEdit() throws NotExistException {
        Schedule editedSchedule = new Schedule(1L, "schedule1", new ArrayList<>());
        ScheduleDto scheduleForEdit = new ScheduleDto(1L, "schedule2", new ArrayList<>());
        when(repository.findById(anyLong())).thenReturn(Optional.of(editedSchedule));
        when(repository.save(editedSchedule)).thenReturn(editedSchedule);
        ScheduleDto scheduleFromDb = service.editSchedule(scheduleForEdit);
        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).save(editedSchedule);
        verifyNoMoreInteractions(repository);
        assertNotNull(scheduleFromDb);
        assertEquals("schedule2", scheduleFromDb.getName());
    }

    @Test
    void shouldThrownAnNotExistExceptionIfThereIsNoLecture() {
        try {
            service.editSchedule(new ScheduleDto());
            fail();
        } catch (NotExistException e) {
            assertNotEquals("", e.getMessage());
        }
        verify(repository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldReturnStringAfterRemove() throws NotExistException {
        when(repository.existsById(anyLong())).thenReturn(true);
        String response = service.removeSchedule(anyLong());
        verify(repository, times(1)).existsById(anyLong());
        verify(repository, times(1)).deleteById(anyLong());
        verifyNoMoreInteractions(repository);
        assertNotEquals("", response);
    }

    @Test
    void shouldReturnNotExistExceptionWhenRemoveIfNotExist() {
        when(repository.existsById(anyLong())).thenReturn(false);
        try {
            service.removeSchedule(anyLong());
        } catch (NotExistException e) {
            assertNotEquals("", e.getMessage());
        }
        verify(repository, times(1)).existsById(anyLong());
        verifyNoMoreInteractions(repository);
    }
}
