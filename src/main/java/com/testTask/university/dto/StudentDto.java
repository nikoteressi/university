package com.testTask.university.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {
    private long StudentId;
    private String firstName;
    private String LastName;
    private long scheduleId;
}
