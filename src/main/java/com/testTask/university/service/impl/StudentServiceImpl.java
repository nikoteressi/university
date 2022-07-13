package com.testTask.university.service.impl;

import com.testTask.university.dao.GroupRepository;
import com.testTask.university.dao.StudentRepository;
import com.testTask.university.dto.StudentDto;
import com.testTask.university.entity.Group;
import com.testTask.university.entity.Student;
import com.testTask.university.exceptions.NotExistException;
import com.testTask.university.service.StudentService;
import com.testTask.university.utils.mappers.StudentsMapper;
import com.testTask.university.utils.validators.FieldValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository repository;
    private final GroupRepository groupRepository;
    private final StudentsMapper mapper;
    private final FieldValidator fieldValidator;


    @Override
    public List<StudentDto> getAllStudents() {
        return repository.findAll().stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<StudentDto> createNewStudent(StudentDto student) {
        fieldValidator.validateCreateOrUpdateStudent(student);
        Student studentToSave = setStudentFieldsToSave(student);
        repository.save(studentToSave);
        return getAllStudents();
    }

    @Transactional
    @Override
    public StudentDto editStudent(StudentDto student) {
        fieldValidator.validateCreateOrUpdateStudent(student);
        Student studentFromDb = getStudentFromDb(student);
        updateStudentFieldsToSave(student, studentFromDb);
        return mapper.convertToDto(repository.save(studentFromDb));
    }

    @Transactional
    @Override
    public String removeStudent(long studentId) {
        checkIfExistToDelete(studentId);
        repository.deleteById(studentId);
        return "Student with ID '" + studentId + "' has been successfully deleted.";
    }

    private void checkIfExistToDelete(long studentId) {
        if (!repository.existsById(studentId))
            throw new NotExistException("The Student with ID '" + studentId + "' not exist.");
    }

    private Group getGroupByNumber(StudentDto student) {
        Group groupFromDb = groupRepository.findByNumber(student.getGroupNumber());
        if (groupFromDb == null)
            throw new NotExistException("The group with number '" + student.getGroupNumber() + "' not exist.");
        return groupFromDb;
    }

    private Student getStudentFromDb(StudentDto student) {
        Student studentFromDb = repository.findById(student.getStudentId()).orElse(null);
        if (studentFromDb == null)
            throw new NotExistException("The Student with ID '" + student.getStudentId() + "' not exist.");
        return studentFromDb;
    }

    private void updateStudentFieldsToSave(StudentDto student, Student studentFromDb) {
        studentFromDb.setFirstName(student.getFirstName());
        studentFromDb.setLastName(student.getLastName());
        studentFromDb.setGroup(getGroupByNumber(student));
    }

    private Student setStudentFieldsToSave(StudentDto student) {
        Student studentToSave = mapper.convertToEntity(student);
        studentToSave.setGroup(getGroupByNumber(student));
        return studentToSave;
    }
}