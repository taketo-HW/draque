package com.example.dockerapi.domain.service;

import com.example.dockerapi.domain.model.Monster;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class EncounterDomainService {
    private static final double DEFAULT_APPEARANCE_RATE = 0.4;
    private final Random random;

    public EncounterDomainService() {
        this.random = new Random();
    }

    // テスト用コンストラクタ
    public EncounterDomainService(Random random) {
        this.random = random;
    }

    /**
     * モンスターエンカウンターを実行する
     * 
     * @param monsters       出現可能なモンスターのリスト
     * @param appearanceRate 全体の出現率
     * @return 出現したモンスター（出現しない場合はOptional.empty()）
     */
    public Optional<Monster> executeEncounter(List<Monster> monsters, double appearanceRate) {
        if (monsters == null || monsters.isEmpty()) {
            return Optional.empty();
        }

        if (random.nextDouble() >= appearanceRate) {
            return Optional.empty();
        }

        return selectMonsterByProbability(monsters);
    }

    /**
     * デフォルトの出現率でエンカウンターを実行する
     */
    public Optional<Monster> executeEncounter(List<Monster> monsters) {
        return executeEncounter(monsters, DEFAULT_APPEARANCE_RATE);
    }

    /**
     * 確率に基づいてモンスターを選択する
     */
    private Optional<Monster> selectMonsterByProbability(List<Monster> monsters) {
        double totalRate = monsters.stream()
                .mapToDouble(Monster::getAppearanceRate)
                .sum();

        if (totalRate <= 0) {
            return Optional.empty();
        }

        double randomValue = random.nextDouble() * totalRate;
        double accumulator = 0;

        for (Monster monster : monsters) {
            accumulator += monster.getAppearanceRate();
            if (randomValue <= accumulator) {
                return Optional.of(monster);
            }
        }

        // フォールバック（通常は到達しない）
        return Optional.of(monsters.get(monsters.size() - 1));
    }
}
