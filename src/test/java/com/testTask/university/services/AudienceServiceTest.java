package com.testTask.university.services;

import com.testTask.university.dao.AudienceRepository;
import com.testTask.university.dto.AudienceDto;
import com.testTask.university.entity.Audience;
import com.testTask.university.exceptions.AlreadyExistException;
import com.testTask.university.exceptions.NotExistException;
import com.testTask.university.exceptions.WrongInputDataException;
import com.testTask.university.service.AudienceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AudienceServiceTest {

    @Autowired
    private AudienceService service;

    @MockBean
    private AudienceRepository repository;

    @Test
    void shouldReturnListWithAllAudiences() {
        List<Audience> audiences = new ArrayList<>();
        audiences.add(new Audience(1L, 1, new ArrayList<>()));
        when(repository.findAll()).thenReturn(audiences);
        List<AudienceDto> audiencesFromDb = service.getAllAudiences();
        assertEquals(1, audiencesFromDb.size());
        assertEquals(1, audiences.get(0).getNumber());
    }

    @Test
    void shouldReturnEmptyListIfThereIsNoAudiences() {
        List<Audience> audiences = new ArrayList<>();
        when(repository.findAll()).thenReturn(audiences);
        List<AudienceDto> audiencesFromDb = service.getAllAudiences();
        assertTrue(audiencesFromDb.isEmpty());
    }

    @Test
    void shouldReturnAlreadyExistExceptionWhenCreateNewIfExist() {
        AudienceDto audienceDto = new AudienceDto(1L, 1);
        when(repository.existsByNumber(anyInt())).thenReturn(true);
        try {
            service.createNewAudience(audienceDto);
            fail();
        } catch (Exception e) {
            assertTrue((e instanceof AlreadyExistException));
            assertTrue(e.getMessage().contains("audience"));
        }
    }

    @Test
    void shouldReturnWrongInputDataExceptionWhenCreateNewIfWrongNumber() {
        AudienceDto audienceDto = new AudienceDto(0, 1567);
        try {
            service.createNewAudience(audienceDto);
            fail();
        } catch (Exception e) {
            assertTrue((e instanceof WrongInputDataException));
            assertTrue(e.getMessage().contains("audience"));
        }
    }

    @Test
    void shouldReturnAllAudiencesAfterCreateNew() throws Exception {
        Audience audience = new Audience(1L, 12, new ArrayList<>());
        AudienceDto audienceDto = new AudienceDto(0, 12);
        List<Audience> audiences = new ArrayList<>();
        audiences.add(audience);
        when(repository.findAll()).thenReturn(audiences);
        List<AudienceDto> audiencesFromDb = service.createNewAudience(audienceDto);
        assertFalse(audiencesFromDb.isEmpty());
        assertEquals(12, audiencesFromDb.get(0).getNumber());
    }

    @Test
    void shouldReturnEditedAudienceAfterEdit() throws Exception {
        Audience audience = new Audience(1L, 12, new ArrayList<>());
        AudienceDto audienceDto = new AudienceDto(0, 12);
        when(repository.save(audience)).thenReturn(audience);
        when(repository.findByNumber(anyInt())).thenReturn(audience);
        AudienceDto audienceFromDb = service.editAudience(audienceDto);
        assertNotNull(audienceFromDb);
        assertEquals(12, audienceFromDb.getNumber());

    }

    @Test
    void shouldThrownAnNotExistExceptionWhenEditIfThereIsNoAudience() {
        AudienceDto audienceDto = new AudienceDto(0, 12);
        try {
            service.editAudience(audienceDto);
            fail();
        } catch (Exception e) {
            assertTrue((e instanceof NotExistException));
            assertTrue(e.getMessage().contains("audience"));
        }
    }

    @Test
    void shouldReturnStringAfterRemove() throws Exception {
        when(repository.existsById(anyLong())).thenReturn(true);
        String response = service.removeAudience(anyLong());
        assertTrue(response.contains("success"));
    }

    @Test
    void shouldReturnNotExistExceptionWhenRemoveIfNotExist() {
        when(repository.existsById(anyLong())).thenReturn(false);
        try {
            service.removeAudience(anyLong());
        } catch (Exception e) {
            assertTrue(e instanceof NotExistException);
            assertTrue(e.getMessage().contains("audience"));
        }
    }
}
