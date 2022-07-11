package com.testTask.university.service;

import com.testTask.university.dto.ScheduleDto;
import com.testTask.university.exceptions.AlreadyExistException;
import com.testTask.university.exceptions.NotExistException;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public interface ScheduleService {
    List<ScheduleDto> getAllSchedules();

    ScheduleDto getStudentSchedule(long studentId, String date) throws NotExistException, Exception;

    List<ScheduleDto> createNewSchedule(ScheduleDto schedule) throws AlreadyExistException;

    ScheduleDto editSchedule(ScheduleDto schedule) throws NotExistException;

    String removeSchedule(long scheduleId) throws NotExistException;
}
