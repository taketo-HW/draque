package com.example.dockerapi.infrastructure.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ヘルスチェック用のコントローラー
 */
@RestController
public class HealthController {

    /**
     * ヘルスチェックエンドポイント
     */
    @GetMapping("/health")
    public String health() {
        return "OK";
    }

    /**
     * ルートエンドポイント
     */
    @GetMapping("/api")
    public String root() {
        return "Draque API is running!";
    }

    /**
     * ルートパス
     */
    @GetMapping("/")
    public String home() {
        return "Draque API is running!";
    }
}
