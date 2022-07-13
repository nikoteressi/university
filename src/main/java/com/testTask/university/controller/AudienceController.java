package com.testTask.university.controller;

import com.testTask.university.dto.AudienceDto;
import com.testTask.university.service.AudienceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/audience")
public class AudienceController {

    private final AudienceService service;

    @GetMapping("/all-audiences")
    public List<AudienceDto> getAllAudiences() {
        return service.getAllAudiences();
    }

    @PostMapping("/new-audience")
    public List<AudienceDto> createAudience(@RequestBody AudienceDto audience){
        return service.createNewAudience(audience);
    }

    @PutMapping("/edit-audience")
    public AudienceDto editAudience(@RequestBody AudienceDto audience) {
        return service.editAudience(audience);
    }

    @DeleteMapping("/remove-audience")
    public String removeAudience(@RequestParam long audienceId) {
        return service.removeAudience(audienceId);
    }
}
