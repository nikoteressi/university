package com.testTask.university.exceptions;

public class NotExistException extends RuntimeException{
    public NotExistException(String message) {
        super(message);
    }
}
