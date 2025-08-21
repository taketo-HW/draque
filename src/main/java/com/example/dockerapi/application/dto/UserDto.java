package com.example.dockerapi.application.dto;

import java.io.Serializable;

/**
 * ユーザー情報のDTO
 */
public class UserDto implements Serializable {

    private Long id;
    private String name;
    private String email;
    private int level;
    private int experience;

    public UserDto() {
    }

    public UserDto(Long id, String name, String email, int level, int experience) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.level = level;
        this.experience = experience;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", level=" + level +
                ", experience=" + experience +
                '}';
    }
}
