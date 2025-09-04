package com.example.dockerapi.infrastructure.web.controller;

import com.example.dockerapi.application.usecase.ProbabilityUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 確率計算コントローラー
 */
@RestController
@RequestMapping("/api")
public class ProbabilityController {
    private final ProbabilityUseCase probabilityUseCase;

    public ProbabilityController(ProbabilityUseCase probabilityUseCase) {
        this.probabilityUseCase = probabilityUseCase;
    }

    @GetMapping("/choose")
    public ResponseEntity<String> chooseByProbability(
            @RequestParam List<String> items,
            @RequestParam List<Double> weights) {
        try {
            String selected = probabilityUseCase.chooseByProbability(items, weights);
            return ResponseEntity.ok(selected);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
