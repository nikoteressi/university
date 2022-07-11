package com.testTask.university.service.impl;

import com.testTask.university.dao.ScheduleRepository;
import com.testTask.university.dao.StudentRepository;
import com.testTask.university.dto.ScheduleDto;
import com.testTask.university.entity.Schedule;
import com.testTask.university.entity.Student;
import com.testTask.university.exceptions.NotExistException;
import com.testTask.university.mappers.GroupMapper;
import com.testTask.university.mappers.ScheduleMapper;
import com.testTask.university.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.ignoreCase;

@RequiredArgsConstructor
@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final StudentRepository studentRepository;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleMapper scheduleMapper;
    private final GroupMapper groupMapper;

    @Override
    public List<ScheduleDto> getAllSchedules() {
        return scheduleRepository.findAll().stream()
                .map(scheduleMapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ScheduleDto getStudentSchedule(long studentId, String date) throws Exception {
        Student student = studentRepository.findById(studentId).orElse(null);
        if (student == null)
            throw new NotExistException("Student with ID: " + studentId + " not found.");

        ScheduleDto studentSchedule = new ScheduleDto();
        studentSchedule.setGroup_id(student.getGroup().getId());
        studentSchedule.setDate(date);
        List<ScheduleDto> schedules = createNewSchedule(studentSchedule);
        return schedules.stream().filter(f -> f.getDate().equals(date)).findFirst().orElse(null);
    }

    @Transactional
    @Override
    public List<ScheduleDto> createNewSchedule(ScheduleDto schedule) {
        Schedule scheduleToSave = scheduleMapper.convertToEntity(schedule);
        if (checkIfExistByDate(scheduleToSave)) return getAllSchedules();

        scheduleRepository.save(scheduleToSave);
        return getAllSchedules();
    }

    @Transactional
    @Override
    public ScheduleDto editSchedule(ScheduleDto schedule) throws NotExistException {
        Schedule scheduleFromDb = null;
        if (checkIfExistById(schedule.getScheduleId())) {
            scheduleFromDb = scheduleRepository.findById(schedule.getScheduleId()).orElse(null);
            Schedule editedSchedule = scheduleMapper.convertToEntity(schedule);
            Objects.requireNonNull(scheduleFromDb).setId(editedSchedule.getId());
            scheduleFromDb.setDate(editedSchedule.getDate());
            scheduleFromDb.setGroup(editedSchedule.getGroup());
        }
        return scheduleMapper.convertToDto(scheduleRepository.save(Objects.requireNonNull(scheduleFromDb)));
    }

    @Override
    public String removeSchedule(long scheduleId) throws NotExistException {
        if (checkIfExistById(scheduleId)) scheduleRepository.deleteById(scheduleId);
        return "Schedule with ID: " + scheduleId + " has been successfully deleted.";
    }

    private boolean checkIfExistByDate(Schedule scheduleToSave) {
        return scheduleRepository.existsByDate(scheduleToSave.getDate());
    }

    private boolean checkIfExistById(long id) throws NotExistException {
        if (scheduleRepository.existsById(id))
            throw new NotExistException("Schedule with ID: " + id + " not found.");
        return true;
    }
}
