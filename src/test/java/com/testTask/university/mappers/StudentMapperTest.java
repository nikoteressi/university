package com.testTask.university.mappers;

import com.testTask.university.dto.StudentDto;
import com.testTask.university.entity.Group;
import com.testTask.university.entity.Schedule;
import com.testTask.university.entity.Student;
import com.testTask.university.utils.mappers.StudentsMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class StudentMapperTest {

    @Autowired
    private StudentsMapper mapper;

    @Test
    public void shouldReturnAudienceDto() {
        Student student = new Student(0, "name", "lastname",
                new Group(1L, 12, new ArrayList<>(), new ArrayList<>(), new Schedule()));
        StudentDto studentDto = new StudentDto(0, "name", "lastname", 12);
        StudentDto converted = mapper.convertToDto(student);
        assertEquals(studentDto, converted);
    }

    @Test
    public void shouldReturnAudience() {
        StudentDto studentDto = new StudentDto(0, "name", "lastname", 12);
        Student expected = new Student(0, "name", "lastname", null);
        Student converted = mapper.convertToEntity(studentDto);
        assertEquals(expected, converted);
    }
}
