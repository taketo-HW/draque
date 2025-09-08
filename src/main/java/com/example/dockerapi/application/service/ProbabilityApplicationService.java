package com.example.dockerapi.application.service;

import com.example.dockerapi.domain.repository.ProbabilityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProbabilityApplicationService {
    private final ProbabilityRepository probabilityRepository;

    public ProbabilityApplicationService(ProbabilityRepository probabilityRepository) {
        this.probabilityRepository = probabilityRepository;
    }

    /**
     * 確率に基づいて項目を選択する
     */
    public String selectItemByProbability(List<String> items, List<Double> weights) {
        return probabilityRepository.selectByProbability(items, weights);
    }
}
