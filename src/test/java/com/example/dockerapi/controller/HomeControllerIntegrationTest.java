package com.example.dockerapi.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * HealthControllerの統合テスト
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebMvc
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@DisplayName("HealthController 統合テスト")
class HomeControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("ヘルスチェック - OKが返される")
    void health_ShouldReturnOk() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }

    @Test
    @DisplayName("APIルート - 適切なメッセージが返される")
    void apiRoot_ShouldReturnMessage() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api"))
                .andExpect(status().isOk())
                .andExpect(content().string("Draque API is running!"));
    }

    @Test
    @DisplayName("ルートパス - 適切なメッセージが返される")
    void rootPath_ShouldReturnMessage() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string("Draque API is running!"));
    }

    @Test
    @DisplayName("存在しないパス - 404エラーが返される")
    void nonExistentPath_ShouldReturn404() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/non-existent"))
                .andExpect(status().isNotFound());
    }
}