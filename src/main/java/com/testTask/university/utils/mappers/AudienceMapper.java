package com.testTask.university.utils.mappers;

import com.testTask.university.dto.AudienceDto;
import com.testTask.university.entity.Audience;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AudienceMapper {

    public AudienceDto convertToDto(Audience audience) {
        return AudienceDto.builder()
                .id(audience.getId())
                .number(audience.getNumber())
                .build();
    }

    public Audience convertToEntity(AudienceDto audienceDto) {
        Audience audience = new Audience();
        audience.setId(audienceDto.getId());
        audience.setNumber(audienceDto.getNumber());
        return audience;
    }
}
