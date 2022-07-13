package com.testTask.university.utils.mappers;

import com.testTask.university.dto.LectureDto;
import com.testTask.university.entity.Lecture;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LectureMapper {

    public LectureDto convertToDto(Lecture lecture) {
        return LectureDto.builder()
                .lectureId(lecture.getId())
                .lectureName(lecture.getName())
                .lectureDate(lecture.getDate())
                .audienceNumber(lecture.getAudience().getNumber())
                .groupNumber(lecture.getGroup().getNumber())
                .build();
    }

    public Lecture convertToEntity(LectureDto lectureDto) {
        Lecture lecture = new Lecture();
        lecture.setId(lectureDto.getLectureId());
        lecture.setName(lectureDto.getLectureName());
        lecture.setDate(lectureDto.getLectureDate());
        return lecture;
    }
}
