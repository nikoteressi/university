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
        verify(repository, times(1)).findAll();
        verify(lectureRepository, times(schedules.size())).findAllByGroup_NumberAndAndDate(anyInt(), anyString());
        verifyNoMoreInteractions(repository);
        verifyNoMoreInteractions(lectureRepository);
    }

    @Test
    void shouldReturnEmptyListWithSchedulesIfThereIsNoSchedules() {
        List<Schedule> schedules = new ArrayList<>();
        when(repository.findAll()).thenReturn(schedules);
        List<ScheduleDto> lecturesFromDb = service.getAllSchedules();
        assertTrue(lecturesFromDb.isEmpty());
        verify(repository, times(1)).findAll();
        verify(lectureRepository, times(schedules.size())).findAllByGroup_NumberAndAndDate(anyInt(), anyString());
        verifyNoMoreInteractions(repository);
        verifyNoMoreInteractions(lectureRepository);
    }

    @Test
    void shouldReturnScheduleIfStudentExist() {
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
        verify(repository, times(1)).findByGroup_IdAndDate(anyLong(), anyString());
        verify(lectureRepository, times(1)).findAllByGroup_NumberAndAndDate(anyInt(), anyString());
        verifyNoMoreInteractions(lectureRepository);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldReturnNotExistExceptionIfStudentNotExist() {
        NotExistException exception = assertThrows(NotExistException.class,
                () -> service.getStudentSchedule(1L, "2022-07-15"));
        assertTrue(exception.getMessage().contains("Student"));
        verify(studentRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(studentRepository);
        verifyNoInteractions(repository);
    }

    @Test
    void shouldReturnAllSchedulesAfterCreateNew() {
        Schedule schedule = new Schedule(1L, "2022-07-15", new Group());
        ScheduleDto scheduleDto = new ScheduleDto(1L, "2022-07-15", 1, new ArrayList<>());
        List<Schedule> list = new ArrayList<>();
        list.add(schedule);
        when(repository.findAll()).thenReturn(list);
        when(groupRepository.findByNumber(anyInt())).thenReturn(new Group());
        List<ScheduleDto> schedules = service.createNewSchedule(scheduleDto);
        assertFalse(schedules.isEmpty());
        assertEquals("2022-07-15", schedules.get(0).getDate());
        verify(repository, times(1)).findAll();
        verify(repository, times(1)).save(schedule);
        verify(repository, times(1)).findByGroup_NumberAndDate(anyInt(), anyString());
        verify(lectureRepository, times(1)).findAllByGroup_NumberAndAndDate(anyInt(), anyString());
        verifyNoMoreInteractions(lectureRepository);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldThrownAlreadyExistExceptionIfExistWhenCreateNewSchedule() {
        when(repository.findByGroup_NumberAndDate(anyInt(), anyString())).thenReturn(new Schedule());
        AlreadyExistException exception = assertThrows(AlreadyExistException.class,
                () -> service.createNewSchedule((new ScheduleDto(1L, "2022-07-15", 1, new ArrayList<>()))));
        assertTrue(exception.getMessage().contains("schedule"));
        verify(repository, times(1)).findByGroup_NumberAndDate(anyInt(), anyString());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldThrownWrongInputDataExceptionWhenCreateNewScheduleIfWrongDate() {
        WrongInputDataException exception = assertThrows(WrongInputDataException.class,
                () -> service.createNewSchedule(new ScheduleDto(1L, "2022-07-155", 1, new ArrayList<>())));
        assertTrue(exception.getMessage().contains("date"));
        verifyNoInteractions(repository);
    }

    @Test
    void shouldThrownWrongInputDataExceptionWhenCreateNewScheduleIfWrongGroupNumber() {
        WrongInputDataException exception = assertThrows(WrongInputDataException.class,
                () -> service.createNewSchedule(new ScheduleDto(1L, "2022-07-15", -1, new ArrayList<>())));
        assertTrue(exception.getMessage().contains("group"));
        verifyNoInteractions(repository);
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
        verify(groupRepository, times(1)).findByNumber(anyInt());
        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).save(editedSchedule);
        verifyNoMoreInteractions(repository);
        verifyNoMoreInteractions(groupRepository);
    }

    @Test
    void shouldThrownAnNotExistExceptionIfThereIsNoScheduleWhenEdit() {
        NotExistException exception = assertThrows(NotExistException.class,
                () -> service.editSchedule(new ScheduleDto(1L, "2022-07-25", 1, new ArrayList<>())));
        assertTrue(exception.getMessage().contains("Schedule"));
        verify(repository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldReturnStringAfterRemove() throws NotExistException {
        when(repository.existsById(anyLong())).thenReturn(true);
        String response = service.removeSchedule(anyLong());
        assertTrue(response.contains("success"));
        verify(repository, times(1)).existsById(anyLong());
        verify(repository, times(1)).deleteById(anyLong());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldReturnNotExistExceptionWhenRemoveIfNotExist() {
        when(repository.existsById(anyLong())).thenReturn(false);
        NotExistException exception = assertThrows(NotExistException.class,
                () -> service.removeSchedule(anyLong()));
        assertTrue(exception.getMessage().contains("Schedule"));
        verify(repository, times(1)).existsById(anyLong());
        verifyNoMoreInteractions(repository);
    }
}
