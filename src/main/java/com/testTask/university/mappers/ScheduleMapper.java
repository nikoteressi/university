package com.testTask.university.mappers;

import com.testTask.university.dao.GroupRepository;
import com.testTask.university.dto.LectureDto;
import com.testTask.university.dto.ScheduleDto;
import com.testTask.university.entity.Group;
import com.testTask.university.entity.Lecture;
import com.testTask.university.entity.Schedule;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class ScheduleMapper {

    private final ModelMapper mapper;
    private final GroupRepository groupRepository;

    public ScheduleDto convertToDto(Schedule schedule) {
        return ScheduleDto.builder()
                .scheduleId(schedule.getId())
                .date(schedule.getDate())
                .group_id(schedule.getGroup().getId())
                .lectures(getGroupLecturesByDate(schedule).stream()
                        .map(f -> mapper.map(f, LectureDto.class))
                        .collect(Collectors.toList()))
                .build();
    }

    public Schedule convertToEntity(ScheduleDto scheduleDto) {
        Schedule schedule = new Schedule();
        Group group = groupRepository.findById(scheduleDto.getGroup_id()).orElse(null);
        schedule.setId(scheduleDto.getScheduleId());
        schedule.setDate(scheduleDto.getDate());
        schedule.setGroup(group);
        return schedule;
    }

    private List<Lecture> getGroupLecturesByDate(Schedule schedule) {
        return schedule.getGroup().getLectures().stream()
                .filter(f -> f.getDate().equals(schedule.getDate()))
                .collect(Collectors.toList());
    }
}
