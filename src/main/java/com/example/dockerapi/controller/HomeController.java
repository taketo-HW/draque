package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    /**
     * ルートにアクセスが来たら、
     * 静的リソース static/index.html を返す（forward）
     */
    @GetMapping("/")
    public String home() {
        return "forward:/index.html";
    }
}
