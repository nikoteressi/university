package com.testTask.university.controller;

import com.testTask.university.dto.ScheduleDto;
import com.testTask.university.exceptions.AlreadyExistException;
import com.testTask.university.exceptions.NotExistException;
import com.testTask.university.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping("/all-schedules")
    public List<ScheduleDto> getAllSchedules() {
        return scheduleService.getAllSchedules();
    }

    @GetMapping("/student-schedule")
    public ScheduleDto getStudentSchedule(@RequestParam("studentId") long studentId, @RequestParam("scheduleDate") String scheduleDate) throws Exception {
        return scheduleService.getStudentSchedule(studentId, scheduleDate);
    }

    @PostMapping("/new-schedule")
    public List<ScheduleDto> addSchedule(@RequestBody ScheduleDto schedule) throws AlreadyExistException {
        return scheduleService.createNewSchedule(schedule);
    }

    @PutMapping("/edit-schedule")
    public ScheduleDto editSchedule(@RequestBody ScheduleDto schedule) throws NotExistException {
        return scheduleService.editSchedule(schedule);
    }

    @DeleteMapping("/remove-schedule")
    public String removeSchedule(@RequestParam long scheduleId) throws NotExistException {
        return scheduleService.removeSchedule(scheduleId);
    }
}
