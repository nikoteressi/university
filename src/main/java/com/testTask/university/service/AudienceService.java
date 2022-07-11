package com.testTask.university.service;

import com.testTask.university.dto.AudienceDto;
import com.testTask.university.exceptions.NotExistException;

import java.util.List;

public interface AudienceService {

    List<AudienceDto> getAllAudiences();

    List<AudienceDto> createNewAudience(AudienceDto audience) throws Exception;

    AudienceDto editAudience(AudienceDto audience) throws Exception;

    String removeAudience(long audienceId) throws NotExistException;
}
