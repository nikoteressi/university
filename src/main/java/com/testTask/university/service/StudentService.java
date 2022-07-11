package com.testTask.university.service;

import com.testTask.university.dto.StudentDto;
import com.testTask.university.exceptions.AlreadyExistException;
import com.testTask.university.exceptions.NotExistException;
import com.testTask.university.exceptions.WrongInputDataException;

import java.util.List;

public interface StudentService {
    List<StudentDto> getAllStudents();

    List<StudentDto> createNewStudent(StudentDto student) throws AlreadyExistException, WrongInputDataException, Exception;

    StudentDto editStudent(StudentDto student) throws NotExistException, Exception;

    String removeStudent(long studentId) throws NotExistException;
}
