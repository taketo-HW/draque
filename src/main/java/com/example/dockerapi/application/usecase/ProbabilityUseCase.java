package com.example.dockerapi.application.usecase;

import com.example.dockerapi.domain.service.ProbabilityDomainService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 確率計算ユースケース
 */
@Service
public class ProbabilityUseCase {
    private final ProbabilityDomainService probabilityDomainService;

    public ProbabilityUseCase(ProbabilityDomainService probabilityDomainService) {
        this.probabilityDomainService = probabilityDomainService;
    }

    public String chooseByProbability(List<String> items, List<Double> weights) {
        return probabilityDomainService.chooseByProbability(items, weights);
    }
}
