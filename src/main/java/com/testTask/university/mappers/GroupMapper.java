package com.testTask.university.mappers;

import com.testTask.university.dto.GroupDto;
import com.testTask.university.dto.LectureDto;
import com.testTask.university.dto.StudentDto;
import com.testTask.university.entity.Group;
import com.testTask.university.entity.Lecture;
import com.testTask.university.entity.Student;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GroupMapper {

    private final ModelMapper mapper;

    public GroupDto convertToDto(Group group) {
        return GroupDto.builder()
                .id(group.getId())
                .number(group.getNumber())
                .students(group.getStudents().stream()
                        .map(f -> mapper.map(f, StudentDto.class))
                        .collect(Collectors.toList()))
                .lectures(group.getLectures().stream()
                        .map(f -> mapper.map(f, LectureDto.class))
                        .collect(Collectors.toList()))
                .build();
    }

    public Group convertToEntity(GroupDto groupDto) {
        Group group = new Group();
        group.setId(group.getId());
        group.setNumber(group.getNumber());
        group.setStudents(groupDto.getStudents().stream()
                .map(f -> mapper.map(f, Student.class))
                .collect(Collectors.toList()));
        group.setLectures(groupDto.getLectures().stream()
                .map(f -> mapper.map(f, Lecture.class))
                .collect(Collectors.toList()));
        return group;
    }
}
