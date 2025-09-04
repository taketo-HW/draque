package com.example.dockerapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
    "com.example.dockerapi.domain.service",
    "com.example.dockerapi.application.usecase",
    "com.example.dockerapi.infrastructure.persistence",
    "com.example.dockerapi.infrastructure.web.controller",
    "com.example.dockerapi.controller"
})
public class DockerApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(DockerApiApplication.class, args);
    }
}
