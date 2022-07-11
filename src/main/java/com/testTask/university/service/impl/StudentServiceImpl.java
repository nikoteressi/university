package com.testTask.university.service.impl;

import com.testTask.university.dao.GroupRepository;
import com.testTask.university.dao.StudentRepository;
import com.testTask.university.dto.StudentDto;
import com.testTask.university.entity.Group;
import com.testTask.university.entity.Student;
import com.testTask.university.exceptions.AlreadyExistException;
import com.testTask.university.exceptions.NotExistException;
import com.testTask.university.exceptions.WrongInputDataException;
import com.testTask.university.mappers.StudentsMapper;
import com.testTask.university.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.ignoreCase;

@RequiredArgsConstructor
@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository repository;
    private final GroupRepository groupRepository;
    private final StudentsMapper mapper;


    @Override
    public List<StudentDto> getAllStudents() {
        return repository.findAll().stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentDto> createNewStudent(StudentDto student) throws Exception {
        if (validateCreateStudent(student)) {
            Student studentToSave = mapper.convertToEntity(student);
            repository.save(studentToSave);
        }
        return getAllStudents();
    }

    @Override
    public StudentDto editStudent(StudentDto student) throws Exception {
        Student studentFromDb = null;
        Group group = groupRepository.findById(student.getGroupId()).orElse(null);
        if (validateUpdateStudent(student)) {
            studentFromDb = repository.findById(student.getStudentId()).orElse(null);
            Objects.requireNonNull(studentFromDb).setFirstName(student.getFirstName());
            studentFromDb.setLastName(student.getLastName());
            studentFromDb.setGroup(group);
        }
        return mapper.convertToDto(repository.save(Objects.requireNonNull(studentFromDb)));
    }

    @Override
    public String removeStudent(long studentId) throws NotExistException {
        if (validateRemoveStudent(studentId)) repository.deleteById(studentId);
        return "Student with ID: " + studentId + " has been successfully deleted.";
    }

    private boolean validateCreateStudent(StudentDto student) throws Exception {
        if (checkIfExist(mapper.convertToEntity(student)))
            throw new AlreadyExistException("Student with name \"" + student.getFirstName() + " " + student.getLastName() + "\" already exist.");
        if (validateUserName(student))
            throw new WrongInputDataException("Student name is wrong!" + "Received data: First name: " + student.getFirstName() + " Last name: " + student.getFirstName());
        if (validateGroupId(student))
            throw new WrongInputDataException("Group ID is Wrong. Must be positive" + "Received Group ID: " + student.getGroupId());

        return true;
    }

    private boolean validateUpdateStudent(StudentDto student) throws Exception {
        if (!checkIfExistById(student.getStudentId()))
            throw new NotExistException("Student with ID: " + student.getStudentId() + " not found.");
        if (validateUserName(student))
            throw new WrongInputDataException("Student name is wrong!" + "Received data: First name: " + student.getFirstName() + " Last name: " + student.getFirstName());
        if (validateGroupId(student))
            throw new WrongInputDataException("Group ID is Wrong. Must be positive" + "Received Group ID: " + student.getGroupId());

        return true;
    }

    private boolean validateRemoveStudent(long id) throws NotExistException {
        if (!checkIfExistById(id))
            throw new NotExistException("Student with ID: " + id + " not found.");

        return true;
    }

    private boolean checkIfExist(Student student) {
        ExampleMatcher nameMatcher = ExampleMatcher.matching()
                .withIgnorePaths("id")
                .withMatcher("firstName", ignoreCase())
                .withMatcher("lastName", ignoreCase());
        Example<Student> example = Example.of(student, nameMatcher);
        return repository.exists(example);
    }

    private boolean checkIfExistById(long id) {
        return repository.existsById(id);
    }

    private boolean validateUserName(StudentDto student) {
        boolean isCorrectFirstName = student.getFirstName().matches("^[а-яА-Я]{25}|[a-zA-Z]{25}$");
        boolean isCorrectLastName = student.getLastName().matches("^[а-яА-Я]{50}|[a-zA-Z]{50}$");
        return isCorrectFirstName && isCorrectLastName;
    }

    private boolean validateGroupId(StudentDto student) {
        return student.getGroupId() >= 0;
    }
}
