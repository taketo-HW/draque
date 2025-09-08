package com.example.dockerapi.infrastructure.repository;

import com.example.dockerapi.domain.model.User;
import com.example.dockerapi.domain.repository.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Infrastructure層のUserRepository実装
 * Domain層のインターフェースを実装し、JPAエンティティを使用
 */
// @Repository
// @Lazy
public class UserRepositoryImpl implements UserRepository {
    private final com.example.dockerapi.repository.UserRepository jpaUserRepository;

    public UserRepositoryImpl(com.example.dockerapi.repository.UserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public User save(User user) {
        com.example.dockerapi.entity.User entity = convertToEntity(user);
        com.example.dockerapi.entity.User savedEntity = jpaUserRepository.save(entity);
        return convertToDomain(savedEntity);
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpaUserRepository.findById(id)
                .map(this::convertToDomain);
    }

    @Override
    public List<User> findAll() {
        return jpaUserRepository.findAll().stream()
                .map(this::convertToDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        jpaUserRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaUserRepository.existsById(id);
    }

    private com.example.dockerapi.entity.User convertToEntity(User domainUser) {
        com.example.dockerapi.entity.User entity = new com.example.dockerapi.entity.User();
        entity.setId(domainUser.getId());
        entity.setName(domainUser.getName());
        entity.setCreatedAt(domainUser.getCreatedAt());
        entity.setUpdatedAt(domainUser.getUpdatedAt());
        return entity;
    }

    private User convertToDomain(com.example.dockerapi.entity.User entity) {
        return new User(
                entity.getId(),
                entity.getName(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}
