package com.example.dockerapi.infrastructure.repository;

import com.example.dockerapi.domain.repository.ProbabilityRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Random;

/**
 * Infrastructure層のProbabilityRepository実装
 * Domain層のインターフェースを実装し、確率計算ロジックを提供
 */
@Repository
@Lazy
public class ProbabilityRepositoryImpl implements ProbabilityRepository {
    private final Random random;

    public ProbabilityRepositoryImpl() {
        this.random = new Random();
    }

    // テスト用コンストラクタ
    public ProbabilityRepositoryImpl(Random random) {
        this.random = random;
    }

    @Override
    public String selectByProbability(List<String> items, List<Double> weights) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Items list cannot be null or empty");
        }
        if (weights == null || weights.isEmpty()) {
            throw new IllegalArgumentException("Weights list cannot be null or empty");
        }
        if (items.size() != weights.size()) {
            throw new IllegalArgumentException("Items and weights lists must have the same size");
        }

        double totalWeight = weights.stream().mapToDouble(Double::doubleValue).sum();
        if (totalWeight <= 0) {
            throw new IllegalArgumentException("Total weight must be positive");
        }

        // 負の重みをチェック
        for (Double weight : weights) {
            if (weight < 0) {
                throw new IllegalArgumentException("Weight cannot be negative");
            }
        }

        double randomValue = random.nextDouble() * totalWeight;
        double accumulator = 0;

        for (int i = 0; i < items.size(); i++) {
            accumulator += weights.get(i);
            if (randomValue <= accumulator) {
                return items.get(i);
            }
        }

        // フォールバック（通常は到達しない）
        return items.get(items.size() - 1);
    }
}
