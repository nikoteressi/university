package com.testTask.university.service.impl;

import com.testTask.university.dao.ScheduleRepository;
import com.testTask.university.dao.StudentRepository;
import com.testTask.university.dto.ScheduleDto;
import com.testTask.university.entity.Schedule;
import com.testTask.university.entity.Student;
import com.testTask.university.exceptions.NotExistException;
import com.testTask.university.mappers.Mapper;
import com.testTask.university.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final StudentRepository studentRepository;
    private final ScheduleRepository scheduleRepository;
    private final Mapper<ScheduleDto, Schedule> scheduleMapper;

    @Override
    public List<ScheduleDto> getAllSchedules() {
        return scheduleRepository.findAll().stream()
                .map(scheduleMapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ScheduleDto getStudentSchedule(long studentId) throws NotExistException {
        Student student = studentRepository.findById(studentId).orElse(null);
        if (student == null) throw new NotExistException("Student with ID: " + studentId + " not found.");
        return scheduleMapper.convertToDto(student.getSchedule());
    }

    @Transactional
    @Override
    public List<ScheduleDto> createNewSchedule(ScheduleDto schedule) {
        Schedule scheduleToSave = scheduleMapper.convertToEntity(schedule);
        scheduleRepository.save(scheduleToSave);
        return getAllSchedules();
    }

    @Transactional
    @Override
    public ScheduleDto editSchedule(ScheduleDto schedule) throws NotExistException {
        Schedule scheduleFromDb = scheduleRepository.findById(schedule.getScheduleId()).orElse(null);
        Schedule editedSchedule = scheduleMapper.convertToEntity(schedule);
        if (scheduleFromDb == null) throw new NotExistException("Schedule with ID: " + schedule.getScheduleId() + " not found.");
        scheduleFromDb.setLectures(editedSchedule.getLectures());
        return scheduleMapper.convertToDto(scheduleRepository.save(scheduleFromDb));
    }

    @Override
    public String removeSchedule(long scheduleId) throws NotExistException {
        if (!scheduleRepository.existsById(scheduleId)) throw new NotExistException("Schedule with ID: " + scheduleId + " not found.");
        scheduleRepository.deleteById(scheduleId);
        return "Schedule with ID: " + scheduleId + " has been successfully deleted.";
    }
}
