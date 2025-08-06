package com.example.dockerapi.repository;

import com.example.dockerapi.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

/**
 * UserRepositoryのテスト
 */
@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.datasource.url=jdbc:h2:mem:testdb"
})
@DisplayName("UserRepository テスト")
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("save - ユーザーが正常に保存される")
    void save_ShouldSaveUserSuccessfully() {
        // Arrange
        User user = new User("テストユーザー");

        // Act
        User savedUser = userRepository.save(user);

        // Assert
        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        assertEquals("テストユーザー", savedUser.getName());
    }

    @Test
    @DisplayName("findById - 存在するユーザーが取得される")
    void findById_WithExistingUser_ShouldReturnUser() {
        // Arrange
        User user = new User("テストユーザー");
        User savedUser = entityManager.persistAndFlush(user);

        // Act
        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals("テストユーザー", foundUser.get().getName());
    }

    @Test
    @DisplayName("findById - 存在しないユーザーの場合、空のOptionalが返される")
    void findById_WithNonExistingUser_ShouldReturnEmptyOptional() {
        // Act
        Optional<User> foundUser = userRepository.findById(999L);

        // Assert
        assertFalse(foundUser.isPresent());
    }

    @Test
    @DisplayName("deleteById - ユーザーが正常に削除される")
    void deleteById_ShouldDeleteUserSuccessfully() {
        // Arrange
        User user = new User("削除対象ユーザー");
        User savedUser = entityManager.persistAndFlush(user);
        Long userId = savedUser.getId();

        // Act
        userRepository.deleteById(userId);

        // Assert
        Optional<User> deletedUser = userRepository.findById(userId);
        assertFalse(deletedUser.isPresent());
    }

    @Test
    @DisplayName("save - 既存ユーザーの更新")
    void save_WithExistingUser_ShouldUpdateUser() {
        // Arrange
        User user = new User("元の名前");
        User savedUser = entityManager.persistAndFlush(user);

        // Act
        savedUser.setName("更新された名前");
        User updatedUser = userRepository.save(savedUser);

        // Assert
        assertEquals("更新された名前", updatedUser.getName());
        assertEquals(savedUser.getId(), updatedUser.getId());
    }
}