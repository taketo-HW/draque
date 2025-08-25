package com.example.dockerapi.domain.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

@Service
public class ProbabilityDomainService {
    private final Random random;

    public ProbabilityDomainService() {
        this.random = new Random();
    }

    // テスト用コンストラクタ
    public ProbabilityDomainService(Random random) {
        this.random = random;
    }

    /**
     * 重み付き確率で項目を選択する
     * 
     * @param items   選択肢のリスト
     * @param weights 各選択肢の重み
     * @return 選択された項目
     * @throws IllegalArgumentException リストのサイズが一致しない、または空の場合
     */
    public String selectByProbability(List<String> items, List<Double> weights) {
        if (items == null || weights == null) {
            throw new IllegalArgumentException("Items and weights cannot be null");
        }
        if (items.size() != weights.size()) {
            throw new IllegalArgumentException("Items and weights must have the same size");
        }
        if (items.isEmpty()) {
            throw new IllegalArgumentException("Items and weights cannot be empty");
        }

        // 負の重みをチェック
        for (Double weight : weights) {
            if (weight == null || weight < 0) {
                throw new IllegalArgumentException("Weights must be non-negative");
            }
        }

        // 累積確率に変換
        NavigableMap<Double, String> cumulativeMap = new TreeMap<>();
        double total = 0.0;
        for (int i = 0; i < weights.size(); i++) {
            total += weights.get(i);
            cumulativeMap.put(total, items.get(i));
        }

        if (total == 0.0) {
            throw new IllegalArgumentException("Total weight cannot be zero");
        }

        // ランダムに選択
        double randomValue = random.nextDouble() * total;
        return cumulativeMap.higherEntry(randomValue).getValue();
    }
}
