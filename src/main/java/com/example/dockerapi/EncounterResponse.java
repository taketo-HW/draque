package com.example.dockerapi;

public class EncounterResponse {
    private boolean appear;
    private MonsterDto monster;

    public EncounterResponse() {
    }

    public EncounterResponse(boolean appear, MonsterDto monster) {
        this.appear = appear;
        this.monster = monster;
    }

    public boolean isAppear() {
        return appear;
    }

    public void setAppear(boolean appear) {
        this.appear = appear;
    }

    public MonsterDto getMonster() {
        return monster;
    }

    public void setMonster(MonsterDto monster) {
        this.monster = monster;
    }
}
