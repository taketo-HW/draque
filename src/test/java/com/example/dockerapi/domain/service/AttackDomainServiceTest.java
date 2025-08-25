package com.example.dockerapi.domain.service;

import com.example.dockerapi.domain.model.AttackResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AttackDomainService テスト")
class AttackDomainServiceTest {

    private AttackDomainService attackDomainService;

    @BeforeEach
    void setUp() {
        attackDomainService = new AttackDomainService();
    }

    @ParameterizedTest
    @DisplayName("getUnitDigit - 様々な座標値の一桁を取得")
    @CsvSource({
            "0.0, 0",
            "1.0, 1",
            "10.0, 0",
            "11.0, 1",
            "99.0, 9",
            "100.0, 0",
            "123.456, 3",
            "-123.456, 3"
    })
    void getUnitDigit_WithVariousCoordinates_ReturnsCorrectUnitDigit(double coordinate, int expected) {
        // Act
        int result = attackDomainService.getUnitDigit(coordinate);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("evaluateAttack - 通常攻撃（北緯と南緯の一桁が異なる）")
    void evaluateAttack_WithDifferentDigits_ReturnsNormalAttack() {
        // Arrange
        int baseAttack = 100;
        int northDigit = 3;
        int southDigit = 7;

        // Act
        AttackResult result = attackDomainService.evaluateAttack(baseAttack, northDigit, southDigit);

        // Assert
        assertEquals(100, result.getDamage());
        assertEquals("通常攻撃", result.getMessage());
        assertEquals(AttackResult.AttackType.NORMAL, result.getAttackType());
        assertFalse(result.isMiss());
        assertFalse(result.isCritical());
    }

    @ParameterizedTest
    @DisplayName("evaluateAttack - ミス判定の境界値テスト")
    @ValueSource(ints = { 0, 1, 2, 3, 4 })
    void evaluateAttack_WithMatchingDigitsAtMissThreshold_ReturnsMiss(int digit) {
        // Arrange
        int baseAttack = 100;

        // Act
        AttackResult result = attackDomainService.evaluateAttack(baseAttack, digit, digit);

        // Assert
        assertEquals(0, result.getDamage());
        assertEquals("ミス：攻撃を回避された", result.getMessage());
        assertEquals(AttackResult.AttackType.MISS, result.getAttackType());
        assertTrue(result.isMiss());
        assertFalse(result.isCritical());
    }

    @ParameterizedTest
    @DisplayName("evaluateAttack - 会心判定の境界値テスト")
    @ValueSource(ints = { 5, 6, 7, 8, 9 })
    void evaluateAttack_WithMatchingDigitsAtCriticalThreshold_ReturnsCritical(int digit) {
        // Arrange
        int baseAttack = 100;

        // Act
        AttackResult result = attackDomainService.evaluateAttack(baseAttack, digit, digit);

        // Assert
        assertEquals(200, result.getDamage());
        assertEquals("会心の一撃：攻撃力2倍", result.getMessage());
        assertEquals(AttackResult.AttackType.CRITICAL, result.getAttackType());
        assertFalse(result.isMiss());
        assertTrue(result.isCritical());
    }

    @Test
    @DisplayName("evaluateAttack - 不正な基礎攻撃力でIllegalArgumentException")
    void evaluateAttack_WithNegativeBaseAttack_ThrowsException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> attackDomainService.evaluateAttack(-1, 5, 5));
    }

    @Test
    @DisplayName("evaluateAttack - 不正な北緯でIllegalArgumentException")
    void evaluateAttack_WithInvalidNorthDigit_ThrowsException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> attackDomainService.evaluateAttack(100, -1, 5));

        assertThrows(IllegalArgumentException.class, () -> attackDomainService.evaluateAttack(100, 10, 5));
    }

    @Test
    @DisplayName("evaluateAttack - 不正な南緯でIllegalArgumentException")
    void evaluateAttack_WithInvalidSouthDigit_ThrowsException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> attackDomainService.evaluateAttack(100, 5, -1));

        assertThrows(IllegalArgumentException.class, () -> attackDomainService.evaluateAttack(100, 5, 10));
    }
}
