package com.testTask.university.service;

import com.testTask.university.dto.ScheduleDto;
import com.testTask.university.exceptions.NotExistException;

import java.util.List;

public interface ScheduleService {
    List<ScheduleDto> getAllSchedules();

    ScheduleDto getStudentSchedule(long studentId, String date);

    List<ScheduleDto> createNewSchedule(ScheduleDto schedule);

    ScheduleDto editSchedule(ScheduleDto schedule);

    String removeSchedule(long scheduleId);
}
