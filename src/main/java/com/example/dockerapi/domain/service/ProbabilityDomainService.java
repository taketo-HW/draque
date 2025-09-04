package com.example.dockerapi.domain.service;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

/**
 * 確率計算ドメインサービス
 */
@Component
public class ProbabilityDomainService {
    private final Random random = new Random();

    public String chooseByProbability(List<String> items, List<Double> weights) {
        if (items.size() != weights.size() || items.isEmpty()) {
            throw new IllegalArgumentException("items と weights は同じ長さのリストで、空であってはいけません");
        }

        // 累積確率に変換
        NavigableMap<Double, String> map = new TreeMap<>();
        double total = 0.0;
        for (int i = 0; i < weights.size(); i++) {
            total += weights.get(i);
            map.put(total, items.get(i));
        }

        // ランダムに選択
        double r = random.nextDouble() * total;
        return map.higherEntry(r).getValue();
    }
}
