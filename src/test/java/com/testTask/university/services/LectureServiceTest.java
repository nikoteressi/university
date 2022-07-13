package com.testTask.university.services;

import com.testTask.university.dao.AudienceRepository;
import com.testTask.university.dao.GroupRepository;
import com.testTask.university.dao.LectureRepository;
import com.testTask.university.dto.LectureDto;
import com.testTask.university.entity.Audience;
import com.testTask.university.entity.Group;
import com.testTask.university.entity.Lecture;
import com.testTask.university.entity.Schedule;
import com.testTask.university.exceptions.AlreadyExistException;
import com.testTask.university.exceptions.NotExistException;
import com.testTask.university.exceptions.WrongInputDataException;
import com.testTask.university.service.LectureService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class LectureServiceTest {
    @Autowired
    private LectureService lectureService;
    @MockBean
    private LectureRepository lectureRepository;

    @MockBean
    private GroupRepository groupRepository;

    @MockBean
    private AudienceRepository audienceRepository;

    @Test
    void shouldReturnListWithAllLectures() {
        List<Lecture> lectures = new ArrayList<>();
        lectures.add(new Lecture(1L, "lecture1", "2022-07-12", new Audience(), new Group()));
        lectures.add(new Lecture(2L, "lecture2", "2022-07-12", new Audience(), new Group()));
        lectures.add(new Lecture(3L, "lecture3", "2022-07-12", new Audience(), new Group()));
        when(lectureRepository.findAll()).thenReturn(lectures);
        List<LectureDto> lecturesFromDb = lectureService.getAllLectures();
        assertEquals(3, lecturesFromDb.size());
        assertEquals("lecture1", lecturesFromDb.get(0).getLectureName());
        assertEquals("lecture3", lecturesFromDb.get(2).getLectureName());
        verify(lectureRepository, times(1)).findAll();
        verifyNoMoreInteractions(lectureRepository);
    }

    @Test
    void shouldReturnEmptyListWithLecturesIfThereIsNoLectures() {
        List<Lecture> lectures = new ArrayList<>();
        when(lectureRepository.findAll()).thenReturn(lectures);
        List<LectureDto> lecturesFromDb = lectureService.getAllLectures();
        assertTrue(lecturesFromDb.isEmpty());
        verify(lectureRepository, times(1)).findAll();
        verifyNoMoreInteractions(lectureRepository);
    }

    @Test
    void shouldReturnAlreadyExistExceptionWhenCreateNewIfExist() {
        LectureDto lectureDto = new LectureDto(0, "lecture1", "2022-07-12", 1, 1);
        when(lectureRepository.findByNameAndDate(anyString(), anyString()))
                .thenReturn(new Lecture(1L, "name", "2022-03-45", new Audience(), new Group()));
        AlreadyExistException exception = assertThrows(AlreadyExistException.class,
                () -> lectureService.createNewLecture(lectureDto));
        assertTrue(exception.getMessage().contains("lecture"));
        verify(lectureRepository, times(1)).findByNameAndDate(anyString(), anyString());
        verifyNoMoreInteractions(lectureRepository);
    }

    @Test
    void shouldReturnWrongInputDataExceptionWhenCreateNewIfDateIsEmpty() {
        LectureDto lectureDto = new LectureDto(0, "name", "", 1, 1);
        WrongInputDataException exception = assertThrows(WrongInputDataException.class,
                () -> lectureService.createNewLecture(lectureDto));
        assertTrue(exception.getMessage().contains("date"));
        verifyNoInteractions(lectureRepository);
    }

    @Test
    void shouldReturnWrongInputDataExceptionWhenCreateNewIfDateIncorrectFormat() {
        LectureDto lectureDto = new LectureDto(0, "12-02-2022", "lecture1", 1, 1);
        WrongInputDataException exception = assertThrows(WrongInputDataException.class,
                () -> lectureService.createNewLecture(lectureDto));
        assertTrue(exception.getMessage().contains("lecture"));
        verifyNoInteractions(lectureRepository);
    }

    @Test
    void shouldReturnWrongInputDataExceptionWhenCreateNewIfNameIsEmpty() {
        LectureDto lectureDto = new LectureDto(0, "", "lecture1", 1, 1);
        WrongInputDataException exception = assertThrows(WrongInputDataException.class,
                () -> lectureService.createNewLecture(lectureDto));
        assertTrue(exception.getMessage().contains("lecture"));
        verifyNoInteractions(lectureRepository);
    }

    @Test
    void shouldReturnWrongInputDataExceptionWhenCreateNewIfAudienceNumberIncorrect() {
        LectureDto lectureDto = new LectureDto(0, "", "lecture1", 0, 1);
        WrongInputDataException exception = assertThrows(WrongInputDataException.class,
                () -> lectureService.createNewLecture(lectureDto));
        assertTrue(exception.getMessage().contains("lecture"));
        verifyNoInteractions(lectureRepository);
    }

    @Test
    void shouldReturnWrongInputDataExceptionWhenCreateNewIfGroupNumberIncorrect() {
        LectureDto lectureDto = new LectureDto(0, "", "lecture1", 1, 1000);
        WrongInputDataException exception = assertThrows(WrongInputDataException.class,
                () -> lectureService.createNewLecture(lectureDto));
        assertTrue(exception.getMessage().contains("lecture"));
        verifyNoInteractions(lectureRepository);
    }

    @Test
    void shouldReturnAllLecturesAfterCreateNew() {
        Audience audience = new Audience(1L, 1, new ArrayList<>());
        Group group = new Group(1L, 1, new ArrayList<>(), new ArrayList<>(), new Schedule());
        LectureDto lectureDto = new LectureDto(0, "lecture1", "2022-07-12", 1, 1);
        Lecture lecture = new Lecture(0, "lecture1", "2022-07-12", audience, group);
        List<Lecture> lectures = new ArrayList<>();
        lectures.add(lecture);
        when(audienceRepository.findByNumber(anyInt())).thenReturn(audience);
        when(groupRepository.findByNumber(anyInt())).thenReturn(group);
        when(lectureRepository.findAll()).thenReturn(lectures);
        List<LectureDto> lecturesFromDb = lectureService.createNewLecture(lectureDto);
        assertFalse(lecturesFromDb.isEmpty());
        assertEquals(1, lecturesFromDb.get(0).getAudienceNumber());
        verify(lectureRepository, times(1)).findByNameAndDate(anyString(), anyString());
        verify(groupRepository, times(1)).findByNumber(anyInt());
        verify(audienceRepository, times(1)).findByNumber(anyInt());
        verify(lectureRepository, times(1)).findByNameAndDate(anyString(), anyString());
        verify(lectureRepository, times(1)).save(lecture);
        verify(lectureRepository, times(1)).findAll();
        verifyNoMoreInteractions(lectureRepository);
    }

    @Test
    void shouldReturnEditedLectureAfterEdit() {
        Audience audience = new Audience(1L, 1, new ArrayList<>());
        Group group = new Group(1L, 1, new ArrayList<>(), new ArrayList<>(), new Schedule());
        LectureDto lectureDto = new LectureDto(1L, "lecture2", "2022-07-15", 1, 1);
        Lecture lecture = new Lecture(1L, "lecture1", "2022-07-12", audience, group);
        when(lectureRepository.findById(lectureDto.getLectureId())).thenReturn(Optional.of(lecture));
        when(lectureRepository.save(lecture)).thenReturn(lecture);
        when(groupRepository.findByNumber(anyInt())).thenReturn(group);
        when(audienceRepository.findByNumber(anyInt())).thenReturn(audience);
        LectureDto lectureFromDb = lectureService.editLecture(lectureDto);
        assertNotNull(lectureFromDb);
        assertEquals("lecture2", lectureFromDb.getLectureName());
        assertEquals("2022-07-15", lectureFromDb.getLectureDate());
        verify(groupRepository, times(1)).findByNumber(anyInt());
        verify(audienceRepository, times(1)).findByNumber(anyInt());
        verify(lectureRepository, times(1)).findById(anyLong());
        verify(lectureRepository, times(1)).save(lecture);
        verifyNoMoreInteractions(lectureRepository);
    }

    @Test
    void shouldThrownAnNotExistExceptionWhenEditIfThereIsNoLecture() {
        LectureDto lectureDto = new LectureDto(1L, "lecture1", "2022-07-15", 1, 1);
        NotExistException exception = assertThrows(NotExistException.class,
                () -> lectureService.editLecture(lectureDto));
        assertTrue(exception.getMessage().contains("Lecture"));
        verify(lectureRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(lectureRepository);
    }

    @Test
    void shouldReturnStringAfterRemove() throws NotExistException {
        when(lectureRepository.existsById(anyLong())).thenReturn(true);
        String response = lectureService.removeLecture(anyLong());
        assertTrue(response.contains("success"));
        verify(lectureRepository, times(1)).existsById(anyLong());
        verify(lectureRepository, times(1)).deleteById(anyLong());
        verifyNoMoreInteractions(lectureRepository);
    }

    @Test
    void shouldReturnNotExistExceptionWhenRemoveIfNotExist() {
        when(lectureRepository.existsById(anyLong())).thenReturn(false);
        NotExistException exception = assertThrows(NotExistException.class,
                () -> lectureService.removeLecture(anyLong()));
        assertTrue(exception.getMessage().contains("Lecture"));
        verify(lectureRepository, times(1)).existsById(anyLong());
        verifyNoMoreInteractions(lectureRepository);
    }
}
