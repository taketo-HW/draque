package com.example.dockerapi.domain.model;

public class AttackResult {
    private final int damage;
    private final String message;
    private final AttackType attackType;

    public enum AttackType {
        MISS, NORMAL, CRITICAL
    }

    public AttackResult(int damage, String message, AttackType attackType) {
        if (damage < 0) {
            throw new IllegalArgumentException("Damage cannot be negative");
        }
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }
        if (attackType == null) {
            throw new IllegalArgumentException("Attack type cannot be null");
        }

        this.damage = damage;
        this.message = message;
        this.attackType = attackType;
    }

    public int getDamage() {
        return damage;
    }

    public String getMessage() {
        return message;
    }

    public AttackType getAttackType() {
        return attackType;
    }

    public boolean isMiss() {
        return attackType == AttackType.MISS;
    }

    public boolean isCritical() {
        return attackType == AttackType.CRITICAL;
    }
}
