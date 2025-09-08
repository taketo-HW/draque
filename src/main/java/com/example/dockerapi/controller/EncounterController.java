package com.example.dockerapi.controller;

import com.example.dockerapi.application.service.GameApplicationService;
import com.example.dockerapi.dto.EncounterResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EncounterController {
    private final GameApplicationService gameApplicationService;

    public EncounterController(GameApplicationService gameApplicationService) {
        this.gameApplicationService = gameApplicationService;
    }

    @GetMapping("/api/encounter")
    public EncounterResponse encounter() {
        return gameApplicationService.executeEncounter();
    }
}
