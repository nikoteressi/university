package com.testTask.university.utils.mappers;

import com.testTask.university.dto.GroupDto;
import com.testTask.university.entity.Group;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupMapper {


    public GroupDto convertToDto(Group group) {
        return GroupDto.builder()
                .id(group.getId())
                .number(group.getNumber())
                .build();
    }

    public Group convertToEntity(GroupDto groupDto) {
        Group group = new Group();
        group.setId(groupDto.getId());
        group.setNumber(groupDto.getNumber());
        return group;
    }
}
