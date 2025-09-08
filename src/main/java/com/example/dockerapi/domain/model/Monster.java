package com.example.dockerapi.domain.model;

public class Monster {
    private final String name;
    private final int hp;
    private final int attack;
    private final int exp;
    private final String image;
    private final double appearanceRate;

    public Monster(String name, int hp, int attack, int exp, String image, double appearanceRate) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Monster name cannot be null or empty");
        }
        if (hp <= 0) {
            throw new IllegalArgumentException("HP must be positive");
        }
        if (attack < 0) {
            throw new IllegalArgumentException("Attack cannot be negative");
        }
        if (exp < 0) {
            throw new IllegalArgumentException("Experience cannot be negative");
        }
        if (appearanceRate < 0 || appearanceRate > 1) {
            throw new IllegalArgumentException("Appearance rate must be between 0 and 1");
        }

        this.name = name;
        this.hp = hp;
        this.attack = attack;
        this.exp = exp;
        this.image = image;
        this.appearanceRate = appearanceRate;
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public int getAttack() {
        return attack;
    }

    public int getExp() {
        return exp;
    }

    public String getImage() {
        return image;
    }

    public double getAppearanceRate() {
        return appearanceRate;
    }
}
