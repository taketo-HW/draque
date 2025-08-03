package com.example.dockerapi.service;

import com.example.dockerapi.entity.User;
import com.example.dockerapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public User createUser(User user) {
        return repo.save(user);
    }

    public Optional<User> getUserById(Long id) {
        return repo.findById(id);
    }

    public User updateUser(User user) {
        return repo.save(user);
    }

    public void deleteUser(Long id) {
        repo.deleteById(id);
    }
}
