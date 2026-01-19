package com.example.dockerapi.controller;

import com.example.dockerapi.dto.AttackResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * AttackResponseクラスのテスト
 */
@DisplayName("AttackResponse テスト")
class AttackResponseTest {

    @Test
    @DisplayName("デフォルトコンストラクタ - 空のオブジェクトが作成される")
    void defaultConstructor_CreatesEmptyObject() {
        // Act
        AttackResponse response = new AttackResponse();

        // Assert
        assertEquals(0, response.getNorthDigit());
        assertEquals(0, response.getSouthDigit());
        assertEquals(0, response.getDamage());
        assertNull(response.getMessage());
    }

    @Test
    @DisplayName("パラメータ付きコンストラクタ - 正しい値が設定される")
    void parameterizedConstructor_SetsCorrectValues() {
        // Arrange
        int northDigit = 3;
        int southDigit = 7;
        int damage = 100;
        String message = "通常攻撃";

        // Act
        AttackResponse response = new AttackResponse(northDigit, southDigit, damage, message);

        // Assert
        assertEquals(northDigit, response.getNorthDigit());
        assertEquals(southDigit, response.getSouthDigit());
        assertEquals(damage, response.getDamage());
        assertEquals(message, response.getMessage());
    }

    @Test
    @DisplayName("セッターとゲッター - 値が正しく設定・取得される")
    void settersAndGetters_WorkCorrectly() {
        // Arrange
        AttackResponse response = new AttackResponse();
        int northDigit = 5;
        int southDigit = 9;
        int damage = 200;
        String message = "会心の一撃";

        // Act
        response.setNorthDigit(northDigit);
        response.setSouthDigit(southDigit);
        response.setDamage(damage);
        response.setMessage(message);

        // Assert
        assertEquals(northDigit, response.getNorthDigit());
        assertEquals(southDigit, response.getSouthDigit());
        assertEquals(damage, response.getDamage());
        assertEquals(message, response.getMessage());
    }

    @Test
    @DisplayName("セッターとゲッター - 負の値も正しく設定・取得される")
    void settersAndGetters_WithNegativeValues_WorkCorrectly() {
        // Arrange
        AttackResponse response = new AttackResponse();
        int northDigit = -1;
        int southDigit = -5;
        int damage = -50;
        String message = "ミス";

        // Act
        response.setNorthDigit(northDigit);
        response.setSouthDigit(southDigit);
        response.setDamage(damage);
        response.setMessage(message);

        // Assert
        assertEquals(northDigit, response.getNorthDigit());
        assertEquals(southDigit, response.getSouthDigit());
        assertEquals(damage, response.getDamage());
        assertEquals(message, response.getMessage());
    }
}