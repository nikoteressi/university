package com.testTask.university.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LectureDto {
    private long lectureId;
    private String lectureName;
    private String lectureDate;
    private int audienceNumber;
    private int groupNumber;
}
