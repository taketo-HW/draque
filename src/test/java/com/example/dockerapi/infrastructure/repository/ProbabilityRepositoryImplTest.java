package com.example.dockerapi.infrastructure.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProbabilityRepositoryImpl テスト")
class ProbabilityRepositoryImplTest {

    private ProbabilityRepositoryImpl probabilityRepository;

    @BeforeEach
    void setUp() {
        probabilityRepository = new ProbabilityRepositoryImpl(new Random(0));
    }

    @Test
    @DisplayName("selectByProbability - 正常な選択")
    void selectByProbability_WithValidInput_ReturnsSelectedItem() {
        // Arrange
        List<String> items = Arrays.asList("A", "B", "C");
        List<Double> weights = Arrays.asList(0.5, 0.3, 0.2);

        // Act
        String result = probabilityRepository.selectByProbability(items, weights);

        // Assert
        assertNotNull(result);
        assertTrue(items.contains(result));
    }

    @Test
    @DisplayName("selectByProbability - 単一要素の選択")
    void selectByProbability_WithSingleItem_ReturnsThatItem() {
        // Arrange
        List<String> items = Arrays.asList("OnlyItem");
        List<Double> weights = Arrays.asList(1.0);

        // Act
        String result = probabilityRepository.selectByProbability(items, weights);

        // Assert
        assertEquals("OnlyItem", result);
    }

    @Test
    @DisplayName("selectByProbability - nullのitemsでIllegalArgumentException")
    void selectByProbability_WithNullItems_ThrowsException() {
        // Arrange
        List<Double> weights = Arrays.asList(1.0);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            probabilityRepository.selectByProbability(null, weights));
    }

    @Test
    @DisplayName("selectByProbability - nullのweightsでIllegalArgumentException")
    void selectByProbability_WithNullWeights_ThrowsException() {
        // Arrange
        List<String> items = Arrays.asList("A");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            probabilityRepository.selectByProbability(items, null));
    }

    @Test
    @DisplayName("selectByProbability - サイズが異なるリストでIllegalArgumentException")
    void selectByProbability_WithDifferentSizes_ThrowsException() {
        // Arrange
        List<String> items = Arrays.asList("A", "B");
        List<Double> weights = Arrays.asList(1.0);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> probabilityRepository.selectByProbability(items, weights));
    }

    @Test
    @DisplayName("selectByProbability - 空のリストでIllegalArgumentException")
    void selectByProbability_WithEmptyLists_ThrowsException() {
        // Arrange
        List<String> items = Collections.emptyList();
        List<Double> weights = Collections.emptyList();

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> probabilityRepository.selectByProbability(items, weights));
    }

    @Test
    @DisplayName("selectByProbability - 負の重みでIllegalArgumentException")
    void selectByProbability_WithNegativeWeight_ThrowsException() {
        // Arrange
        List<String> items = Arrays.asList("A", "B");
        List<Double> weights = Arrays.asList(1.0, -0.5);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> probabilityRepository.selectByProbability(items, weights));
    }

    @Test
    @DisplayName("selectByProbability - 合計重みが0でIllegalArgumentException")
    void selectByProbability_WithZeroTotalWeight_ThrowsException() {
        // Arrange
        List<String> items = Arrays.asList("A", "B");
        List<Double> weights = Arrays.asList(0.0, 0.0);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> probabilityRepository.selectByProbability(items, weights));
    }

    @Test
    @DisplayName("selectByProbability - 確率分布のテスト")
    void selectByProbability_ProbabilityDistribution_WorksCorrectly() {
        // Arrange
        List<String> items = Arrays.asList("A", "B", "C");
        List<Double> weights = Arrays.asList(0.7, 0.2, 0.1);
        Map<String, Integer> counts = new HashMap<>();

        // 固定シードで多数回実行
        probabilityRepository = new ProbabilityRepositoryImpl(new Random(42));

        // Act
        for (int i = 0; i < 1000; i++) {
            String result = probabilityRepository.selectByProbability(items, weights);
            counts.put(result, counts.getOrDefault(result, 0) + 1);
        }

        // Assert
        // Aが最も多く選ばれることを確認
        assertTrue(counts.get("A") > counts.get("B"));
        assertTrue(counts.get("B") > counts.get("C"));

        // すべての要素が少なくとも1回は選ばれることを確認
        assertEquals(3, counts.size());
        counts.values().forEach(count -> assertTrue(count > 0));
    }
}
