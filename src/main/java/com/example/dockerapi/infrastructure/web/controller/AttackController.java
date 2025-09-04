package com.example.dockerapi.infrastructure.web.controller;

import com.example.dockerapi.application.dto.AttackResponseDto;
import com.example.dockerapi.application.usecase.AttackUseCase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 攻撃コントローラー
 */
@RestController
@RequestMapping("/api")
public class AttackController {
    private final AttackUseCase attackUseCase;

    public AttackController(AttackUseCase attackUseCase) {
        this.attackUseCase = attackUseCase;
    }

    @GetMapping("/attack")
    public AttackResponseDto attack(
            @RequestParam int baseAttack,
            @RequestParam double northLatitude,
            @RequestParam double southLatitude) {
        return attackUseCase.attack(baseAttack, northLatitude, southLatitude);
    }
}
