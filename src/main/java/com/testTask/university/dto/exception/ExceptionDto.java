package com.testTask.university.dto.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ExceptionDto {
    private String errorMessage;
    private int statusCode;
}
