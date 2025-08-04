package com.example.dockerapi;

public class MonsterDto {
    private String name;
    private int hp;
    private int attack;
    private int exp;
    private String image;
    private double rate; // 出現確率

    public MonsterDto() {
    }

    public MonsterDto(String name, int hp, int attack, int exp, String image, double rate) {
        this.name = name;
        this.hp = hp;
        this.attack = attack;
        this.exp = exp;
        this.image = image;
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
