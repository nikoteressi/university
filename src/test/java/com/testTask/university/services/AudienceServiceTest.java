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
import static org.mockito.Mockito.*;

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
        verify(repository, times(1)).findAll();
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldReturnEmptyListIfThereIsNoAudiences() {
        List<Audience> audiences = new ArrayList<>();
        when(repository.findAll()).thenReturn(audiences);
        List<AudienceDto> audiencesFromDb = service.getAllAudiences();
        assertTrue(audiencesFromDb.isEmpty());
        verify(repository, times(1)).findAll();
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldReturnAlreadyExistExceptionWhenCreateNewIfExist() {
        AudienceDto audienceDto = new AudienceDto(1L, 1);
        when(repository.existsByNumber(anyInt())).thenReturn(true);
        AlreadyExistException alreadyExistException = assertThrows(AlreadyExistException.class,
                () -> service.createNewAudience(audienceDto));
        assertTrue(alreadyExistException.getMessage().contains("audience"));
        verify(repository, times(1)).existsByNumber(anyInt());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldReturnWrongInputDataExceptionWhenCreateNewIfWrongNumber() {
        AudienceDto audienceDto = new AudienceDto(0, 1567);
        WrongInputDataException wrongInputDataException = assertThrows(WrongInputDataException.class,
                () -> service.createNewAudience(audienceDto));
        assertTrue(wrongInputDataException.getMessage().contains("audience"));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldReturnAllAudiencesAfterCreateNew() {
        Audience audience = new Audience(1L, 12, new ArrayList<>());
        AudienceDto audienceDto = new AudienceDto(0, 12);
        List<Audience> audiences = new ArrayList<>();
        audiences.add(audience);
        when(repository.findAll()).thenReturn(audiences);
        List<AudienceDto> audiencesFromDb = service.createNewAudience(audienceDto);
        assertFalse(audiencesFromDb.isEmpty());
        assertEquals(12, audiencesFromDb.get(0).getNumber());
        verify(repository, times(1)).existsByNumber(anyInt());
        verify(repository, times(1)).save(audience);
        verify(repository, times(1)).findAll();
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldReturnEditedAudienceAfterEdit() {
        Audience audience = new Audience(1L, 12, new ArrayList<>());
        AudienceDto audienceDto = new AudienceDto(0, 12);
        when(repository.save(audience)).thenReturn(audience);
        when(repository.findByNumber(anyInt())).thenReturn(audience);
        AudienceDto audienceFromDb = service.editAudience(audienceDto);
        assertNotNull(audienceFromDb);
        assertEquals(12, audienceFromDb.getNumber());
        verify(repository, times(1)).findByNumber(anyInt());
        verify(repository, times(1)).save(audience);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldThrownAnNotExistExceptionWhenEditIfThereIsNoAudience() {
        AudienceDto audienceDto = new AudienceDto(0, 12);
        when(repository.existsByNumber(anyInt())).thenReturn(false);
        NotExistException notExistException = assertThrows(NotExistException.class,
                () -> service.editAudience(audienceDto));
        assertTrue(notExistException.getMessage().contains("audience"));
        verify(repository, times(1)).findByNumber(anyInt());
        verifyNoMoreInteractions(repository);

    }

    @Test
    void shouldReturnStringAfterRemove() {
        when(repository.existsById(anyLong())).thenReturn(true);
        String response = service.removeAudience(anyLong());
        assertTrue(response.contains("success"));
        verify(repository, times(1)).existsById(anyLong());
        verify(repository, times(1)).deleteById(anyLong());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldReturnNotExistExceptionWhenRemoveIfNotExist() {
        when(repository.existsById(anyLong())).thenReturn(false);
        NotExistException notExistException = assertThrows(NotExistException.class,
                () -> service.removeAudience(anyLong()));
        assertTrue(notExistException.getMessage().contains("audience"));
        verify(repository, times(1)).existsById(anyLong());
        verifyNoMoreInteractions(repository);
    }
}
