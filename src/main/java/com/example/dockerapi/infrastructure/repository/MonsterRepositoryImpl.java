package com.example.dockerapi.infrastructure.repository;

import com.example.dockerapi.domain.model.Monster;
import com.example.dockerapi.domain.repository.MonsterRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Infrastructure層のMonsterRepository実装
 * Domain層のインターフェースを実装し、モンスターデータを管理
 */
@Repository
@Lazy
public class MonsterRepositoryImpl implements MonsterRepository {
    private static final List<Monster> AVAILABLE_MONSTERS = Arrays.asList(
            new Monster("スライム", 20, 10, 10, "/image/Slime.webp", 0.65),
            new Monster("ドラゴン", 40, 20, 15, "/image/dragon.webp", 0.25),
            new Monster("メタルスライム", 20, 10, 30, "/image/Metal_slime.webp", 0.10));

    private static final double DEFAULT_APPEARANCE_RATE = 0.4;
    private final Random random;

    public MonsterRepositoryImpl() {
        this.random = new Random();
    }

    // テスト用コンストラクタ
    public MonsterRepositoryImpl(Random random) {
        this.random = random;
    }

    @Override
    public List<Monster> findAll() {
        return AVAILABLE_MONSTERS;
    }

    @Override
    public Optional<Monster> executeEncounter(List<Monster> monsters) {
        if (monsters == null || monsters.isEmpty()) {
            return Optional.empty();
        }

        if (random.nextDouble() >= DEFAULT_APPEARANCE_RATE) {
            return Optional.empty();
        }

        return selectMonsterByProbability(monsters);
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
