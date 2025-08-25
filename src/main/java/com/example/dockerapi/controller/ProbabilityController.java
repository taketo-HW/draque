package com.example.dockerapi.controller;

import com.example.dockerapi.application.service.ProbabilityApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProbabilityController {
    private final ProbabilityApplicationService probabilityApplicationService;

    public ProbabilityController(ProbabilityApplicationService probabilityApplicationService) {
        this.probabilityApplicationService = probabilityApplicationService;
    }

    @GetMapping("/api/choose")
    public ResponseEntity<String> chooseByProbability(
            @RequestParam List<String> items,
            @RequestParam List<Double> weights) {

        try {
            String selected = probabilityApplicationService.selectItemByProbability(items, weights);
            return ResponseEntity.ok(selected);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
