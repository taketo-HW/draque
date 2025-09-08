package com.example.dockerapi.controller;

import com.example.dockerapi.application.service.GameApplicationService;
import com.example.dockerapi.dto.AttackResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AttackController {
    private final GameApplicationService gameApplicationService;

    public AttackController(GameApplicationService gameApplicationService) {
        this.gameApplicationService = gameApplicationService;
    }

    @GetMapping("/api/attack")
    public AttackResponse attack(
            @RequestParam double northCoord,
            @RequestParam double southCoord,
            @RequestParam(defaultValue = "10") int baseAttack) {

        return gameApplicationService.executeAttack(northCoord, southCoord, baseAttack);
    }
}
