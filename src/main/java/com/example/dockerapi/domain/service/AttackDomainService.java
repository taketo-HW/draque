package com.example.dockerapi.domain.service;

import com.example.dockerapi.domain.model.AttackResult;
import org.springframework.stereotype.Service;

@Service
public class AttackDomainService {
    private static final int UNIT_DIGIT_DIVISOR = 10;
    private static final int MISS_THRESHOLD = 4;
    private static final int CRITICAL_MULTIPLIER = 2;

    /**
     * 座標の整数部から「一桁」（1の位）を取り出す
     */
    public int getUnitDigit(double coord) {
        int integerPart = (int) Math.abs(coord);
        return integerPart % UNIT_DIGIT_DIVISOR;
    }

    /**
     * 北緯・南緯の一桁を比較し、攻撃結果を判定する
     * 
     * @param baseAttack ヒーローの基礎攻撃力
     * @param northDigit 北緯の一桁
     * @param southDigit 南緯の一桁
     * @return AttackResult 攻撃結果
     */
    public AttackResult evaluateAttack(int baseAttack, int northDigit, int southDigit) {
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
            if (northDigit <= MISS_THRESHOLD) {
                // 一桁が 0～4 で一致→ミス
                return new AttackResult(0, "ミス：攻撃を回避された", AttackResult.AttackType.MISS);
            } else {
                // 一桁が 5～9 で一致→会心
                return new AttackResult(
                        baseAttack * CRITICAL_MULTIPLIER,
                        "会心の一撃：攻撃力2倍",
                        AttackResult.AttackType.CRITICAL);
            }
        }
        // 一致しなければ通常攻撃
        return new AttackResult(baseAttack, "通常攻撃", AttackResult.AttackType.NORMAL);
    }
}
