package com.testTask.university.utils.validators;

import com.testTask.university.dto.LectureDto;
import com.testTask.university.dto.StudentDto;
import com.testTask.university.exceptions.WrongInputDataException;
import com.testTask.university.utils.Constants;
import org.springframework.stereotype.Component;

@Component
public class FieldValidator {

    public void validateCreateOrUpdateLecture(LectureDto lecture) {
        validateLectureName(lecture.getLectureName());
        validateDate(lecture.getLectureDate());
        validateAudienceNumber(lecture.getAudienceNumber());
        validateGroupNumber(lecture.getGroupNumber());
    }

    public void validateCreateOrUpdateStudent(StudentDto studentDto) {
        validateName(studentDto.getFirstName(), studentDto.getLastName());
        validateGroupNumber(studentDto.getGroupNumber());
    }

    public void validateDate(String date) throws WrongInputDataException {
        if (!date.matches(Constants.DATE_PATTERN_REGEX))
            throw new WrongInputDataException("The received date '" + date + "' is wrong. Must be YYYY-MM-DD.");
    }

    public void validateGroupNumber(int groupNumber) {
        if (groupNumber < 1 || groupNumber > 1000)
            throw new WrongInputDataException("The received group number '" + groupNumber + "'  is wrong. Must be in range [1-100]");
    }

    public void validateAudienceNumber(int audienceNumber) {
        if (audienceNumber < 1 || audienceNumber > 1000)
            throw new WrongInputDataException("The received audience number '" + audienceNumber + "'  is wrong. Must be in range [1-1000]");
    }

    public void validateName(String firstName, String lastName) {
        if ((firstName.isBlank() || lastName.isBlank())
                || (firstName.length() > 25 || lastName.length() > 50)
                || (firstName.length() < 3 || lastName.length() < 5))
            throw new WrongInputDataException("The received Student name '" + firstName + " " + lastName + "'  is wrong.");
    }

    public void validateLectureName(String lectureName) {
        if (lectureName.length() > 255 || lectureName.length() < 5 && lectureName.isBlank())
            throw new WrongInputDataException("The received lecture name '" + lectureName + "' is wrong. Length must be in range [5-255]");
    }
}
