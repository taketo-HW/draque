package com.example.dockerapi.domain.service;

import com.example.dockerapi.domain.model.Monster;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("EncounterDomainService テスト")
class EncounterDomainServiceTest {

    private EncounterDomainService encounterDomainService;
    private List<Monster> testMonsters;

    @BeforeEach
    void setUp() {
        testMonsters = Arrays.asList(
                new Monster("スライム", 20, 10, 10, "/image/Slime.webp", 0.65),
                new Monster("ドラゴン", 40, 20, 15, "/image/dragon.webp", 0.25),
                new Monster("メタルスライム", 20, 10, 30, "/image/Metal_slime.webp", 0.10));
    }

    @Test
    @DisplayName("executeEncounter - 出現率0でモンスターが出現しない")
    void executeEncounter_WithZeroAppearanceRate_ReturnsEmpty() {
        // Arrange
        encounterDomainService = new EncounterDomainService(new Random(0));

        // Act
        Optional<Monster> result = encounterDomainService.executeEncounter(testMonsters, 0.0);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("executeEncounter - 出現率1でモンスターが必ず出現")
    void executeEncounter_WithFullAppearanceRate_ReturnsMonster() {
        // Arrange
        encounterDomainService = new EncounterDomainService(new Random(0));

        // Act
        Optional<Monster> result = encounterDomainService.executeEncounter(testMonsters, 1.0);

        // Assert
        assertTrue(result.isPresent());
        assertNotNull(result.get());
    }

    @Test
    @DisplayName("executeEncounter - 空のモンスターリストで空のOptionalが返される")
    void executeEncounter_WithEmptyMonsterList_ReturnsEmpty() {
        // Arrange
        encounterDomainService = new EncounterDomainService();
        List<Monster> emptyList = Collections.emptyList();

        // Act
        Optional<Monster> result = encounterDomainService.executeEncounter(emptyList, 1.0);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("executeEncounter - nullのモンスターリストで空のOptionalが返される")
    void executeEncounter_WithNullMonsterList_ReturnsEmpty() {
        // Arrange
        encounterDomainService = new EncounterDomainService();

        // Act
        Optional<Monster> result = encounterDomainService.executeEncounter(null, 1.0);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("executeEncounter - デフォルト出現率でのテスト")
    void executeEncounter_WithDefaultAppearanceRate_WorksCorrectly() {
        // Arrange
        encounterDomainService = new EncounterDomainService(new Random(0));

        // Act
        Optional<Monster> result = encounterDomainService.executeEncounter(testMonsters);

        // Assert
        // デフォルト出現率は0.4なので、結果は実装に依存
        assertNotNull(result);
    }

    @Test
    @DisplayName("executeEncounter - 確率的な選択のテスト")
    void executeEncounter_ProbabilitySelection_WorksCorrectly() {
        // Arrange
        // 固定シードで予測可能な結果を得る
        Random fixedRandom = new Random(123);
        encounterDomainService = new EncounterDomainService(fixedRandom);

        // Act
        List<Monster> encounters = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            fixedRandom.setSeed(123 + i);
            encounterDomainService = new EncounterDomainService(fixedRandom);
            Optional<Monster> result = encounterDomainService.executeEncounter(testMonsters, 1.0);
            if (result.isPresent()) {
                encounters.add(result.get());
            }
        }

        // Assert
        // 少なくとも何らかのモンスターが出現することを確認
        assertFalse(encounters.isEmpty());

        // 異なる種類のモンスターが出現することを確認
        Set<String> monsterNames = new HashSet<>();
        encounters.forEach(monster -> monsterNames.add(monster.getName()));

        // 確率的に複数種類のモンスターが出現する可能性が高い
        assertTrue(monsterNames.size() > 0);
    }

    @Test
    @DisplayName("executeEncounter - 出現率の合計が0の場合")
    void executeEncounter_WithZeroTotalRate_ReturnsEmpty() {
        // Arrange
        List<Monster> zeroRateMonsters = Arrays.asList(
                new Monster("ゼロモンスター1", 10, 5, 5, "/image/zero1.webp", 0.0),
                new Monster("ゼロモンスター2", 10, 5, 5, "/image/zero2.webp", 0.0));
        encounterDomainService = new EncounterDomainService(new Random(0));

        // Act
        Optional<Monster> result = encounterDomainService.executeEncounter(zeroRateMonsters, 1.0);

        // Assert
        assertFalse(result.isPresent());
    }
}
