package com.example.dockerapi.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@DisplayName("Game Controller Integration Test")
class GameControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("モンスターエンカウンターAPIが正常に動作する")
    void encounterApi_ShouldWork() throws Exception {
        mockMvc.perform(get("/api/encounter"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.appear").exists())
                .andExpect(jsonPath("$.appear").isBoolean());
    }

    @Test
    @DisplayName("確率選択APIが正常に動作する")
    void probabilityApi_ShouldWork() throws Exception {
        mockMvc.perform(get("/api/choose")
                .param("items", "A", "B", "C")
                .param("weights", "0.5", "0.3", "0.2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.anyOf(
                        org.hamcrest.Matchers.equalTo("A"),
                        org.hamcrest.Matchers.equalTo("B"),
                        org.hamcrest.Matchers.equalTo("C"))));
    }

    @Test
    @DisplayName("攻撃APIが正常に動作する")
    void attackApi_ShouldWork() throws Exception {
        mockMvc.perform(get("/api/attack")
                .param("northCoord", "123.456")
                .param("southCoord", "789.012")
                .param("baseAttack", "100"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.northDigit").exists())
                .andExpect(jsonPath("$.southDigit").exists())
                .andExpect(jsonPath("$.damage").exists())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("ホームページが正常に表示される")
    void homePage_ShouldRedirectToIndex() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("不正なパラメータでエラーレスポンスが返される")
    void invalidParameters_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/choose")
                .param("items", "A")
                .param("weights", "0.5", "0.3")) // サイズが一致しない
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
