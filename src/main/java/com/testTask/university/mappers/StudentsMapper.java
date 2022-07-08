package com.testTask.university.mappers;

import com.testTask.university.dao.ScheduleRepository;
import com.testTask.university.dto.StudentDto;
import com.testTask.university.entity.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentsMapper implements Mapper<StudentDto, Student>{

    private final ScheduleRepository scheduleRepository;
    @Override
    public StudentDto convertToDto(Student student) {
        return StudentDto.builder()
                .firstName(student.getFirstName())
                .LastName(student.getLastName())
                .scheduleId(student.getSchedule().getId())
                .build();
    }

    @Override
    public Student convertToEntity(StudentDto studentDto) {
        Student student = new Student();
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        student.setSchedule(scheduleRepository.findById(studentDto.getScheduleId()).orElse(null));
        return student;
    }
}
