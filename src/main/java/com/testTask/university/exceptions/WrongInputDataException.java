package com.testTask.university.exceptions;

public class WrongInputDataException extends RuntimeException{
    public WrongInputDataException(String message) {
        super(message);
    }
}
