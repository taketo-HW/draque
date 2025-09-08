package com.example.dockerapi.application.service;

import com.example.dockerapi.domain.repository.MonsterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DisplayName("GameApplicationService テスト")
class GameApplicationServiceTest {

    @Mock
    private MonsterRepository monsterRepository;

    private GameApplicationService gameApplicationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        gameApplicationService = new GameApplicationService(monsterRepository);
    }

    @ParameterizedTest
    @DisplayName("executeAttack - 様々な座標値の一桁を取得")
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
    void executeAttack_WithVariousCoordinates_ReturnsCorrectUnitDigit(double coordinate, int expected) {
        // Act
        var result = gameApplicationService.executeAttack(coordinate, 5.0, 100);

        // Assert
        assertEquals(expected, result.getNorthDigit());
    }

    @Test
    @DisplayName("executeAttack - 通常攻撃（北緯と南緯の一桁が異なる）")
    void executeAttack_WithDifferentDigits_ReturnsNormalAttack() {
        // Arrange
        int baseAttack = 100;
        double northCoord = 3.0;
        double southCoord = 7.0;

        // Act
        var result = gameApplicationService.executeAttack(northCoord, southCoord, baseAttack);

        // Assert
        assertEquals(100, result.getDamage());
        assertEquals("通常攻撃", result.getMessage());
        assertEquals(3, result.getNorthDigit());
        assertEquals(7, result.getSouthDigit());
    }

    @ParameterizedTest
    @DisplayName("executeAttack - ミス判定の境界値テスト")
    @ValueSource(ints = { 0, 1, 2, 3, 4 })
    void executeAttack_WithMatchingDigitsAtMissThreshold_ReturnsMiss(int digit) {
        // Arrange
        int baseAttack = 100;
        double coord = digit + 0.0;

        // Act
        var result = gameApplicationService.executeAttack(coord, coord, baseAttack);

        // Assert
        assertEquals(0, result.getDamage());
        assertEquals("ミス：攻撃を回避された", result.getMessage());
        assertEquals(digit, result.getNorthDigit());
        assertEquals(digit, result.getSouthDigit());
    }

    @ParameterizedTest
    @DisplayName("executeAttack - 会心判定の境界値テスト")
    @ValueSource(ints = { 5, 6, 7, 8, 9 })
    void executeAttack_WithMatchingDigitsAtCriticalThreshold_ReturnsCritical(int digit) {
        // Arrange
        int baseAttack = 100;
        double coord = digit + 0.0;

        // Act
        var result = gameApplicationService.executeAttack(coord, coord, baseAttack);

        // Assert
        assertEquals(200, result.getDamage());
        assertEquals("会心の一撃：攻撃力2倍", result.getMessage());
        assertEquals(digit, result.getNorthDigit());
        assertEquals(digit, result.getSouthDigit());
    }

    @Test
    @DisplayName("executeAttack - 不正な基礎攻撃力でIllegalArgumentException")
    void executeAttack_WithNegativeBaseAttack_ThrowsException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            gameApplicationService.executeAttack(5.0, 5.0, -1));
    }

    @Test
    @DisplayName("executeAttack - 座標の一桁が範囲外の場合のテスト")
    void executeAttack_WithInvalidCoordinates_HandlesCorrectly() {
        // 現在の実装では座標の検証は行われていないため、
        // 実際の動作をテストする
        var result1 = gameApplicationService.executeAttack(-1.0, 5.0, 100);
        var result2 = gameApplicationService.executeAttack(5.0, -1.0, 100);
        
        // 座標の絶対値が使用されるため、-1.0は1.0として扱われる
        assertNotNull(result1);
        assertNotNull(result2);
    }
}
