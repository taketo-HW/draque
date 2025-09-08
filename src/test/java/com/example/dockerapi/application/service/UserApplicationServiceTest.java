package com.example.dockerapi.application.service;

import com.example.dockerapi.domain.model.User;
import com.example.dockerapi.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

/**
 * UserApplicationServiceのテスト
 */
@DisplayName("UserApplicationService テスト")
class UserApplicationServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserApplicationService userApplicationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("createUser - ユーザーが正常に作成される")
    void createUser_ShouldCreateUserSuccessfully() {
        // Arrange
        User user = new User("テストユーザー");

        User savedUser = new User("テストユーザー");
        // IDはJPAが自動生成するため、テストでは設定しない

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        User result = userApplicationService.createUser(user);

        // Assert
        assertNotNull(result);
        assertEquals("テストユーザー", result.getName());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("createUser - nullユーザーでIllegalArgumentException")
    void createUser_WithNullUser_ThrowsException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userApplicationService.createUser(null));
    }

    @Test
    @DisplayName("createUser - 空の名前でIllegalArgumentException")
    void createUser_WithEmptyName_ThrowsException() {
        // Arrange
        User user = new User("");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userApplicationService.createUser(user));
    }

    @Test
    @DisplayName("getUserById - 存在するユーザーが取得される")
    void getUserById_WithExistingUser_ShouldReturnUser() {
        // Arrange
        Long userId = 1L;
        User user = new User("テストユーザー");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userApplicationService.getUserById(userId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("テストユーザー", result.get().getName());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("getUserById - 存在しないユーザーの場合、空のOptionalが返される")
    void getUserById_WithNonExistingUser_ShouldReturnEmptyOptional() {
        // Arrange
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userApplicationService.getUserById(userId);

        // Assert
        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("getUserById - nullのIDでIllegalArgumentException")
    void getUserById_WithNullId_ThrowsException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userApplicationService.getUserById(null));
    }

    @Test
    @DisplayName("updateUser - ユーザーが正常に更新される")
    void updateUser_ShouldUpdateUserSuccessfully() {
        // Arrange
        User user = new User("更新されたユーザー");
        user.setId(1L);

        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User result = userApplicationService.updateUser(user);

        // Assert
        assertNotNull(result);
        assertEquals("更新されたユーザー", result.getName());
        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("deleteUser - ユーザーが正常に削除される")
    void deleteUser_ShouldDeleteUserSuccessfully() {
        // Arrange
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);
        doNothing().when(userRepository).deleteById(userId);

        // Act
        userApplicationService.deleteUser(userId);

        // Assert
        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }
}
