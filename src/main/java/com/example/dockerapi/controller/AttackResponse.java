package com.example.dockerapi.controller;

public class AttackResponse {
    private int northDigit;
    private int southDigit;
    private int damage;
    private String message;

    public AttackResponse() {
    }

    public AttackResponse(int northDigit, int southDigit, int damage, String message) {
        this.northDigit = northDigit;
        this.southDigit = southDigit;
        this.damage = damage;
        this.message = message;
    }

    public int getNorthDigit() {
        return northDigit;
    }

    public void setNorthDigit(int northDigit) {
        this.northDigit = northDigit;
    }

    public int getSouthDigit() {
        return southDigit;
    }

    public void setSouthDigit(int southDigit) {
        this.southDigit = southDigit;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
