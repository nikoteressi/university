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
        verify(repository, times(1)).findAll();
        assertEquals(1, studentsFromDb.size());
    }

    @Test
    void shouldReturnEmptyListWithStudentsIfThereIsNoStudents() {
        List<Student> students = new ArrayList<>();
        when(repository.findAll()).thenReturn(students);
        List<StudentDto> studentsFromDb = service.getAllStudents();
        assertTrue(studentsFromDb.isEmpty());
    }

    @Test
    void shouldReturnAllStudentsAfterCreateNew() throws Exception {
        Group group = new Group(1L, 1, new ArrayList<>(), new ArrayList<>(), new Schedule());
        Student student = new Student(1L, "name", "lastname", group);
        List<Student> list = new ArrayList<>();
        list.add(student);
        when(repository.findAll()).thenReturn(list);
        when(groupRepository.findByNumber(anyInt())).thenReturn(group);
        List<StudentDto> studentsFromDb = service.createNewStudent(new StudentDto(0, "name", "lastName", 1));
        assertFalse(studentsFromDb.isEmpty());
        assertEquals("name", studentsFromDb.get(0).getFirstName());
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
    }

    @Test
    void shouldThrownAlreadyExistExceptionIfExistWhenNameLengthMoreThanTwentyFiveChar() {
        try {
            service.createNewStudent(new StudentDto(0, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaffgdfgfddgdfgfg", "lastName", 1));
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof WrongInputDataException);
            assertTrue(e.getMessage().contains("name"));
        }
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
    }

    @Test
    void shouldThrownAlreadyExistExceptionIfExistWhenLastNameMoreThanFiftyCharacters() {
        try {
            service.createNewStudent(new StudentDto(0, "name", "hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh", 1));
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof WrongInputDataException);
            assertTrue(e.getMessage().contains("name"));
        }
    }

    @Test
    void shouldReturnEditedStudentAfterEdit() throws Exception {
        Group group = new Group(1L, 1, new ArrayList<>(), new ArrayList<>(), new Schedule());
        Student editedStudent = new Student(1L, "name", "lastname", group);
        StudentDto studentForEdit = new StudentDto(1L, "Vasilii", "Petrov", 1);
        when(repository.findById(anyLong())).thenReturn(Optional.of(editedStudent));
        when(repository.save(editedStudent)).thenReturn(editedStudent);
        when(groupRepository.findByNumber(anyInt())).thenReturn(group);
        StudentDto studentFromDb = service.editStudent(studentForEdit);
        assertNotNull(studentFromDb);
        assertEquals("Vasilii", studentFromDb.getFirstName());
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
    }

    @Test
    void shouldThrownWrongInputDataExceptionWhenEditIfExistIfNameLengthMoreThanTwentyFiveChar() {
        try {
            service.createNewStudent(new StudentDto(0, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaffgdfgfddgdfgfg", "lastName", 1));
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof WrongInputDataException);
            assertTrue(e.getMessage().contains("name"));
        }
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
    }

    @Test
    void shouldThrownWrongInputDataExceptionWhenEditIfExistIfLastNameMoreThanFiftyCharacters() {
        try {
            service.createNewStudent(new StudentDto(0, "name", "hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh", 1));
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof WrongInputDataException);
            assertTrue(e.getMessage().contains("name"));
        }
    }

    @Test
    void shouldReturnStringAfterRemove() throws NotExistException {
        when(repository.existsById(anyLong())).thenReturn(true);
        String response = service.removeStudent(anyLong());
        assertTrue(response.contains("success"));
    }

    @Test
    void shouldReturnNotExistExceptionWhenRemoveIfNotExist() {
        when(repository.existsById(anyLong())).thenReturn(false);
        try {
            service.removeStudent(anyLong());
        } catch (NotExistException e) {
            assertTrue(e.getMessage().contains("Student"));
        }
    }
}