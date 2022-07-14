package com.testTask.university.controller;

import com.testTask.university.dto.AudienceDto;
import com.testTask.university.service.AudienceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/audiences")
public class AudienceController {

    private final AudienceService service;

    @GetMapping
    public List<AudienceDto> getAllAudiences() {
        return service.getAllAudiences();
    }

    @PostMapping
    public List<AudienceDto> createAudience(@RequestBody AudienceDto audience){
        return service.createNewAudience(audience);
    }

    @PutMapping
    public AudienceDto editAudience(@RequestBody AudienceDto audience) {
        return service.editAudience(audience);
    }

    @DeleteMapping("/{audienceId}")
    public String removeAudience(@PathVariable long audienceId) {
        return service.removeAudience(audienceId);
    }
}
