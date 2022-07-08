package com.testTask.university.mappers;

import com.testTask.university.dto.LectureDto;
import com.testTask.university.dto.ScheduleDto;
import com.testTask.university.entity.Lecture;
import com.testTask.university.entity.Schedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class ScheduleMapper implements Mapper<ScheduleDto, Schedule>{

    private final Mapper<LectureDto, Lecture> lectureMapper;

    public ScheduleDto convertToDto(Schedule schedule) {
        List<LectureDto> lectures = schedule.getLectures().stream()
                .map(lectureMapper::convertToDto)
                .collect(Collectors.toList());
       return ScheduleDto.builder()
                .lectures(lectures)
                .build();
    }

    @Override
    public Schedule convertToEntity(ScheduleDto scheduleDto) {
        Schedule schedule = new Schedule();
        List<Lecture> lectures = scheduleDto.getLectures().stream()
                .map(lectureMapper::convertToEntity)
                .collect(Collectors.toList());
        schedule.setLectures(lectures);
        return schedule;
    }
}
