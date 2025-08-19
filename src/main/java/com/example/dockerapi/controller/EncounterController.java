package com.example.dockerapi.controller;

import com.example.dockerapi.dto.EncounterResponse;
import com.example.dockerapi.service.EncounterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EncounterController {

    private final EncounterService encounterService;

    public EncounterController(EncounterService encounterService) {
        this.encounterService = encounterService;
    }

    @GetMapping("/api/encounter")
    public EncounterResponse encounter() {
        return encounterService.rollEncounter();
    }
}
