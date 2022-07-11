package com.testTask.university.service;

import com.testTask.university.dto.LectureDto;
import com.testTask.university.exceptions.AlreadyExistException;
import com.testTask.university.exceptions.NotExistException;

import java.util.List;

public interface LectureService {
    List<LectureDto> getAllLectures();

    LectureDto getLectureById(long lectureId) throws NotExistException;

    List<LectureDto> createNewLecture(LectureDto lecture) throws AlreadyExistException, Exception;

    LectureDto editLecture(LectureDto lecture) throws NotExistException, Exception;

    String removeLecture(long lectureId) throws NotExistException;
}
