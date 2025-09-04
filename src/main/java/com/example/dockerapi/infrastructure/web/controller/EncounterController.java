package com.example.dockerapi.infrastructure.web.controller;

import com.example.dockerapi.application.dto.EncounterResponseDto;
import com.example.dockerapi.application.usecase.EncounterUseCase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * エンカウントコントローラー
 */
@RestController
@RequestMapping("/api")
public class EncounterController {
    private final EncounterUseCase encounterUseCase;

    public EncounterController(EncounterUseCase encounterUseCase) {
        this.encounterUseCase = encounterUseCase;
    }

    @GetMapping("/encounter")
    public EncounterResponseDto encounter() {
        return encounterUseCase.encounter();
    }
}
