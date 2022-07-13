package com.testTask.university.services;

import com.testTask.university.dao.GroupRepository;
import com.testTask.university.dto.GroupDto;
import com.testTask.university.entity.Group;
import com.testTask.university.entity.Schedule;
import com.testTask.university.exceptions.AlreadyExistException;
import com.testTask.university.exceptions.NotExistException;
import com.testTask.university.exceptions.WrongInputDataException;
import com.testTask.university.service.GroupService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class GroupServiceTest {

    @Autowired
    private GroupService service;

    @MockBean
    private GroupRepository repository;

    @Test
    void shouldReturnListWithAllGroups() {
        List<Group> groups = new ArrayList<>();
        groups.add(new Group(1L, 1, new ArrayList<>(), new ArrayList<>(), new Schedule()));
        when(repository.findAll()).thenReturn(groups);
        List<GroupDto> groupsFromDb = service.getAllGroups();
        assertEquals(1, groupsFromDb.size());
        assertEquals(1, groupsFromDb.get(0).getNumber());
        verify(repository, times(1)).findAll();
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldReturnEmptyListIfThereIsNoGroups() {
        List<GroupDto> groupsFromDb = service.getAllGroups();
        assertTrue(groupsFromDb.isEmpty());
        verify(repository, times(1)).findAll();
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldReturnAlreadyExistExceptionWhenCreateNewIfExist() {
        GroupDto groupDto = new GroupDto(1L, 1);
        when(repository.existsByNumber(anyInt())).thenReturn(true);
        AlreadyExistException exception = assertThrows(AlreadyExistException.class,
                () -> service.createNewGroup(groupDto));
        assertTrue(exception.getMessage().contains("group"));
        verify(repository, times(1)).existsByNumber(anyInt());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldReturnWrongInputDataExceptionWhenCreateNewIfWrongNumber() {
        GroupDto groupDto = new GroupDto(0, 1567);
        WrongInputDataException exception = assertThrows(WrongInputDataException.class,
                () -> service.createNewGroup(groupDto));
        assertTrue(exception.getMessage().contains("group"));
        verifyNoInteractions(repository);
    }

    @Test
    void shouldReturnAllLecturesAfterCreateNew() {
        Group group = new Group(1L, 12, new ArrayList<>(), new ArrayList<>(), new Schedule());
        GroupDto groupDto = new GroupDto(0, 12);
        List<Group> groups = new ArrayList<>();
        groups.add(group);
        when(repository.findAll()).thenReturn(groups);
        List<GroupDto> groupsFromDb = service.createNewGroup(groupDto);
        assertFalse(groupsFromDb.isEmpty());
        assertEquals(12, groups.get(0).getNumber());
        verify(repository, times(1)).save(group);
        verify(repository, times(1)).existsByNumber(anyInt());
        verify(repository, times(1)).findAll();
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldReturnEditedGroupAfterEdit() {
        Group group = new Group(1L, 1, new ArrayList<>(), new ArrayList<>(), new Schedule());
        GroupDto groupDto = new GroupDto(1L, 12);
        when(repository.save(group)).thenReturn(group);
        when(repository.findByNumber(anyInt())).thenReturn(group);
        GroupDto groupFromDb = service.editGroup(groupDto);
        assertNotNull(groupFromDb);
        assertEquals(12, groupFromDb.getNumber());
        verify(repository, times(1)).save(group);
        verify(repository, times(1)).findByNumber(anyInt());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldThrownAnNotExistExceptionWhenEditIfThereIsNoGroup() {
        GroupDto groupDto = new GroupDto(1L, 1);
        when(repository.findByNumber(anyInt())).thenReturn(null);
        NotExistException exception = assertThrows(NotExistException.class,
                () -> service.editGroup(groupDto));
        assertTrue(exception.getMessage().contains("group"));
        verify(repository, times(1)).findByNumber(anyInt());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldReturnStringAfterRemove() throws NotExistException {
        when(repository.existsById(anyLong())).thenReturn(true);
        String response = service.removeGroup(anyLong());
        assertTrue(response.contains("success"));
        verify(repository, times(1)).existsById(anyLong());
        verify(repository, times(1)).deleteById(anyLong());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldReturnNotExistExceptionWhenRemoveIfNotExist() {
        when(repository.existsById(anyLong())).thenReturn(false);
        NotExistException exception = assertThrows(NotExistException.class,
                () -> service.removeGroup(anyLong()));
        assertTrue(exception.getMessage().contains("group"));
        verify(repository, times(1)).existsById(anyLong());
        verifyNoMoreInteractions(repository);
    }
}
