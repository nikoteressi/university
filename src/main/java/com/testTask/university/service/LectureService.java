package com.testTask.university.service;

import com.testTask.university.dto.LectureDto;
import com.testTask.university.exceptions.NotExistException;

import java.util.List;

public interface LectureService {
    List<LectureDto> getAllLectures();

    List<LectureDto> createNewLecture(LectureDto lecture);

    LectureDto editLecture(LectureDto lecture);

    String removeLecture(long lectureId);
}
