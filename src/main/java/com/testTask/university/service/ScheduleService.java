package com.testTask.university.service;

import com.testTask.university.dto.ScheduleDto;
import com.testTask.university.exceptions.NotExistException;
import java.util.List;

public interface ScheduleService {
    List<ScheduleDto> getAllSchedules();

    ScheduleDto getStudentSchedule(long studentId) throws NotExistException;

    List<ScheduleDto> createNewSchedule(ScheduleDto schedule);

    ScheduleDto editSchedule(ScheduleDto schedule) throws NotExistException;

    String removeSchedule(long scheduleId) throws NotExistException;
}
