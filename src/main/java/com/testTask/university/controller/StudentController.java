package com.testTask.university.controller;

import com.testTask.university.dto.StudentDto;
import com.testTask.university.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/student")
public class StudentController {

    private final StudentService service;

    @GetMapping("/all-students")
    public List<StudentDto> getAllStudents() {
        return service.getAllStudents();
    }

    @PostMapping("/new-student")
    public List<StudentDto> createStudent(@RequestBody StudentDto student) {
        return service.createNewStudent(student);
    }

    @PutMapping("/edit-student")
    public StudentDto editStudent(@RequestBody StudentDto student) {
        return service.editStudent(student);
    }

    @DeleteMapping("/remove-student")
    public String removeStudent(@RequestParam long studentId) {
        return service.removeStudent(studentId);
    }
}
