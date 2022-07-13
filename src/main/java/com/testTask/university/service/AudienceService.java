package com.testTask.university.service;

import com.testTask.university.dto.AudienceDto;

import java.util.List;

public interface AudienceService {

    List<AudienceDto> getAllAudiences();

    List<AudienceDto> createNewAudience(AudienceDto audience);

    AudienceDto editAudience(AudienceDto audience);

    String removeAudience(long audienceId);
}
