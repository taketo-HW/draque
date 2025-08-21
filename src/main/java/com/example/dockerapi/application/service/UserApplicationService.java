package com.example.dockerapi.application.service;

import com.example.dockerapi.application.dto.UserDto;
import com.example.dockerapi.domain.model.User;
import com.example.dockerapi.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * ユーザーのアプリケーションサービス
 */
@Service
@Transactional
public class UserApplicationService {

    private final UserRepository userRepository;

    @Autowired
    public UserApplicationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * ユーザーを作成する
     */
    public UserDto createUser(String name, String email) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setLevel(1);
        user.setExperience(0);

        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }

    /**
     * ユーザーを更新する
     */
    public UserDto updateUser(Long id, String name, String email) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setName(name);
            user.setEmail(email);
            User savedUser = userRepository.save(user);
            return convertToDto(savedUser);
        }
        throw new IllegalArgumentException("ユーザーが見つかりません: " + id);
    }

    /**
     * ユーザーに経験値を追加する
     */
    public UserDto addExperience(Long userId, int experience) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.gainExperience(experience);
            User savedUser = userRepository.save(user);
            return convertToDto(savedUser);
        }
        throw new IllegalArgumentException("ユーザーが見つかりません: " + userId);
    }

    /**
     * ユーザーを取得する
     */
    @Transactional(readOnly = true)
    public Optional<UserDto> getUserById(Long id) {
        return userRepository.findById(id).map(this::convertToDto);
    }

    /**
     * メールアドレスでユーザーを取得する
     */
    @Transactional(readOnly = true)
    public Optional<UserDto> getUserByEmail(String email) {
        return userRepository.findByEmail(email).map(this::convertToDto);
    }

    /**
     * 全ユーザーを取得する
     */
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * ユーザーを削除する
     */
    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("ユーザーが見つかりません: " + id);
        }
    }

    /**
     * ドメインモデルをDTOに変換する
     */
    private UserDto convertToDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getLevel(),
                user.getExperience());
    }
}
