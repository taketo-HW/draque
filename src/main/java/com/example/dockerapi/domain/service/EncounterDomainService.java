package com.example.dockerapi.domain.service;

import com.example.dockerapi.domain.entity.Monster;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Random;

/**
 * エンカウントドメインサービス
 */
@Component
public class EncounterDomainService {
    private static final double APPEARANCE_RATE = 0.4;
    private static final List<Monster> MONSTERS = List.of(
            new Monster("スライム", 20, 10, 10, "/image/Slime.webp", 0.65),
            new Monster("ドラゴン", 40, 20, 15, "/image/dragon.webp", 0.25),
            new Monster("メタルスライム", 20, 10, 30, "/image/Metal_slime.webp", 0.10));

    private final Random random = new Random();

    public EncounterResult encounter() {
        if (random.nextDouble() < APPEARANCE_RATE) {
            // 累積確率でモンスターを選択
            double r = random.nextDouble() * MONSTERS.stream().mapToDouble(Monster::getRate).sum();
            double acc = 0;
            Monster picked = MONSTERS.get(MONSTERS.size() - 1);
            for (Monster m : MONSTERS) {
                acc += m.getRate();
                if (r <= acc) {
                    picked = m;
                    break;
                }
            }
            return new EncounterResult(true, picked);
        } else {
            return new EncounterResult(false, null);
        }
    }

    public static class EncounterResult {
        private final boolean appear;
        private final Monster monster;

        public EncounterResult(boolean appear, Monster monster) {
            this.appear = appear;
            this.monster = monster;
        }

        public boolean isAppear() {
            return appear;
        }

        public Monster getMonster() {
            return monster;
        }
    }
}
