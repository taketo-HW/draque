package com.example.dockerapi.domain.repository;

import com.example.dockerapi.domain.model.Monster;

import java.util.List;
import java.util.Optional;

/**
 * Domain層のMonsterRepositoryインターフェース
 * Infrastructure層で実装される
 */
public interface MonsterRepository {
    List<Monster> findAll();

    Optional<Monster> executeEncounter(List<Monster> monsters);
}
