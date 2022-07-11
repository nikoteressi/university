package com.testTask.university.service.impl;

import com.testTask.university.dao.AudienceRepository;
import com.testTask.university.dto.AudienceDto;
import com.testTask.university.entity.Audience;
import com.testTask.university.exceptions.AlreadyExistException;
import com.testTask.university.exceptions.NotExistException;
import com.testTask.university.exceptions.WrongInputDataException;
import com.testTask.university.mappers.AudienceMapper;
import com.testTask.university.service.AudienceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.ignoreCase;

@RequiredArgsConstructor
@Service
public class AudienceServiceImpl implements AudienceService {

    private final AudienceRepository repository;
    private final AudienceMapper mapper;

    @Override
    public List<AudienceDto> getAllAudiences() {
        return repository.findAll().stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AudienceDto> createNewAudience(AudienceDto audience) throws Exception {
        Audience audienceToSave = mapper.convertToEntity(audience);
        if (validateCreateAudience(audienceToSave)) repository.save(audienceToSave);
        return getAllAudiences();
    }

    @Override
    public AudienceDto editAudience(AudienceDto audience) throws Exception {
        Audience audienceToSave = mapper.convertToEntity(audience);
        Audience updatedAudience = null;
        if (validateUpdateAudience(audienceToSave)) {
            Audience audienceFromDb = repository.findById(audienceToSave.getId()).orElse(null);
            Objects.requireNonNull(audienceFromDb).setNumber(audienceToSave.getNumber());
            audienceFromDb.setLectures(audienceToSave.getLectures());
            updatedAudience = repository.save(audienceFromDb);
        }
        return mapper.convertToDto(updatedAudience);
    }

    @Override
    public String removeAudience(long audienceId) throws NotExistException {
        if (validateRemoveAudience(audienceId)) repository.deleteById(audienceId);
        return "Audience with ID: " + audienceId + " has been successfully deleted.";
    }

    private boolean validateRemoveAudience(long id) throws NotExistException {
        if (!checkIfExistById(id))
            throw new NotExistException("The audience with id \"" + id + "\" not exist.");

        return true;
    }

    private boolean validateCreateAudience(Audience audience) throws Exception {
        if (!checkAudienceNumber(audience.getNumber()))
            throw new WrongInputDataException("The audience number is wrong. Must be 1-1000. Received audience number: " + audience.getNumber());

        if (!checkIfExistByNumber(audience))
            throw new AlreadyExistException("The audience with number \"" + audience.getNumber() + "\" already exist.");

        return true;
    }

    private boolean validateUpdateAudience(Audience audience) throws Exception {
        if (!checkIfExistById(audience.getId()))
            throw new NotExistException("The audience with number \"" + audience.getNumber() + "\" not exist.");

        if (!checkAudienceNumber(audience.getNumber()))
            throw new AlreadyExistException("The audience with number \"" + audience.getNumber() + "\" already exist.");
        return true;
    }

    private boolean checkAudienceNumber(int number) {
        return String.valueOf(number).matches("^[1-9][0 -9]{1,3}$");
    }

    private boolean checkIfExistByNumber(Audience audience) {
        ExampleMatcher nameMatcher = ExampleMatcher.matching()
                .withIgnorePaths("id")
                .withMatcher("number", ignoreCase());
        Example<Audience> example = Example.of(audience, nameMatcher);
        return repository.exists(example);
    }

    private boolean checkIfExistById(long id) {
        return repository.existsById(id);
    }
}
