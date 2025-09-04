package com.example.dockerapi.infrastructure.persistence;

import com.example.dockerapi.domain.entity.User;
import com.example.dockerapi.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

/**
 * InMemoryUserRepositoryのテスト
 */
@SpringBootTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.datasource.url=jdbc:h2:mem:testdb"
})
@DisplayName("InMemoryUserRepository テスト")
class InMemoryUserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("save - ユーザーが正常に保存される")
    void save_ShouldSaveUserSuccessfully() {
        // Arrange
        User user = new User(null, "テストユーザー", "test@example.com");

        // Act
        User savedUser = userRepository.save(user);

        // Assert
        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        assertEquals("テストユーザー", savedUser.getName());
        assertEquals("test@example.com", savedUser.getEmail());
    }

    @Test
    @DisplayName("findById - 存在するユーザーが取得される")
    void findById_WithExistingUser_ShouldReturnUser() {
        // Arrange
        User user = new User(null, "テストユーザー", "test@example.com");
        User savedUser = userRepository.save(user);

        // Act
        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals("テストユーザー", foundUser.get().getName());
        assertEquals("test@example.com", foundUser.get().getEmail());
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
    @DisplayName("findAll - 全てのユーザーが取得される")
    void findAll_ShouldReturnAllUsers() {
        // Arrange
        User user1 = new User(null, "ユーザー1", "user1@example.com");
        User user2 = new User(null, "ユーザー2", "user2@example.com");
        userRepository.save(user1);
        userRepository.save(user2);

        // Act
        List<User> allUsers = userRepository.findAll();

        // Assert
        assertTrue(allUsers.size() >= 2);
        assertTrue(allUsers.stream().anyMatch(u -> u.getName().equals("ユーザー1")));
        assertTrue(allUsers.stream().anyMatch(u -> u.getName().equals("ユーザー2")));
    }

    @Test
    @DisplayName("deleteById - ユーザーが正常に削除される")
    void deleteById_ShouldDeleteUserSuccessfully() {
        // Arrange
        User user = new User(null, "削除対象ユーザー", "delete@example.com");
        User savedUser = userRepository.save(user);
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
        User user = new User(null, "元の名前", "original@example.com");
        User savedUser = userRepository.save(user);

        // Act
        savedUser.setName("更新された名前");
        savedUser.setEmail("updated@example.com");
        User updatedUser = userRepository.save(savedUser);

        // Assert
        assertEquals("更新された名前", updatedUser.getName());
        assertEquals("updated@example.com", updatedUser.getEmail());
        assertEquals(savedUser.getId(), updatedUser.getId());
    }
}
