package com.testTask.university.services;

import com.testTask.university.dao.StudentRepository;
import com.testTask.university.dto.ScheduleDto;
import com.testTask.university.dto.StudentDto;
import com.testTask.university.entity.Schedule;
import com.testTask.university.entity.Student;
import com.testTask.university.exceptions.AlreadyExistException;
import com.testTask.university.exceptions.NotExistException;
import com.testTask.university.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.ignoreCase;

@SpringBootTest
public class StudentServiceTest {

    @Autowired
    private StudentService service;
    @MockBean
    private StudentRepository repository;

    @Test
    void shouldReturnListWithStudents() {
        List<Student> students = new ArrayList<>();
        students.add(new Student(1L, "Vasya", "Petrov", new Schedule()));
        when(repository.findAll()).thenReturn(students);
        List<StudentDto> studentsFromDb = service.getAllStudents();
        verify(repository, times(1)).findAll();
        verifyNoMoreInteractions(repository);
        assertEquals(1, studentsFromDb.size());
        assertEquals("Vasya", studentsFromDb.get(0).getFirstName());
    }

    @Test
    void shouldReturnEmptyListWithStudentsIfThereIsNoStudents() {
        List<Student> students = new ArrayList<>();
        when(repository.findAll()).thenReturn(students);
        List<StudentDto> studentsFromDb = service.getAllStudents();
        verify(repository, times(1)).findAll();
        verifyNoMoreInteractions(repository);
        assertTrue(studentsFromDb.isEmpty());
    }

    @Test
    void shouldReturnAllStudentsAfterCreateNew() throws AlreadyExistException {
        List<Student> list = new ArrayList<>();
        list.add(new Student(1L, "Vasya", "Petrov", new Schedule()));
        when(repository.findAll()).thenReturn(list);
        List<StudentDto> studentsFromDb = service.createNewStudent(new StudentDto());
        verify(repository, times(1)).save(new Student());
        verify(repository, times(1)).findAll();
        verifyNoMoreInteractions(repository);
        assertFalse(studentsFromDb.isEmpty());
        assertEquals("Vasya", studentsFromDb.get(0).getFirstName());
    }

    @Test
    void shouldThrownAlreadyExistExceptionIfExistWhenCreateNewStudent() throws AlreadyExistException {
        ExampleMatcher nameMatcher = ExampleMatcher.matching()
                .withIgnorePaths("id")
                .withMatcher("firstName", ignoreCase())
                .withMatcher("lastName", ignoreCase());
        Example<Student> example = Example.of(new Student(), nameMatcher);
        when(repository.exists(example)).thenReturn(true);

        try {
            service.createNewStudent(new StudentDto());
            fail();
        } catch (AlreadyExistException e) {
            assertNotEquals("", e.getMessage());
        }
        verify(repository, times(1)).exists(example);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldReturnEditedStudentAfterEdit() throws NotExistException {
        Student editedStudent = new Student(1L, "Vasya", "Petrov", new Schedule());
        StudentDto studentForEdit = new StudentDto(1L, "Vasilii", "Petrov", 1L);
        when(repository.findById(anyLong())).thenReturn(Optional.of(editedStudent));
        when(repository.save(editedStudent)).thenReturn(editedStudent);
        StudentDto studentFromDb = service.editStudent(studentForEdit);
        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).save(editedStudent);
        verifyNoMoreInteractions(repository);
        assertNotNull(studentFromDb);
        assertEquals("Vasilii", studentFromDb.getFirstName());
    }

    @Test
    void shouldThrownAnNotExistExceptionWhenEditIfThereIsNoStudent() {
        try {
            service.editStudent(new StudentDto());
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
        String response = service.removeStudent(anyLong());
        verify(repository, times(1)).existsById(anyLong());
        verify(repository, times(1)).deleteById(anyLong());
        verifyNoMoreInteractions(repository);
        assertNotEquals("", response);
    }

    @Test
    void shouldReturnNotExistExceptionWhenRemoveIfNotExist() {
        when(repository.existsById(anyLong())).thenReturn(false);
        try {
            service.removeStudent(anyLong());
        } catch (NotExistException e) {
            assertNotEquals("", e.getMessage());
        }
        verify(repository, times(1)).existsById(anyLong());
        verifyNoMoreInteractions(repository);
    }
}