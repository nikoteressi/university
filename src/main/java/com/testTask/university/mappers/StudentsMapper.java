package com.testTask.university.mappers;

import com.testTask.university.dao.GroupRepository;
import com.testTask.university.dto.StudentDto;
import com.testTask.university.entity.Group;
import com.testTask.university.entity.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentsMapper {

    private final GroupRepository groupRepository;

    public StudentDto convertToDto(Student student) {
        return StudentDto.builder()
                .studentId(student.getId())
                .firstName(student.getFirstName())
                .LastName(student.getLastName())
                .groupId(student.getId())
                .build();
    }

    public Student convertToEntity(StudentDto studentDto) {
        Student student = new Student();
        Group group = groupRepository.findById(studentDto.getGroupId()).orElse(null);
        student.setId(studentDto.getStudentId());
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        student.setGroup(group);
        return student;
    }
}
