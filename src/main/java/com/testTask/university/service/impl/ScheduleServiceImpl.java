package com.testTask.university.service.impl;

import com.testTask.university.dao.GroupRepository;
import com.testTask.university.dao.LectureRepository;
import com.testTask.university.dao.ScheduleRepository;
import com.testTask.university.dao.StudentRepository;
import com.testTask.university.dto.LectureDto;
import com.testTask.university.dto.ScheduleDto;
import com.testTask.university.entity.Group;
import com.testTask.university.entity.Schedule;
import com.testTask.university.entity.Student;
import com.testTask.university.exceptions.AlreadyExistException;
import com.testTask.university.exceptions.NotExistException;
import com.testTask.university.service.ScheduleService;
import com.testTask.university.utils.mappers.LectureMapper;
import com.testTask.university.utils.mappers.ScheduleMapper;
import com.testTask.university.utils.validators.FieldValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final StudentRepository studentRepository;
    private final ScheduleRepository scheduleRepository;
    private final LectureRepository lectureRepository;
    private final GroupRepository groupRepository;
    private final ScheduleMapper scheduleMapper;
    private final LectureMapper lectureMapper;
    private final FieldValidator fieldValidator;

    @Override
    public List<ScheduleDto> getAllSchedules() {
        List<ScheduleDto> schedules = scheduleRepository.findAll().stream()
                .map(scheduleMapper::convertToDto)
                .collect(Collectors.toList());
        schedules.forEach(f -> f.setLectures(getGroupLecturesByDate(f.getGroupNumber(), f.getDate())));
        return schedules;
    }

    @Override
    public ScheduleDto getStudentSchedule(long studentId, String date) throws Exception {
        fieldValidator.validateDate(date);
        Student student = findStudentById(studentId);
        Schedule scheduleFromDb = scheduleRepository.findByGroup_IdAndDate(student.getGroup().getId(), date);
        if (scheduleFromDb != null) {
            return convertNewScheduleToDto(scheduleFromDb);
        }
        Schedule schedule = new Schedule();
        schedule.setDate(date);
        schedule.setGroup(student.getGroup());
        return convertNewScheduleToDto(scheduleRepository.save(schedule));
    }

    @Transactional
    @Override
    public List<ScheduleDto> createNewSchedule(ScheduleDto schedule) throws Exception {
        fieldValidator.validateDate(schedule.getDate());
        fieldValidator.validateGroupNumber(schedule.getGroupNumber());
        checkIfExistByGroupNumberAndDate(schedule);
        Schedule scheduleToSave = scheduleMapper.convertToEntity(schedule);
        scheduleToSave.setGroup(findGroupByNumber(schedule));
        scheduleRepository.save(scheduleToSave);
        return getAllSchedules();
    }

    private void checkIfExistByGroupNumberAndDate(ScheduleDto schedule) throws AlreadyExistException {
        Schedule scheduleFromDb = scheduleRepository.findByGroup_NumberAndDate(schedule.getGroupNumber(), schedule.getDate());
        if (scheduleFromDb != null)
            throw  new AlreadyExistException("The schedule for group '" + schedule.getGroupNumber() + "' and for date '" + schedule.getDate() + "' already exists.");
    }

    @Transactional
    @Override
    public ScheduleDto editSchedule(ScheduleDto schedule) throws NotExistException {
        Schedule scheduleFromDb = findScheduleById(schedule);
        Schedule editedSchedule = scheduleMapper.convertToEntity(schedule);
        scheduleFromDb.setId(editedSchedule.getId());
        scheduleFromDb.setDate(editedSchedule.getDate());
        scheduleFromDb.setGroup(findGroupByNumber(schedule));
        return scheduleMapper.convertToDto(scheduleRepository.save(Objects.requireNonNull(scheduleFromDb)));
    }

    @Transactional
    @Override
    public String removeSchedule(long scheduleId) throws NotExistException {
        if (!scheduleRepository.existsById(scheduleId))
            throw new NotExistException("Schedule with ID: " + scheduleId + " not found.");
        scheduleRepository.deleteById(scheduleId);
        return "Schedule with ID: " + scheduleId + " has been successfully deleted.";
    }

    private ScheduleDto convertNewScheduleToDto(Schedule schedule) {
        ScheduleDto studentSchedule = scheduleMapper.convertToDto(schedule);
        studentSchedule.setLectures(getGroupLecturesByDate(schedule.getGroup().getNumber(), schedule.getDate()));
        return studentSchedule;
    }

    private List<LectureDto> getGroupLecturesByDate(int groupNumber, String date) {
        return lectureRepository.findAllByGroup_NumberAndAndDate(groupNumber, date).stream()
                .map(lectureMapper::convertToDto)
                .collect(Collectors.toList());
    }

    private Group findGroupByNumber(ScheduleDto schedule) throws NotExistException {
        Group group = groupRepository.findByNumber(schedule.getGroupNumber());
        if (group == null)
            throw new NotExistException("Group with number '" + schedule.getScheduleId() + "' not found.");
        return group;
    }

    private Student findStudentById(long studentId) throws NotExistException {
        Student student = studentRepository.findById(studentId).orElse(null);
        if (student == null)
            throw new NotExistException("Student with ID: " + studentId + " not found.");
        return student;
    }

    private Schedule findScheduleById(ScheduleDto schedule) throws NotExistException {
        Schedule scheduleFromDb = scheduleRepository.findById(schedule.getScheduleId()).orElse(null);
        if (scheduleFromDb == null)
            throw new NotExistException("Schedule with ID: " + schedule.getScheduleId() + " not found.");
        return scheduleFromDb;
    }
}
