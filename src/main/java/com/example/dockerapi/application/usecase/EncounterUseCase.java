package com.example.dockerapi.application.usecase;

import com.example.dockerapi.application.dto.EncounterResponseDto;
import com.example.dockerapi.application.dto.MonsterDto;
import com.example.dockerapi.domain.service.EncounterDomainService;
import org.springframework.stereotype.Service;

/**
 * エンカウントユースケース
 */
@Service
public class EncounterUseCase {
    private final EncounterDomainService encounterDomainService;

    public EncounterUseCase(EncounterDomainService encounterDomainService) {
        this.encounterDomainService = encounterDomainService;
    }

    public EncounterResponseDto encounter() {
        EncounterDomainService.EncounterResult result = encounterDomainService.encounter();
        
        if (result.isAppear()) {
            MonsterDto monsterDto = convertToDto(result.getMonster());
            return new EncounterResponseDto(true, monsterDto);
        } else {
            return new EncounterResponseDto(false, null);
        }
    }

    private MonsterDto convertToDto(com.example.dockerapi.domain.entity.Monster monster) {
        return new MonsterDto(
                monster.getName(),
                monster.getHp(),
                monster.getAttack(),
                monster.getExp(),
                monster.getImage(),
                monster.getRate()
        );
    }
}
