package com.example.dockerapi.util;

/**
 * 座標関連のユーティリティクラス
 * 
 * @deprecated このクラスの機能は AttackDomainService に移行されました
 */
@Deprecated
public class CoordinateUtils {

    // 定数定義
    private static final int UNIT_DIGIT_DIVISOR = 10;
    private static final int MISS_THRESHOLD = 4;
    private static final int CRITICAL_MULTIPLIER = 2;

    /**
     * 座標の整数部から「一桁」（1の位）を取り出す
     * 
     * @deprecated AttackDomainService.getUnitDigit を使用してください
     */
    @Deprecated
    public static int getUnitDigit(double coord) {
        int integerPart = (int) Math.abs(coord);
        return integerPart % UNIT_DIGIT_DIVISOR;
    }

    /**
     * 北緯・南緯の一桁を比較し、ミス/会心/通常を判定する
     * 
     * @deprecated AttackDomainService.evaluateAttack を使用してください
     *
     * @param baseAttack ヒーローの基礎攻撃力
     * @param northDigit 北緯の一桁
     * @param southDigit 南緯の一桁
     * @return AttackResult ダメージ値とメッセージ
     */
    @Deprecated
    public static AttackResult evaluateAttack(int baseAttack, int northDigit, int southDigit) {
        if (northDigit == southDigit) {
            if (northDigit <= MISS_THRESHOLD) {
                // 一桁が 0～4 で一致→ミス
                return new AttackResult(0, "ミス：攻撃を回避された");
            } else {
                // 一桁が 5～9 で一致→会心
                return new AttackResult(baseAttack * CRITICAL_MULTIPLIER, "会心の一撃：攻撃力2倍");
            }
        }
        // 一致しなければ通常攻撃
        return new AttackResult(baseAttack, "通常攻撃");
    }

    /**
     * 判定結果を保持するクラス
     * 
     * @deprecated com.example.dockerapi.domain.model.AttackResult を使用してください
     */
    @Deprecated
    public static class AttackResult {
        private final int damage;
        private final String message;

        public AttackResult(int damage, String message) {
            this.damage = damage;
            this.message = message;
        }

        public int getDamage() {
            return damage;
        }

        public String getMessage() {
            return message;
        }
    }
}
