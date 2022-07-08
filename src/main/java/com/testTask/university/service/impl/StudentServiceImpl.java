package com.testTask.university.service.impl;

import com.testTask.university.dao.ScheduleRepository;
import com.testTask.university.dao.StudentRepository;
import com.testTask.university.dto.StudentDto;
import com.testTask.university.entity.Student;
import com.testTask.university.exceptions.NotExistException;
import com.testTask.university.mappers.Mapper;
import com.testTask.university.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository repository;
    private final ScheduleRepository scheduleRepository;
    private final Mapper<StudentDto, Student> mapper;


    @Override
    public List<StudentDto> getAllStudents() {
        return repository.findAll().stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentDto> createNewStudent(StudentDto student) {
        Student studentToSave = mapper.convertToEntity(student);
        repository.save(studentToSave);
        return getAllStudents();
    }

    @Override
    public StudentDto editStudent(StudentDto student) throws NotExistException {
        Student studentFromDb = repository.findById(student.getStudentId()).orElse(null);
        if (studentFromDb == null) throw new NotExistException("Student with ID: " + student.getStudentId() + " not found.");
        studentFromDb.setFirstName(student.getFirstName());
        studentFromDb.setLastName(student.getLastName());
        studentFromDb.setSchedule(scheduleRepository.findById(student.getScheduleId()).orElse(null));
        return mapper.convertToDto(repository.save(studentFromDb));
    }

    @Override
    public String removeStudent(long studentId) throws NotExistException {
        Student studentFromDb = repository.findById(studentId).orElse(null);
        if (studentFromDb == null) throw new NotExistException("Student with ID: " + studentId + " not found.");
        repository.deleteById(studentId);
        return  "Student with ID: " + studentId + " has been successfully deleted.";
    }
}
