package com.testTask.university.service.impl;

import com.testTask.university.dao.GroupRepository;
import com.testTask.university.dto.GroupDto;
import com.testTask.university.entity.Group;
import com.testTask.university.exceptions.AlreadyExistException;
import com.testTask.university.exceptions.NotExistException;
import com.testTask.university.utils.mappers.GroupMapper;
import com.testTask.university.utils.validators.FieldValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class GroupServiceImpl implements com.testTask.university.service.GroupService {

    private final GroupRepository repository;

    private final GroupMapper mapper;
    private final FieldValidator fieldValidator;

    @Override
    public List<GroupDto> getAllGroups() {
        return repository.findAll().stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList());
    }


    @Transactional
    @Override
    public List<GroupDto> createNewGroup(GroupDto group) {
        fieldValidator.validateGroupNumber(group.getNumber());
        checkIfExistByNumber(group.getNumber());
        repository.save(mapper.convertToEntity(group));
        return getAllGroups();
    }

    @Transactional
    @Override
    public GroupDto editGroup(GroupDto group) {
        fieldValidator.validateGroupNumber(group.getNumber());
        Group groupFromDb = getGroupByByNumber(group.getNumber());
        groupFromDb.setNumber(group.getNumber());
        return mapper.convertToDto(repository.save(groupFromDb));
    }

    @Transactional
    @Override
    public String removeGroup(long groupId) {
        checkIfExistToRemove(groupId);
        repository.deleteById(groupId);
        return "Group with ID: " + groupId + " has been successfully deleted.";
    }

    private void checkIfExistToRemove(long id) {
        if (!repository.existsById(id))
            throw new NotExistException("The group with id '" + id + "' not exist.");
    }

    private void checkIfExistByNumber(int groupNumber) {
        if (repository.existsByNumber(groupNumber))
            throw new AlreadyExistException("The group with number '" + groupNumber + "' already exist.");
    }

    private Group getGroupByByNumber(int groupNumber) {
        Group groupFromDb = repository.findByNumber(groupNumber);
        if (groupFromDb == null)
            throw new NotExistException("The group with number '" + groupNumber + "' not exist.");
        return groupFromDb;
    }
}
