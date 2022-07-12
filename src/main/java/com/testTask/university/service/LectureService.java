package com.testTask.university.service;

import com.testTask.university.dto.LectureDto;
import com.testTask.university.exceptions.NotExistException;

import java.util.List;

public interface LectureService {
    List<LectureDto> getAllLectures();

    List<LectureDto> createNewLecture(LectureDto lecture) throws Exception;

    LectureDto editLecture(LectureDto lecture) throws Exception;

    String removeLecture(long lectureId) throws NotExistException;
}
