package com.example.dockerapi.application.service;

import com.example.dockerapi.domain.model.AttackResult;
import com.example.dockerapi.domain.model.Monster;
import com.example.dockerapi.domain.repository.MonsterRepository;
import com.example.dockerapi.dto.AttackResponse;
import com.example.dockerapi.dto.EncounterResponse;
import com.example.dockerapi.dto.MonsterDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameApplicationService {
    private final MonsterRepository monsterRepository;

    public GameApplicationService(MonsterRepository monsterRepository) {
        this.monsterRepository = monsterRepository;
    }

    /**
     * モンスターエンカウンターを実行する
     */
    public EncounterResponse executeEncounter() {
        List<Monster> availableMonsters = monsterRepository.findAll();
        Optional<Monster> encounteredMonster = monsterRepository.executeEncounter(availableMonsters);

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
        int northDigit = getUnitDigit(northCoord);
        int southDigit = getUnitDigit(southCoord);

        AttackResult result = evaluateAttack(baseAttack, northDigit, southDigit);

        return new AttackResponse(northDigit, southDigit, result.getDamage(), result.getMessage());
    }

    /**
     * 座標の整数部から「一桁」（1の位）を取り出す
     */
    private int getUnitDigit(double coord) {
        int integerPart = (int) Math.abs(coord);
        return integerPart % 10;
    }

    /**
     * 北緯・南緯の一桁を比較し、攻撃結果を判定する
     */
    private AttackResult evaluateAttack(int baseAttack, int northDigit, int southDigit) {
        if (baseAttack < 0) {
            throw new IllegalArgumentException("Base attack cannot be negative");
        }
        if (northDigit < 0 || northDigit > 9) {
            throw new IllegalArgumentException("North digit must be between 0 and 9");
        }
        if (southDigit < 0 || southDigit > 9) {
            throw new IllegalArgumentException("South digit must be between 0 and 9");
        }

        if (northDigit == southDigit) {
            if (northDigit <= 4) {
                // 一桁が 0～4 で一致→ミス
                return new AttackResult(0, "ミス：攻撃を回避された", AttackResult.AttackType.MISS);
            } else {
                // 一桁が 5～9 で一致→会心
                return new AttackResult(
                        baseAttack * 2,
                        "会心の一撃：攻撃力2倍",
                        AttackResult.AttackType.CRITICAL);
            }
        }
        // 一致しなければ通常攻撃
        return new AttackResult(baseAttack, "通常攻撃", AttackResult.AttackType.NORMAL);
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
