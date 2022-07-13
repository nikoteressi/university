package com.testTask.university.controller;

import com.testTask.university.dto.LectureDto;
import com.testTask.university.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/lecture")
public class LectureController {

    private final LectureService service;

    @GetMapping("/all-lectures")
    public List<LectureDto> getAllLectures() {
        return service.getAllLectures();
    }

    @PostMapping("/new-lecture")
    public List<LectureDto> createLecture(@RequestBody LectureDto lecture) {
        return service.createNewLecture(lecture);
    }

    @PutMapping("/edit-lecture")
    public LectureDto editLecture(@RequestBody LectureDto lecture) {
        return service.editLecture(lecture);
    }

    @DeleteMapping("/remove-lecture")
    public String removeLecture(@RequestParam long lectureId) {
        return service.removeLecture(lectureId);
    }
}
