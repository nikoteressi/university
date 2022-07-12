package com.testTask.university.mappers;

import com.testTask.university.dto.GroupDto;
import com.testTask.university.entity.Group;
import com.testTask.university.entity.Schedule;
import com.testTask.university.utils.mappers.GroupMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class GroupMapperTest {

    @Autowired
    private GroupMapper mapper;

    @Test
    public void shouldReturnAudienceDto() {
        Group group = new Group(0, 12, new ArrayList<>(), new ArrayList<>(), new Schedule());
        GroupDto groupDto = new GroupDto(0, 12);
        GroupDto converted = mapper.convertToDto(group);
        assertEquals(groupDto, converted);
    }

    @Test
    public void shouldReturnAudience() {
        Group group = new Group(0, 12, new ArrayList<>(), new ArrayList<>(), new Schedule());
        GroupDto groupDto = new GroupDto(0, 12);
        Group converted = mapper.convertToEntity(groupDto);
        assertEquals(group, converted);
    }
}
