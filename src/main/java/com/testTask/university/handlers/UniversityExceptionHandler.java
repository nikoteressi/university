package com.testTask.university.handlers;

import com.testTask.university.dto.exception.ExceptionDto;
import com.testTask.university.exceptions.NotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class UniversityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotExistException.class)
    protected ResponseEntity<ExceptionDto> handleScheduleNotExistException(NotExistException ex) {
        return new ResponseEntity<>(new ExceptionDto(ex.getMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
    }
}
