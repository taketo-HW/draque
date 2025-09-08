package com.example.dockerapi.domain.repository;

import java.util.List;

/**
 * Domain層のProbabilityRepositoryインターフェース
 * Infrastructure層で実装される
 */
public interface ProbabilityRepository {
    String selectByProbability(List<String> items, List<Double> weights);
}
