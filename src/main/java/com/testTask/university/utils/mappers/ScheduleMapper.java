package com.testTask.university.utils.mappers;

import com.testTask.university.dto.ScheduleDto;
import com.testTask.university.entity.Schedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ScheduleMapper {


    public ScheduleDto convertToDto(Schedule schedule) {
        return ScheduleDto.builder()
                .scheduleId(schedule.getId())
                .date(schedule.getDate())
                .groupNumber(schedule.getGroup().getNumber())
                .build();
    }

    public Schedule convertToEntity(ScheduleDto scheduleDto){
        Schedule schedule = new Schedule();
        schedule.setId(scheduleDto.getScheduleId());
        schedule.setDate(scheduleDto.getDate());
        return schedule;
    }
}
