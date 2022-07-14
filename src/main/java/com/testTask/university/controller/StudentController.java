package com.testTask.university.controller;

import com.testTask.university.dto.StudentDto;
import com.testTask.university.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService service;

    @GetMapping
    public List<StudentDto> getAllStudents() {
        return service.getAllStudents();
    }

    @PostMapping
    public List<StudentDto> createStudent(@RequestBody StudentDto student) {
        return service.createNewStudent(student);
    }

    @PutMapping
    public StudentDto editStudent(@RequestBody StudentDto student) {
        return service.editStudent(student);
    }

    @DeleteMapping("/{studentId}")
    public String removeStudent(@PathVariable long studentId) {
        return service.removeStudent(studentId);
    }
}
