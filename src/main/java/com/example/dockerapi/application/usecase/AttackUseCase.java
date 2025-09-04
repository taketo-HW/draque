package com.example.dockerapi.application.usecase;

import com.example.dockerapi.application.dto.AttackResponseDto;
import com.example.dockerapi.domain.service.AttackDomainService;
import org.springframework.stereotype.Service;

/**
 * 攻撃ユースケース
 */
@Service
public class AttackUseCase {
    private final AttackDomainService attackDomainService;

    public AttackUseCase(AttackDomainService attackDomainService) {
        this.attackDomainService = attackDomainService;
    }

    public AttackResponseDto attack(int baseAttack, double northLatitude, double southLatitude) {
        int northDigit = attackDomainService.getUnitDigit(northLatitude);
        int southDigit = attackDomainService.getUnitDigit(southLatitude);
        
        AttackDomainService.AttackResult result = attackDomainService.evaluateAttack(baseAttack, northDigit, southDigit);
        
        return new AttackResponseDto(northDigit, southDigit, result.getDamage(), result.getMessage());
    }
}
