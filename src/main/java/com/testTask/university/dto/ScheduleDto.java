package com.testTask.university.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDto {
    private long scheduleId;
    private String date;
    private int groupNumber;
    private List<LectureDto> lectures;
}
