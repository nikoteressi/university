package com.testTask.university.mappers;

import com.testTask.university.dto.AudienceDto;
import com.testTask.university.dto.LectureDto;
import com.testTask.university.entity.Audience;
import com.testTask.university.entity.Lecture;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AudienceMapper {

    private final ModelMapper mapper;

    public AudienceDto convertToDto(Audience audience) {

        return AudienceDto.builder()
                .id(audience.getId())
                .number(audience.getNumber())
                .lectures(audience.getLectures().stream()
                        .map(f -> mapper.map(f, LectureDto.class))
                        .collect(Collectors.toList()))
                .build();
    }

    public Audience convertToEntity(AudienceDto audienceDto) {
        Audience audience = new Audience();
        audience.setId(audienceDto.getId());
        audience.setNumber(audienceDto.getNumber());
        audience.setLectures(audienceDto.getLectures().stream()
                .map(f -> mapper.map(f, Lecture.class))
                .collect(Collectors.toList()));
        return audience;
    }
}
