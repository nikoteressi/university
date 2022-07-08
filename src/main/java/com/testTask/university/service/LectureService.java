package com.testTask.university.service;

import com.testTask.university.dto.LectureDto;
import com.testTask.university.exceptions.NotExistException;

import java.util.List;

public interface LectureService {
    List<LectureDto> getAllLectures();

    LectureDto getLectureById(long lectureId) throws NotExistException;

    List<LectureDto> createNewLecture(LectureDto lecture);

    LectureDto editLecture(LectureDto lecture) throws NotExistException;

    String removeLecture(long lectureId) throws NotExistException;
}
