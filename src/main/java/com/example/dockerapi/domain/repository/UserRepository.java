package com.example.dockerapi.domain.repository;

import com.example.dockerapi.domain.model.User;
import java.util.List;
import java.util.Optional;

/**
 * ユーザーリポジトリのインターフェース
 */
public interface UserRepository {

    /**
     * ユーザーを保存する
     */
    User save(User user);

    /**
     * IDでユーザーを検索する
     */
    Optional<User> findById(Long id);

    /**
     * メールアドレスでユーザーを検索する
     */
    Optional<User> findByEmail(String email);

    /**
     * 名前でユーザーを検索する
     */
    List<User> findByName(String name);

    /**
     * 全ユーザーを取得する
     */
    List<User> findAll();

    /**
     * ユーザーを削除する
     */
    void deleteById(Long id);

    /**
     * ユーザーが存在するかチェックする
     */
    boolean existsById(Long id);
}
