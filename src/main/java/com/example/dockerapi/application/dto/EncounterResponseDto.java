package com.example.dockerapi.application.dto;

/**
 * エンカウントレスポンスDTO
 */
public class EncounterResponseDto {
    private boolean appear;
    private MonsterDto monster;

    public EncounterResponseDto() {
    }

    public EncounterResponseDto(boolean appear, MonsterDto monster) {
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
