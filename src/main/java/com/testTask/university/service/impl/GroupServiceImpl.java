package com.testTask.university.service.impl;

import com.testTask.university.dao.GroupRepository;
import com.testTask.university.dto.GroupDto;
import com.testTask.university.entity.Group;
import com.testTask.university.exceptions.AlreadyExistException;
import com.testTask.university.exceptions.NotExistException;
import com.testTask.university.exceptions.WrongInputDataException;
import com.testTask.university.mappers.GroupMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.ignoreCase;

@RequiredArgsConstructor
@Component
public class GroupServiceImpl implements com.testTask.university.service.GroupService {

    private final GroupRepository repository;

    private final GroupMapper mapper;

    @Override
    public List<GroupDto> getAllGroups() {
        return repository.findAll().stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList());
    }


    @Override
    public List<GroupDto> createNewGroup(GroupDto group) throws Exception {
        Group groupToSave = mapper.convertToEntity(group);
        if (validateCreateGroup(groupToSave)) repository.save(groupToSave);
        return getAllGroups();
    }

    @Override
    public GroupDto editGroup(GroupDto group) throws Exception {
        Group groupToSave = mapper.convertToEntity(group);
        Group updatedGroup = null;
        if (validateUpdateGroup(groupToSave)) {
            Group groupFromDb = repository.findById(groupToSave.getId()).orElse(null);
            Objects.requireNonNull(groupFromDb).setNumber(groupToSave.getNumber());
            groupFromDb.setLectures(groupToSave.getLectures());
            updatedGroup = repository.save(groupFromDb);
        }
        return mapper.convertToDto(Objects.requireNonNull(updatedGroup));
    }

    @Override
    public String removeGroup(long groupId) throws NotExistException {
        if (validateRemoveGroup(groupId)) repository.deleteById(groupId);
        return "Group with ID: " + groupId + " has been successfully deleted.";
    }

    private boolean validateRemoveGroup(long id) throws NotExistException {
        if (!checkIfExistById(id))
            throw new NotExistException("The group with id \"" + id + "\" not exist.");

        return true;
    }

    private boolean validateCreateGroup(Group group) throws Exception {
        if (!checkGroupNumber(group.getNumber()))
            throw new WrongInputDataException("The group number is wrong. Must be 1-1000. Received audience number: " + group.getNumber());

        if (!checkIfExistByNumber(group))
            throw new AlreadyExistException("The group with number \"" + group.getNumber() + "\" already exist.");

        return true;
    }

    private boolean validateUpdateGroup(Group group) throws Exception {
        if (!checkIfExistById(group.getId()))
            throw new NotExistException("The group with number \"" + group.getNumber() + "\" not exist.");

        if (!checkGroupNumber(group.getNumber()))
            throw new AlreadyExistException("The group with number \"" + group.getNumber() + "\" already exist.");
        return true;
    }

    private boolean checkGroupNumber(int number) {
        return String.valueOf(number).matches("^[1-9][0 -9]{1,3}$");
    }

    private boolean checkIfExistByNumber(Group group) {
        ExampleMatcher nameMatcher = ExampleMatcher.matching()
                .withIgnorePaths("id")
                .withMatcher("number", ignoreCase());
        Example<Group> example = Example.of(group, nameMatcher);
        return repository.exists(example);
    }

    private boolean checkIfExistById(long id) {
        return repository.existsById(id);
    }
}
