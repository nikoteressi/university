package com.testTask.university.controller;

import com.testTask.university.dto.ScheduleDto;
import com.testTask.university.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping
    public List<ScheduleDto> getAllSchedules() {
        return scheduleService.getAllSchedules();
    }

    @GetMapping("/student/{studentId}/date/{scheduleDate}")
    public ScheduleDto getStudentSchedule(@PathVariable("studentId") long studentId,
                                          @PathVariable("scheduleDate") String scheduleDate) {
        return scheduleService.getStudentSchedule(studentId, scheduleDate);
    }

    @PostMapping
    public List<ScheduleDto> addSchedule(@RequestBody ScheduleDto schedule) {
        return scheduleService.createNewSchedule(schedule);
    }

    @PutMapping
    public ScheduleDto editSchedule(@RequestBody ScheduleDto schedule) {
        return scheduleService.editSchedule(schedule);
    }

    @DeleteMapping("/{scheduleId}")
    public String removeSchedule(@PathVariable long scheduleId) {
        return scheduleService.removeSchedule(scheduleId);
    }
}
