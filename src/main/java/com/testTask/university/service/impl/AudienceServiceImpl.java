package com.testTask.university.service.impl;

import com.testTask.university.dao.AudienceRepository;
import com.testTask.university.dto.AudienceDto;
import com.testTask.university.entity.Audience;
import com.testTask.university.exceptions.AlreadyExistException;
import com.testTask.university.exceptions.NotExistException;
import com.testTask.university.service.AudienceService;
import com.testTask.university.utils.mappers.AudienceMapper;
import com.testTask.university.utils.validators.FieldValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AudienceServiceImpl implements AudienceService {

    private final AudienceRepository repository;
    private final AudienceMapper mapper;
    private final FieldValidator fieldValidator;

    @Override
    public List<AudienceDto> getAllAudiences() {
        return repository.findAll().stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<AudienceDto> createNewAudience(AudienceDto audience) {
        fieldValidator.validateAudienceNumber(audience.getNumber());
        checkIfExistByNumber(audience);
        repository.save(mapper.convertToEntity(audience));
        return getAllAudiences();
    }

    @Transactional
    @Override
    public AudienceDto editAudience(AudienceDto audience) {
        fieldValidator.validateAudienceNumber(audience.getNumber());
        Audience audienceFromDb = findAudienceByNumber(audience);
        Audience audienceToSave = mapper.convertToEntity(audience);
        audienceFromDb.setNumber(audienceToSave.getNumber());
        return mapper.convertToDto(repository.save(audienceFromDb));
    }

    @Transactional
    @Override
    public String removeAudience(long audienceId) {
        checkIfExistToRemove(audienceId);
        repository.deleteById(audienceId);
        return "Audience with ID: " + audienceId + " has been successfully deleted.";
    }

    private void checkIfExistByNumber(AudienceDto audience) {
        if (repository.existsByNumber(audience.getNumber()))
            throw new AlreadyExistException("The audience with number '" + audience.getNumber() + "' already exist.");
    }

    private void checkIfExistToRemove(long id) {
        if (!repository.existsById(id))
            throw new NotExistException("The audience with id '" + id + "' not exist.");
    }

    private Audience findAudienceByNumber(AudienceDto audience) {
        Audience audienceFromDb = repository.findByNumber(audience.getNumber());
        if (audienceFromDb == null)
            throw new NotExistException("The audience with number '" + audience.getNumber() + "' not exist.");
        return audienceFromDb;
    }
}
