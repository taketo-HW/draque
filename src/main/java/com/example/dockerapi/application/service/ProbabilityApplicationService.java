package com.example.dockerapi.application.service;

import com.example.dockerapi.domain.service.ProbabilityDomainService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProbabilityApplicationService {
    private final ProbabilityDomainService probabilityDomainService;

    public ProbabilityApplicationService(ProbabilityDomainService probabilityDomainService) {
        this.probabilityDomainService = probabilityDomainService;
    }

    /**
     * 確率に基づいて項目を選択する
     */
    public String selectItemByProbability(List<String> items, List<Double> weights) {
        return probabilityDomainService.selectByProbability(items, weights);
    }
}
