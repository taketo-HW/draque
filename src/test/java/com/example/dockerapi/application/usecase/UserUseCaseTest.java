package com.example.dockerapi.application.usecase;

import com.example.dockerapi.application.dto.UserDto;
import com.example.dockerapi.domain.entity.User;
import com.example.dockerapi.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * UserUseCaseのテスト
 */
@DisplayName("UserUseCase テスト")
class UserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserUseCase userUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("createUser - ユーザーが正常に作成される")
    void createUser_ShouldCreateUserSuccessfully() {
        // Arrange
        String name = "テストユーザー";
        String email = "test@example.com";
        LocalDateTime now = LocalDateTime.now();
        
        User user = new User(null, name, email);
        User savedUser = new User(1L, name, email);
        savedUser.setCreatedAt(now);
        savedUser.setUpdatedAt(now);
        
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        
        // Act
        UserDto result = userUseCase.createUser(name, email);
        
        // Assert
        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(email, result.getEmail());
        assertEquals(now, result.getCreatedAt());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("getUserById - 存在するユーザーが取得される")
    void getUserById_WithExistingUser_ShouldReturnUser() {
        // Arrange
        Long userId = 1L;
        String name = "テストユーザー";
        String email = "test@example.com";
        LocalDateTime now = LocalDateTime.now();
        
        User user = new User(userId, name, email);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        
        // Act
        Optional<UserDto> result = userUseCase.getUserById(userId);
        
        // Assert
        assertTrue(result.isPresent());
        assertEquals(name, result.get().getName());
        assertEquals(email, result.get().getEmail());
        assertEquals(now, result.get().getCreatedAt());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("getUserById - 存在しないユーザーの場合、空のOptionalが返される")
    void getUserById_WithNonExistingUser_ShouldReturnEmptyOptional() {
        // Arrange
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        
        // Act
        Optional<UserDto> result = userUseCase.getUserById(userId);
        
        // Assert
        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("getAllUsers - 全てのユーザーが取得される")
    void getAllUsers_ShouldReturnAllUsers() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        User user1 = new User(1L, "ユーザー1", "user1@example.com");
        user1.setCreatedAt(now);
        user1.setUpdatedAt(now);
        
        User user2 = new User(2L, "ユーザー2", "user2@example.com");
        user2.setCreatedAt(now);
        user2.setUpdatedAt(now);
        
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));
        
        // Act
        List<UserDto> result = userUseCase.getAllUsers();
        
        // Assert
        assertEquals(2, result.size());
        assertEquals("ユーザー1", result.get(0).getName());
        assertEquals("ユーザー2", result.get(1).getName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("deleteUser - ユーザーが正常に削除される")
    void deleteUser_ShouldDeleteUserSuccessfully() {
        // Arrange
        Long userId = 1L;
        doNothing().when(userRepository).deleteById(userId);
        
        // Act
        userUseCase.deleteUser(userId);
        
        // Assert
        verify(userRepository, times(1)).deleteById(userId);
    }
}
