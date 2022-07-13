package com.testTask.university.services;

import com.testTask.university.dao.GroupRepository;
import com.testTask.university.dao.StudentRepository;
import com.testTask.university.dto.StudentDto;
import com.testTask.university.entity.Group;
import com.testTask.university.entity.Schedule;
import com.testTask.university.entity.Student;
import com.testTask.university.exceptions.NotExistException;
import com.testTask.university.exceptions.WrongInputDataException;
import com.testTask.university.service.StudentService;
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
public class StudentServiceTest {

    @Autowired
    private StudentService service;
    @MockBean
    private StudentRepository repository;

    @MockBean
    private GroupRepository groupRepository;

    @Test
    void shouldReturnListWithStudents() {
        List<Student> students = new ArrayList<>();
        students.add(new Student(1L, "name", "lastname", new Group()));
        when(repository.findAll()).thenReturn(students);
        List<StudentDto> studentsFromDb = service.getAllStudents();
        assertEquals(1, studentsFromDb.size());
        verify(repository, times(1)).findAll();
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldReturnEmptyListWithStudentsIfThereIsNoStudents() {
        List<Student> students = new ArrayList<>();
        when(repository.findAll()).thenReturn(students);
        List<StudentDto> studentsFromDb = service.getAllStudents();
        assertTrue(studentsFromDb.isEmpty());
        verify(repository, times(1)).findAll();
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldReturnAllStudentsAfterCreateNew() {
        Group group = new Group(1L, 1, new ArrayList<>(), new ArrayList<>(), new Schedule());
        Student student = new Student(0, "name", "lastname", group);
        StudentDto studentDto = new StudentDto(0, "name", "lastname", 1);
        List<Student> list = new ArrayList<>();
        list.add(student);
        when(repository.findAll()).thenReturn(list);
        when(groupRepository.findByNumber(anyInt())).thenReturn(group);
        when(repository.save(student)).thenReturn(student);
        List<StudentDto> studentsFromDb = service.createNewStudent(studentDto);
        assertFalse(studentsFromDb.isEmpty());
        assertEquals("name", studentsFromDb.get(0).getFirstName());
        verify(repository, times(1)).findAll();
        verify(repository, times(1)).save(student);
        verify(groupRepository, times(1)).findByNumber(anyInt());
        verifyNoMoreInteractions(repository);
        verifyNoMoreInteractions(groupRepository);
    }

    @Test
    void shouldThrownWrongInputDataExceptionIfExistWhenNameIsEmpty() {
        try {
            service.createNewStudent(new StudentDto(0, "", "lastName", 1));
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof WrongInputDataException);
            assertTrue(e.getMessage().contains("name"));
        }
        verifyNoInteractions(repository);
    }

    @Test
    void shouldThrownAlreadyExistExceptionIfExistWhenNameLengthMoreThanTwentyFiveChar() {
        try {
            service.createNewStudent(new StudentDto(0,
                    "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaffgdfgfddgdfgfg",
                    "lastName",
                    1));
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof WrongInputDataException);
            assertTrue(e.getMessage().contains("name"));
        }
        verifyNoInteractions(repository);
    }

    @Test
    void shouldThrownAlreadyExistExceptionIfExistWhenLastNameIsEmpty() {
        try {
            service.createNewStudent(new StudentDto(0, "name", "", 1));
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof WrongInputDataException);
            assertTrue(e.getMessage().contains("name"));
        }
        verifyNoInteractions(repository);
    }

    @Test
    void shouldThrownAlreadyExistExceptionIfExistWhenLastNameMoreThanFiftyCharacters() {
        try {
            service.createNewStudent(new StudentDto(0, "name",
                    "hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh",
                    1));
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof WrongInputDataException);
            assertTrue(e.getMessage().contains("name"));
        }
        verifyNoInteractions(repository);
    }

    @Test
    void shouldReturnEditedStudentAfterEdit() {
        Group group = new Group(1L, 1, new ArrayList<>(), new ArrayList<>(), new Schedule());
        Student editedStudent = new Student(1L, "name", "lastname", group);
        StudentDto studentForEdit = new StudentDto(1L, "Vasilii", "Petrov", 1);
        when(repository.findById(anyLong())).thenReturn(Optional.of(editedStudent));
        when(repository.save(editedStudent)).thenReturn(editedStudent);
        when(groupRepository.findByNumber(anyInt())).thenReturn(group);
        StudentDto studentFromDb = service.editStudent(studentForEdit);
        assertNotNull(studentFromDb);
        assertEquals("Vasilii", studentFromDb.getFirstName());
        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).save(editedStudent);
        verify(groupRepository, times(1)).findByNumber(anyInt());
        verifyNoMoreInteractions(repository);
        verifyNoMoreInteractions(groupRepository);
    }

    @Test
    void shouldThrownAnNotExistExceptionWhenEditIfNoStudent() {
        try {
            service.editStudent(new StudentDto(1L, "Vasilii", "Petrov", 1));
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof NotExistException);
            assertTrue(e.getMessage().contains("Student"));
        }
        verifyNoInteractions(repository);
    }

    @Test
    void shouldThrownWrongInputDataExceptionWhenEditIfExistIfNameIsEmpty() {
        try {
            service.editStudent(new StudentDto(0, "", "lastName", 1));
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof WrongInputDataException);
            assertTrue(e.getMessage().contains("name"));
        }
        verifyNoInteractions(repository);
    }

    @Test
    void shouldThrownWrongInputDataExceptionWhenEditIfExistIfNameLengthMoreThanTwentyFiveChar() {
        try {
            service.createNewStudent(new StudentDto(0,
                    "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaffgdfgfddgdfgfg",
                    "lastName", 1));
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof WrongInputDataException);
            assertTrue(e.getMessage().contains("name"));
        }
        verifyNoInteractions(repository);
    }

    @Test
    void shouldThrownWrongInputDataExceptionWhenEditIfExistIfLastNameIsEmpty() {
        try {
            service.createNewStudent(new StudentDto(0, "name", "", 1));
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof WrongInputDataException);
            assertTrue(e.getMessage().contains("name"));
        }
        verifyNoInteractions(repository);
    }

    @Test
    void shouldThrownWrongInputDataExceptionWhenEditIfExistIfLastNameMoreThanFiftyCharacters() {
        try {
            service.createNewStudent(new StudentDto(0, "name",
                    "hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh",
                    1));
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof WrongInputDataException);
            assertTrue(e.getMessage().contains("name"));
        }
        verifyNoInteractions(repository);
    }

    @Test
    void shouldReturnStringAfterRemove() throws NotExistException {
        when(repository.existsById(anyLong())).thenReturn(true);
        String response = service.removeStudent(anyLong());
        assertTrue(response.contains("success"));
        verify(repository, times(1)).existsById(anyLong());
        verify(repository, times(1)).deleteById(anyLong());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldReturnNotExistExceptionWhenRemoveIfNotExist() {
        when(repository.existsById(anyLong())).thenReturn(false);
        try {
            service.removeStudent(anyLong());
        } catch (NotExistException e) {
            assertTrue(e.getMessage().contains("Student"));
        }
        verify(repository, times(1)).existsById(anyLong());
        verifyNoMoreInteractions(repository);
    }
}