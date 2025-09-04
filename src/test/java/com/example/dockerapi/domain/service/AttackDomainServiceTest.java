package com.example.dockerapi.domain.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

/**
 * AttackDomainServiceクラスのテスト
 */
@DisplayName("AttackDomainService テスト")
class AttackDomainServiceTest {

    private AttackDomainService attackDomainService;

    @BeforeEach
    void setUp() {
        attackDomainService = new AttackDomainService();
    }

    @Test
    @DisplayName("getUnitDigit - 正の座標の一桁を取得")
    void getUnitDigit_WithPositiveCoordinate_ReturnsUnitDigit() {
        // Arrange
        double coordinate = 123.456;

        // Act
        int result = attackDomainService.getUnitDigit(coordinate);

        // Assert
        assertEquals(3, result);
    }

    @Test
    @DisplayName("getUnitDigit - 負の座標の一桁を取得")
    void getUnitDigit_WithNegativeCoordinate_ReturnsUnitDigit() {
        // Arrange
        double coordinate = -123.456;

        // Act
        int result = attackDomainService.getUnitDigit(coordinate);

        // Assert
        assertEquals(3, result);
    }

    @Test
    @DisplayName("getUnitDigit - 小数点以下の座標の一桁を取得")
    void getUnitDigit_WithDecimalCoordinate_ReturnsUnitDigit() {
        // Arrange
        double coordinate = 0.123;

        // Act
        int result = attackDomainService.getUnitDigit(coordinate);

        // Assert
        assertEquals(0, result);
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
        AttackDomainService.AttackResult result = attackDomainService.evaluateAttack(baseAttack, northDigit,
                southDigit);

        // Assert
        assertEquals(100, result.getDamage());
        assertEquals("通常攻撃", result.getMessage());
    }

    @Test
    @DisplayName("evaluateAttack - ミス（北緯と南緯の一桁が一致し、4以下）")
    void evaluateAttack_WithMatchingDigitsBelowThreshold_ReturnsMiss() {
        // Arrange
        int baseAttack = 100;
        int northDigit = 2;
        int southDigit = 2;

        // Act
        AttackDomainService.AttackResult result = attackDomainService.evaluateAttack(baseAttack, northDigit,
                southDigit);

        // Assert
        assertEquals(0, result.getDamage());
        assertEquals("ミス：攻撃を回避された", result.getMessage());
    }

    @Test
    @DisplayName("evaluateAttack - 会心攻撃（北緯と南緯の一桁が一致し、5以上）")
    void evaluateAttack_WithMatchingDigitsAboveThreshold_ReturnsCritical() {
        // Arrange
        int baseAttack = 100;
        int northDigit = 7;
        int southDigit = 7;

        // Act
        AttackDomainService.AttackResult result = attackDomainService.evaluateAttack(baseAttack, northDigit,
                southDigit);

        // Assert
        assertEquals(200, result.getDamage());
        assertEquals("会心の一撃：攻撃力2倍", result.getMessage());
    }

    @ParameterizedTest
    @DisplayName("evaluateAttack - ミス判定の境界値テスト")
    @ValueSource(ints = { 0, 1, 2, 3, 4 })
    void evaluateAttack_WithMatchingDigitsAtMissThreshold_ReturnsMiss(int digit) {
        // Arrange
        int baseAttack = 100;

        // Act
        AttackDomainService.AttackResult result = attackDomainService.evaluateAttack(baseAttack, digit, digit);

        // Assert
        assertEquals(0, result.getDamage());
        assertEquals("ミス：攻撃を回避された", result.getMessage());
    }

    @ParameterizedTest
    @DisplayName("evaluateAttack - 会心判定の境界値テスト")
    @ValueSource(ints = { 5, 6, 7, 8, 9 })
    void evaluateAttack_WithMatchingDigitsAtCriticalThreshold_ReturnsCritical(int digit) {
        // Arrange
        int baseAttack = 100;

        // Act
        AttackDomainService.AttackResult result = attackDomainService.evaluateAttack(baseAttack, digit, digit);

        // Assert
        assertEquals(200, result.getDamage());
        assertEquals("会心の一撃：攻撃力2倍", result.getMessage());
    }

    @Test
    @DisplayName("AttackResult - コンストラクタとゲッターのテスト")
    void attackResult_ConstructorAndGetters_WorkCorrectly() {
        // Arrange
        int damage = 150;
        String message = "テストメッセージ";

        // Act
        AttackDomainService.AttackResult result = new AttackDomainService.AttackResult(damage, message);

        // Assert
        assertEquals(damage, result.getDamage());
        assertEquals(message, result.getMessage());
    }
}
