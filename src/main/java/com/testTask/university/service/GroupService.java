package com.testTask.university.service;

import com.testTask.university.dto.GroupDto;
import com.testTask.university.exceptions.NotExistException;

import java.util.List;

public interface GroupService {

    List<GroupDto> getAllGroups();

    List<GroupDto> createNewGroup(GroupDto Group) throws Exception;

    GroupDto editGroup(GroupDto group) throws Exception;

    String removeGroup(long groupId) throws NotExistException;
}
