package com.testTask.university.service.impl;

import com.testTask.university.dao.LectureRepository;
import com.testTask.university.dto.GroupDto;
import com.testTask.university.dto.LectureDto;
import com.testTask.university.entity.Group;
import com.testTask.university.entity.Lecture;
import com.testTask.university.exceptions.AlreadyExistException;
import com.testTask.university.exceptions.NotExistException;
import com.testTask.university.exceptions.WrongInputDataException;
import com.testTask.university.mappers.LectureMapper;
import com.testTask.university.service.LectureService;
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
public class LectureServiceImpl implements LectureService {

    private final LectureRepository repository;
    private final LectureMapper mapper;


    @Override
    public List<LectureDto> getAllLectures() {
        return repository.findAll().stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public LectureDto getLectureById(long lectureId) throws NotExistException {
        Lecture lecture = null;
        if (validateRemoveLecture(lectureId)) {
            lecture = repository.findById(lectureId).orElse(null);
        }
        return mapper.convertToDto(lecture);
    }

    @Override
    public List<LectureDto> createNewLecture(LectureDto lecture) throws Exception {
        Lecture lectureToSave = mapper.convertToEntity(lecture);
        if (validateCreateLecture(lectureToSave)) repository.save(lectureToSave);
        return getAllLectures();
    }

    @Override
    public LectureDto editLecture(LectureDto lecture) throws Exception {
        Lecture lectureFromDb = null;
        if (validateUpdateLecture(lecture)) {
            lectureFromDb = repository.findById(lecture.getLectureId()).orElse(null);
            Objects.requireNonNull(lectureFromDb).setDate(lecture.getLectureDate());
            repository.save(lectureFromDb);
        }
        return mapper.convertToDto(lectureFromDb);
    }

    @Override
    public String removeLecture(long lectureId) throws NotExistException {
        if (validateRemoveLecture(lectureId)) repository.deleteById(lectureId);
        return "Lecture with ID: " + lectureId + " has been successfully deleted.";
    }

    private boolean validateCreateLecture(Lecture lecture) throws Exception {

        if (checkIfExistByName(lecture))
            throw new AlreadyExistException("Lecture with name \"" + lecture.getName() + "\" already exist.");
        if (validateName(lecture.getName()))
            throw new WrongInputDataException("The lecture name is wrong! Must not be empty. Received lecture name: " + lecture.getName());
        return true;
    }

    private boolean validateUpdateLecture(LectureDto lecture) throws Exception {
        if (!checkIfExistById(lecture.getLectureId()))
            throw new NotExistException("Lecture with ID: " + lecture.getLectureId() + " not exist.");
        if (validateName(lecture.getLectureName()))
            throw new WrongInputDataException("The lecture name is wrong! Must not be empty. Received lecture name: " + lecture.getLectureName());
        return true;
    }

    private boolean validateRemoveLecture(long id) throws NotExistException {
        if (!checkIfExistById(id))
            throw new NotExistException("Lecture with ID: " + id + " not exist.");
        return true;
    }

    private boolean validateName(String name) {
        return !name.isBlank();
    }

    private boolean checkIfExistByName(Lecture lecture) {
        ExampleMatcher nameMatcher = ExampleMatcher.matching()
                .withIgnorePaths("id")
                .withMatcher("lectureName", ignoreCase());
        Example<Lecture> example = Example.of(lecture, nameMatcher);
        return repository.exists(example);
    }

    private boolean checkIfExistById(long id) {
        return repository.existsById(id);
    }
}
