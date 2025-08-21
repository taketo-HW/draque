package com.example.dockerapi.infrastructure.persistence;

import com.example.dockerapi.domain.model.User;
import com.example.dockerapi.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * ユーザーリポジトリの実装クラス
 */
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Autowired
    public UserRepositoryImpl(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public User save(User user) {
        UserEntity entity = convertToEntity(user);
        UserEntity savedEntity = userJpaRepository.save(entity);
        return convertToDomain(savedEntity);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id).map(this::convertToDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email).map(this::convertToDomain);
    }

    @Override
    public List<User> findByName(String name) {
        return userJpaRepository.findByName(name).stream()
                .map(this::convertToDomain)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll().stream()
                .map(this::convertToDomain)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        userJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return userJpaRepository.existsById(id);
    }

    /**
     * ドメインモデルをエンティティに変換する
     */
    private UserEntity convertToEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setName(user.getName());
        entity.setEmail(user.getEmail());
        entity.setLevel(user.getLevel());
        entity.setExperience(user.getExperience());
        return entity;
    }

    /**
     * エンティティをドメインモデルに変換する
     */
    private User convertToDomain(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getLevel(),
                entity.getExperience());
    }
}
