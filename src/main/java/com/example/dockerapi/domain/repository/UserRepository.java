package com.example.dockerapi.domain.repository;

import com.example.dockerapi.domain.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Domain層のUserRepositoryインターフェース
 * Infrastructure層で実装される
 */
public interface UserRepository {
    User save(User user);

    Optional<User> findById(Long id);

    List<User> findAll();

    void deleteById(Long id);

    boolean existsById(Long id);
}
