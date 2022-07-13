package com.testTask.university.mappers;

import com.testTask.university.dto.AudienceDto;
import com.testTask.university.entity.Audience;
import com.testTask.university.utils.mappers.AudienceMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AudienceMapperTest {

    @Autowired
    private AudienceMapper mapper;

    @Test
    public void shouldReturnAudienceDto() {
        Audience audience = new Audience(0, 12, new ArrayList<>());
        AudienceDto audienceDto = new AudienceDto(0, 12);
        AudienceDto converted = mapper.convertToDto(audience);
        assertEquals(audienceDto, converted);
    }

    @Test
    public void shouldReturnAudience() {
        Audience audience = new Audience(0, 12, null);
        AudienceDto audienceDto = new AudienceDto(0, 12);
        Audience converted = mapper.convertToEntity(audienceDto);
        assertEquals(audience, converted);
    }
}
