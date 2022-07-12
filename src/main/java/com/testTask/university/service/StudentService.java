package com.testTask.university.service;

import com.testTask.university.dto.StudentDto;
import com.testTask.university.exceptions.NotExistException;

import java.util.List;

public interface StudentService {
    List<StudentDto> getAllStudents();

    List<StudentDto> createNewStudent(StudentDto student) throws  Exception;

    StudentDto editStudent(StudentDto student) throws  Exception;

    String removeStudent(long studentId) throws NotExistException;
}
