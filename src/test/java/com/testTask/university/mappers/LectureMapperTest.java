package com.testTask.university.mappers;

import com.testTask.university.dto.LectureDto;
import com.testTask.university.entity.Audience;
import com.testTask.university.entity.Group;
import com.testTask.university.entity.Lecture;
import com.testTask.university.entity.Schedule;
import com.testTask.university.utils.mappers.LectureMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class LectureMapperTest {

    @Autowired
    private LectureMapper mapper;

    @Test
    public void shouldReturnAudienceDto() {
        Lecture lecture = new Lecture(0, "name", "2022-08-14",
                new Audience(1L, 12, new ArrayList<>()),
                new Group(1L, 12, new ArrayList<>(), new ArrayList<>(), new Schedule()));
        LectureDto lectureDto = new LectureDto(0, "name", "2022-08-14", 12, 12);
        LectureDto converted = mapper.convertToDto(lecture);
        assertEquals(lectureDto, converted);
    }

    @Test
    public void shouldReturnAudience() {
        Lecture lecture = new Lecture(0, "name", "2022-08-14",
                new Audience(1L, 12, new ArrayList<>()),
                new Group(1L, 12, new ArrayList<>(), new ArrayList<>(), new Schedule()));
        LectureDto lectureDto = new LectureDto(0, "name", "2022-08-14", 12, 12);
        Lecture expected = new Lecture(0, "name", "2022-08-14", null, null);
        Lecture converted = mapper.convertToEntity(lectureDto);
        assertEquals(expected, converted);
    }
}
