package com.example.dockerapi.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * ユーザーのJPAリポジトリインターフェース
 */
@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    /**
     * メールアドレスでユーザーを検索する
     */
    Optional<UserEntity> findByEmail(String email);

    /**
     * 名前でユーザーを検索する
     */
    java.util.List<UserEntity> findByName(String name);
}
