package com.testTask.university.mappers;

import com.testTask.university.dto.LectureDto;
import com.testTask.university.entity.Lecture;
import org.springframework.stereotype.Component;

@Component
public class LectureMapper implements Mapper<LectureDto, Lecture>{

    @Override
    public LectureDto convertToDto(Lecture lecture) {
        return LectureDto.builder()
                .lectureName(lecture.getLectureName())
                .lectureDate(lecture.getDate())
                .build();
    }

    @Override
    public Lecture convertToEntity(LectureDto lectureDto) {
        Lecture lecture = new Lecture();
        lecture.setDate(lectureDto.getLectureDate());
        lecture.setLectureName(lectureDto.getLectureName());
        return lecture;
    }
}
