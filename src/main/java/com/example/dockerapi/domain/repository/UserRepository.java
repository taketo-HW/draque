package com.example.dockerapi.domain.repository;

import com.example.dockerapi.domain.entity.User;
import java.util.List;
import java.util.Optional;

/**
 * ユーザーリポジトリインターフェース
 */
public interface UserRepository {
    User save(User user);

    Optional<User> findById(Long id);

    List<User> findAll();

    void deleteById(Long id);
}
