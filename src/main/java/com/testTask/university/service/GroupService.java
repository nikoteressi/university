package com.testTask.university.service;

import com.testTask.university.dto.GroupDto;
import com.testTask.university.exceptions.NotExistException;

import java.util.List;

public interface GroupService {

    List<GroupDto> getAllGroups();

    List<GroupDto> createNewGroup(GroupDto Group);

    GroupDto editGroup(GroupDto group);

    String removeGroup(long groupId);
}
