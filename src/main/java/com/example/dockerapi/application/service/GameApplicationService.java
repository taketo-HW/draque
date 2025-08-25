package com.example.dockerapi.application.service;

import com.example.dockerapi.domain.model.AttackResult;
import com.example.dockerapi.domain.model.Monster;
import com.example.dockerapi.domain.service.AttackDomainService;
import com.example.dockerapi.domain.service.EncounterDomainService;
import com.example.dockerapi.dto.AttackResponse;
import com.example.dockerapi.dto.EncounterResponse;
import com.example.dockerapi.dto.MonsterDto;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class GameApplicationService {
    private static final List<Monster> AVAILABLE_MONSTERS = Arrays.asList(
            new Monster("スライム", 20, 10, 10, "/image/Slime.webp", 0.65),
            new Monster("ドラゴン", 40, 20, 15, "/image/dragon.webp", 0.25),
            new Monster("メタルスライム", 20, 10, 30, "/image/Metal_slime.webp", 0.10));

    private final EncounterDomainService encounterDomainService;
    private final AttackDomainService attackDomainService;

    public GameApplicationService(EncounterDomainService encounterDomainService,
            AttackDomainService attackDomainService) {
        this.encounterDomainService = encounterDomainService;
        this.attackDomainService = attackDomainService;
    }

    /**
     * モンスターエンカウンターを実行する
     */
    public EncounterResponse executeEncounter() {
        Optional<Monster> encounteredMonster = encounterDomainService.executeEncounter(AVAILABLE_MONSTERS);

        if (encounteredMonster.isPresent()) {
            Monster monster = encounteredMonster.get();
            MonsterDto monsterDto = convertToDto(monster);
            return new EncounterResponse(true, monsterDto);
        } else {
            return new EncounterResponse(false, null);
        }
    }

    /**
     * 攻撃を実行する
     */
    public AttackResponse executeAttack(double northCoord, double southCoord, int baseAttack) {
        int northDigit = attackDomainService.getUnitDigit(northCoord);
        int southDigit = attackDomainService.getUnitDigit(southCoord);

        AttackResult result = attackDomainService.evaluateAttack(baseAttack, northDigit, southDigit);

        return new AttackResponse(northDigit, southDigit, result.getDamage(), result.getMessage());
    }

    /**
     * MonsterをMonsterDtoに変換する
     */
    private MonsterDto convertToDto(Monster monster) {
        return new MonsterDto(
                monster.getName(),
                monster.getHp(),
                monster.getAttack(),
                monster.getExp(),
                monster.getImage(),
                monster.getAppearanceRate());
    }
}
