package com.testTask.university.mappers;

import com.testTask.university.dto.ScheduleDto;
import com.testTask.university.entity.Group;
import com.testTask.university.entity.Schedule;
import com.testTask.university.utils.mappers.ScheduleMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ScheduleMapperTest {

    @Autowired
    private ScheduleMapper mapper;

    @Test
    public void shouldReturnAudienceDto() {
        Schedule schedule = new Schedule(0, "2022-07-13",
                new Group(1L, 12, new ArrayList<>(), new ArrayList<>(), new Schedule()));
        ScheduleDto scheduleDto = new ScheduleDto(0, "2022-07-13", 12, null);
        ScheduleDto converted = mapper.convertToDto(schedule);
        assertEquals(scheduleDto, converted);
    }

    @Test
    public void shouldReturnAudience() {
        ScheduleDto scheduleDto = new ScheduleDto(0, "2022-07-13", 12, null);
        Schedule converted = mapper.convertToEntity(scheduleDto);
        Schedule expected = new Schedule(0, "2022-07-13", null);
        assertEquals(expected, converted);
    }
}
