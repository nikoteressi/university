package com.testTask.university.service.impl;

import com.testTask.university.dao.AudienceRepository;
import com.testTask.university.dao.GroupRepository;
import com.testTask.university.dao.LectureRepository;
import com.testTask.university.dto.LectureDto;
import com.testTask.university.entity.Audience;
import com.testTask.university.entity.Group;
import com.testTask.university.entity.Lecture;
import com.testTask.university.exceptions.AlreadyExistException;
import com.testTask.university.exceptions.NotExistException;
import com.testTask.university.service.LectureService;
import com.testTask.university.utils.mappers.LectureMapper;
import com.testTask.university.utils.validators.FieldValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class LectureServiceImpl implements LectureService {

    private final LectureRepository repository;
    private final GroupRepository groupRepository;
    private final AudienceRepository audienceRepository;
    private final LectureMapper mapper;
    private final FieldValidator fieldValidator;


    @Override
    public List<LectureDto> getAllLectures() {
        return repository.findAll().stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<LectureDto> createNewLecture(LectureDto lecture) throws Exception {
        fieldValidator.validateCreateOrUpdateLecture(lecture);
        checkIfExistByNameAndDate(lecture.getLectureName(), lecture.getLectureDate());
        repository.save(setLectureFieldsToSave(lecture));
        return getAllLectures();
    }

    @Transactional
    @Override
    public LectureDto editLecture(LectureDto lecture) throws Exception {
        fieldValidator.validateCreateOrUpdateLecture(lecture);
        Lecture lectureFromDb = findLectureById(lecture.getLectureId());
        updateLectureFields(lecture, lectureFromDb);
        return mapper.convertToDto(repository.save(lectureFromDb));
    }

    @Transactional
    @Override
    public String removeLecture(long lectureId) throws NotExistException {
        checkIfExistToRemove(lectureId);
        repository.deleteById(lectureId);
        return "Lecture with ID '" + lectureId + "' has been successfully deleted.";
    }

    private void checkIfExistToRemove(long lectureId) throws NotExistException {
        if (!repository.existsById(lectureId))
            throw new NotExistException("Lecture with ID '" + lectureId + "' not exist.");
    }

    private Lecture setLectureFieldsToSave(LectureDto lecture) throws NotExistException {
        Lecture lectureToSave = mapper.convertToEntity(lecture);
        lectureToSave.setGroup(getGroupByNumber(lecture.getGroupNumber()));
        lectureToSave.setAudience(getAudienceByNumber(lecture.getAudienceNumber()));
        return lectureToSave;
    }

    private void updateLectureFields(LectureDto lecture, Lecture lectureFromDb) throws NotExistException {
        lectureFromDb.setDate(lecture.getLectureDate());
        lectureFromDb.setName(lecture.getLectureName());
        lectureFromDb.setGroup(getGroupByNumber(lecture.getGroupNumber()));
        lectureFromDb.setAudience(getAudienceByNumber(lecture.getAudienceNumber()));
    }

    private void checkIfExistByNameAndDate(String lectureName, String lectureDate) throws AlreadyExistException {
        Lecture lectureFromDb = repository.findByNameAndDate(lectureName, lectureDate);
        if (lectureFromDb != null)
            throw new AlreadyExistException("Lecture with name and date '" + lectureName + " " + lectureDate + "' already exist.");
    }

    private Lecture findLectureById(long id) throws NotExistException {
        Lecture lectureFromDb = repository.findById(id).orElse(null);
        if (lectureFromDb == null)
            throw new NotExistException("Lecture with ID '" + id + "' not exist.");
        return lectureFromDb;
    }

    private Audience getAudienceByNumber(int audienceNumber) throws NotExistException {
        Audience audience = audienceRepository.findByNumber(audienceNumber);
        if (audience == null)
            throw new NotExistException("Audience with number '" + audienceNumber + "' not exist.");
        return audience;
    }

    private Group getGroupByNumber(int groupNumber) throws NotExistException {
        Group group = groupRepository.findByNumber(groupNumber);
        if (group == null)
            throw new NotExistException("Group with number '" + groupNumber + "' not exist.");
        return group;
    }
}
