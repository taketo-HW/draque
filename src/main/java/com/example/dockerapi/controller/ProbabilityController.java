package com.example.dockerapi.controller;

import com.example.dockerapi.service.ProbabilityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProbabilityController {
    private final ProbabilityService probabilityService;

    public ProbabilityController(ProbabilityService probabilityService) {
        this.probabilityService = probabilityService;
    }

    @GetMapping("/api/choose")
    public ResponseEntity<String> chooseByProbability(
            @RequestParam List<String> items,
            @RequestParam List<Double> weights) {

        return probabilityService.chooseByProbability(items, weights);
    }
}
