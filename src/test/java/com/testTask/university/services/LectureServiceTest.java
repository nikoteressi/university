package com.testTask.university.services;

import com.testTask.university.dao.LectureRepository;
import com.testTask.university.dto.LectureDto;
import com.testTask.university.entity.Lecture;
import com.testTask.university.exceptions.AlreadyExistException;
import com.testTask.university.exceptions.NotExistException;
import com.testTask.university.service.LectureService;
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
import static org.mockito.Mockito.*;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.ignoreCase;

@SpringBootTest
public class LectureServiceTest {
    @Autowired
    private LectureService lectureService;
    @MockBean
    private LectureRepository lectureRepository;

    @Test
    void shouldReturnListWithLectures() {
        List<Lecture> lectures = new ArrayList<>();
//        lectures.add(new Lecture(1L, "lecture1", "2022-07-12"));
//        lectures.add(new Lecture(2L, "lecture2", "2022-07-12"));
//        lectures.add(new Lecture(3L, "lecture3", "2022-07-12"));
        when(lectureRepository.findAll()).thenReturn(lectures);
        List<LectureDto> lecturesFromDb = lectureService.getAllLectures();
        verify(lectureRepository, times(1)).findAll();
        verifyNoMoreInteractions(lectureRepository);
        assertEquals(3, lecturesFromDb.size());
        assertEquals("lecture1", lecturesFromDb.get(0).getLectureName());
    }

    @Test
    void shouldReturnEmptyListWithLecturesIfThereIsNoLectures() {
        List<Lecture> lectures = new ArrayList<>();
        when(lectureRepository.findAll()).thenReturn(lectures);
        List<LectureDto> lecturesFromDb = lectureService.getAllLectures();
        verify(lectureRepository, times(1)).findAll();
        verifyNoMoreInteractions(lectureRepository);
        assertTrue(lecturesFromDb.isEmpty());
    }

    @Test
    void shouldReturnLectureIfExist() throws NotExistException {
        Lecture lecture = new Lecture(1L, "lecture1", new Date());
        when(lectureRepository.findById(anyLong())).thenReturn(Optional.of(lecture));
        LectureDto lectureFromDb = lectureService.getLectureById(anyLong());
        verify(lectureRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(lectureRepository);
        assertEquals("lecture1", lectureFromDb.getLectureName());
        assertNotNull(lectureFromDb);
    }

    @Test
    void shouldReturnAllLecturesAfterCreateNew() throws AlreadyExistException {
        List<Lecture> list = new ArrayList<>();
        list.add(new Lecture());
        when(lectureRepository.findAll()).thenReturn(list);
        List<LectureDto> lecturesFromDb = lectureService.createNewLecture(new LectureDto());
        verify(lectureRepository, times(1)).save(new Lecture());
        verify(lectureRepository, times(1)).findAll();
        verifyNoMoreInteractions(lectureRepository);
        assertFalse(lecturesFromDb.isEmpty());
    }

    @Test
    void shouldThrownAlreadyExistExceptionIfExistWhenCreateNew() throws AlreadyExistException {
        ExampleMatcher nameMatcher = ExampleMatcher.matching()
                .withIgnorePaths("id")
                .withMatcher("lectureName", ignoreCase());
        Example<Lecture> example = Example.of(new Lecture(), nameMatcher);
        when(lectureRepository.exists(example)).thenReturn(true);

        try {
            lectureService.createNewLecture(new LectureDto());
            fail();
        } catch (AlreadyExistException e) {
            assertNotEquals("", e.getMessage());
        }
        verify(lectureRepository, times(1)).exists(example);
        verifyNoMoreInteractions(lectureRepository);
    }

    @Test
    void shouldReturnEditedLectureAfterEdit() throws NotExistException {
        Lecture editedLecture = new Lecture(1L, "lecture1", new Date());
        LectureDto lectureForEdit = new LectureDto(1L, "lecture2", new Date());
        when(lectureRepository.findById(anyLong())).thenReturn(Optional.of(editedLecture));
        when(lectureRepository.save(editedLecture)).thenReturn(editedLecture);
        LectureDto lectureFromDb = lectureService.editLecture(lectureForEdit);
        verify(lectureRepository, times(1)).findById(anyLong());
        verify(lectureRepository, times(1)).save(editedLecture);
        verifyNoMoreInteractions(lectureRepository);
        assertNotNull(lectureFromDb);
        assertEquals("lecture2", lectureFromDb.getLectureName());
    }

    @Test
    void shouldThrownAnNotExistExceptionWhenEditIfThereIsNoLecture() {
        try {
            lectureService.editLecture(new LectureDto());
            fail();
        } catch (NotExistException e) {
            assertNotEquals("", e.getMessage());
        }
        verify(lectureRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(lectureRepository);
    }

    @Test
    void shouldReturnStringAfterRemove() throws NotExistException {
        when(lectureRepository.existsById(anyLong())).thenReturn(true);
        String response = lectureService.removeLecture(anyLong());
        verify(lectureRepository, times(1)).existsById(anyLong());
        verify(lectureRepository, times(1)).deleteById(anyLong());
        verifyNoMoreInteractions(lectureRepository);
        assertNotEquals("", response);
    }

    @Test
    void shouldReturnNotExistExceptionWhenRemoveIfNotExist() {
        when(lectureRepository.existsById(anyLong())).thenReturn(false);
        try {
            lectureService.removeLecture(anyLong());
        } catch (NotExistException e) {
            assertNotEquals("", e.getMessage());
        }
        verify(lectureRepository, times(1)).existsById(anyLong());
        verifyNoMoreInteractions(lectureRepository);
    }

}
