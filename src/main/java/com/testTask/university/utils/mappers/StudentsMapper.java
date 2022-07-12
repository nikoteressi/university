package com.testTask.university.utils.mappers;

import com.testTask.university.dto.StudentDto;
import com.testTask.university.entity.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentsMapper {

    public StudentDto convertToDto(Student student) {
        return StudentDto.builder()
                .studentId(student.getId())
                .firstName(student.getFirstName())
                .LastName(student.getLastName())
                .groupNumber(student.getGroup().getNumber())
                .build();
    }

    public Student convertToEntity(StudentDto studentDto) {
        Student student = new Student();
        student.setId(studentDto.getStudentId());
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        return student;
    }
}
