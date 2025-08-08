package com.example.dockerapi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Random;

@RestController
public class EncounterController {

    private static final double APPEARANCE_RATE = 0.4;
    private static final List<MonsterDto> MONSTERS = List.of(
            new MonsterDto("スライム", 20, 10, 10, "/image/Slime.webp", 0.65),
            new MonsterDto("ドラゴン", 40, 20, 15, "/image/dragon.webp", 0.25),
            new MonsterDto("メタルスライム", 20, 10, 30, "/image/Metal_slime.webp", 0.10));

    private final Random random = new Random();

    @GetMapping("/api/encounter")
    public EncounterResponse encounter() {
        if (random.nextDouble() < APPEARANCE_RATE) {
            // 累積確率でモンスターを選択
            double r = random.nextDouble() * MONSTERS.stream().mapToDouble(MonsterDto::getRate).sum();
            double acc = 0;
            MonsterDto picked = MONSTERS.get(MONSTERS.size() - 1);
            for (MonsterDto m : MONSTERS) {
                acc += m.getRate();
                if (r <= acc) {
                    picked = m;
                    break;
                }
            }
            return new EncounterResponse(true, picked);
        } else {
            return new EncounterResponse(false, null);
        }
    }
}
