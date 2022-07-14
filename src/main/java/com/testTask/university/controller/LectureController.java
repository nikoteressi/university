package com.testTask.university.controller;

import com.testTask.university.dto.LectureDto;
import com.testTask.university.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/lectures")
public class LectureController {

    private final LectureService service;

    @GetMapping
    public List<LectureDto> getAllLectures() {
        return service.getAllLectures();
    }

    @PostMapping
    public List<LectureDto> createLecture(@RequestBody LectureDto lecture) {
        return service.createNewLecture(lecture);
    }

    @PutMapping
    public LectureDto editLecture(@RequestBody LectureDto lecture) {
        return service.editLecture(lecture);
    }

    @DeleteMapping("/{lectureId}")
    public String removeLecture(@PathVariable long lectureId) {
        return service.removeLecture(lectureId);
    }
}
