package com.testTask.university.service.impl;

import com.testTask.university.dao.LectureRepository;
import com.testTask.university.dto.LectureDto;
import com.testTask.university.entity.Lecture;
import com.testTask.university.exceptions.NotExistException;
import com.testTask.university.mappers.Mapper;
import com.testTask.university.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class LectureServiceImpl implements LectureService {

    private final LectureRepository repository;
    private final Mapper<LectureDto, Lecture> mapper;


    @Override
    public List<LectureDto> getAllLectures() {
        return repository.findAll().stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public LectureDto getLectureById(long lectureId) throws NotExistException {
        Lecture lecture = repository.findById(lectureId).orElse(null);
        if (lecture == null) throw new NotExistException("Lecture with ID: " + lectureId + " not exist.");
        return mapper.convertToDto(lecture);
    }

    @Override
    public List<LectureDto> createNewLecture(LectureDto lecture) {
        repository.save(mapper.convertToEntity(lecture));
        return getAllLectures();
    }

    @Override
    public LectureDto editLecture(LectureDto lecture) throws NotExistException {
        Lecture lectureFromDb = repository.findById(lecture.getLectureId()).orElse(null);
        if (lectureFromDb == null) throw new NotExistException("Lecture with ID: " + lecture.getLectureId() + " not exist.");
        return null;
    }

    @Override
    public String removeLecture(long lectureId) throws NotExistException {
        Lecture lectureFromDb = repository.findById(lectureId).orElse(null);
        if (lectureFromDb == null) throw new NotExistException("Lecture with ID: " + lectureId + " not exist.");
        repository.deleteById(lectureId);
        return "Lecture with ID: " + lectureId + " has been successfully deleted.";
    }
}
