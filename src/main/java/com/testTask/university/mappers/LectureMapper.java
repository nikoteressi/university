package com.testTask.university.mappers;

import com.testTask.university.dto.AudienceDto;
import com.testTask.university.dto.GroupDto;
import com.testTask.university.dto.LectureDto;
import com.testTask.university.entity.Audience;
import com.testTask.university.entity.Group;
import com.testTask.university.entity.Lecture;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LectureMapper {

    private final ModelMapper mapper;

    public LectureDto convertToDto(Lecture lecture) {
        return LectureDto.builder()
                .lectureId(lecture.getId())
                .lectureName(lecture.getName())
                .lectureDate(lecture.getDate())
                .audiences(lecture.getAudiences().stream()
                        .map(f -> mapper.map(f, AudienceDto.class))
                        .collect(Collectors.toList()))
                .groups(lecture.getGroups().stream()
                        .map(f -> mapper.map(f, GroupDto.class))
                        .collect(Collectors.toList()))
                .build();
    }

    public Lecture convertToEntity(LectureDto lectureDto) {
        Lecture lecture = new Lecture();
        lecture.setId(lectureDto.getLectureId());
        lecture.setName(lectureDto.getLectureName());
        lecture.setDate(lectureDto.getLectureDate());
        lecture.setAudiences(lectureDto.getAudiences().stream()
                .map(f -> mapper.map(f, Audience.class))
                .collect(Collectors.toList()));
        lecture.setGroups(lectureDto.getGroups().stream()
                .map(f -> mapper.map(f, Group.class))
                .collect(Collectors.toList()));
        return lecture;
    }
}
