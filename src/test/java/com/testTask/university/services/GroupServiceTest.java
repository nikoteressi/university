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
import static org.mockito.Mockito.when;

@SpringBootTest
public class GroupServiceTest {

    @Autowired
    private GroupService groupService;

    @MockBean
    private GroupRepository groupRepository;

    @Test
    void shouldReturnListWithAllGroups() {
        List<Group> groups = new ArrayList<>();
        groups.add(new Group(1L, 1, new ArrayList<>(), new ArrayList<>(), new Schedule()));
        when(groupRepository.findAll()).thenReturn(groups);
        List<GroupDto> groupsFromDb = groupService.getAllGroups();
        assertEquals(1, groupsFromDb.size());
        assertEquals(1, groupsFromDb.get(0).getNumber());
    }

    @Test
    void shouldReturnEmptyListIfThereIsNoGroups() {
        List<Group> groups = new ArrayList<>();
        when(groupRepository.findAll()).thenReturn(groups);
        List<GroupDto> groupsFromDb = groupService.getAllGroups();
        assertTrue(groupsFromDb.isEmpty());
    }

    @Test
    void shouldReturnAlreadyExistExceptionWhenCreateNewIfExist() {
        GroupDto groupDto = new GroupDto(1L, 1);
        when(groupRepository.existsByNumber(anyInt())).thenReturn(true);
        try {
            groupService.createNewGroup(groupDto);
            fail();
        } catch (Exception e) {
            assertTrue((e instanceof AlreadyExistException));
            assertTrue(e.getMessage().contains("group"));
        }
    }

    @Test
    void shouldReturnWrongInputDataExceptionWhenCreateNewIfWrongNumber() {
        GroupDto groupDto = new GroupDto(0, 1567);
        try {
            groupService.createNewGroup(groupDto);
            fail();
        } catch (Exception e) {
            assertTrue((e instanceof WrongInputDataException));
            assertTrue(e.getMessage().contains("number"));
        }
    }

    @Test
    void shouldReturnAllLecturesAfterCreateNew() throws Exception {
        Group group = new Group(1L, 12, new ArrayList<>(), new ArrayList<>(), new Schedule());
        GroupDto groupDto = new GroupDto(0, 12);
        List<Group> groups = new ArrayList<>();
        groups.add(group);
        when(groupRepository.findAll()).thenReturn(groups);
        List<GroupDto> groupsFromDb = groupService.createNewGroup(groupDto);
        assertFalse(groupsFromDb.isEmpty());
        assertEquals(12, groups.get(0).getNumber());
    }

    @Test
    void shouldReturnEditedGroupAfterEdit() throws Exception {
        Group group = new Group(1L, 1, new ArrayList<>(), new ArrayList<>(), new Schedule());
        GroupDto groupDto = new GroupDto(1L, 12);
        when(groupRepository.save(group)).thenReturn(group);
        when(groupRepository.findByNumber(anyInt())).thenReturn(group);
        GroupDto groupFromDb = groupService.editGroup(groupDto);
        assertNotNull(groupFromDb);
        assertEquals(12, groupFromDb.getNumber());

    }

    @Test
    void shouldThrownAnNotExistExceptionWhenEditIfThereIsNoGroup() {
        GroupDto groupDto = new GroupDto(1L, 1);
        try {
            groupService.editGroup(groupDto);
            fail();
        } catch (Exception e) {
            assertTrue((e instanceof NotExistException));
            assertTrue(e.getMessage().contains("group"));
        }
    }

    @Test
    void shouldReturnStringAfterRemove() throws NotExistException {
        when(groupRepository.existsById(anyLong())).thenReturn(true);
        String response = groupService.removeGroup(anyLong());
        assertTrue(response.contains("success"));
    }

    @Test
    void shouldReturnNotExistExceptionWhenRemoveIfNotExist() {
        when(groupRepository.existsById(anyLong())).thenReturn(false);
        try {
            groupService.removeGroup(anyLong());
        } catch (NotExistException e) {
            assertTrue(e.getMessage().contains("group"));
        }
    }
}
