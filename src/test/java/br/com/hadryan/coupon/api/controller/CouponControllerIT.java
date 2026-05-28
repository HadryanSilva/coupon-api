package br.com.hadryan.coupon.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CouponControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateAndGetCouponById() throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put("code", "promo-2026");
        payload.put("description", "Integracao");
        payload.put("discountValue", BigDecimal.valueOf(0.75));
        payload.put("expirationDate", LocalDateTime.now().plusDays(5));
        payload.put("published", true);

        String createResponse = mockMvc.perform(post("/api/v1/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("PROMO2"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String id = objectMapper.readTree(createResponse).get("id").asText();

        mockMvc.perform(get("/api/v1/coupons/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.code").value("PROMO2"));
    }

    @Test
    void shouldDeleteCouponAndReturnNoContent() throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put("code", "del-2026");
        payload.put("description", "Delete");
        payload.put("discountValue", BigDecimal.valueOf(0.85));
        payload.put("expirationDate", LocalDateTime.now().plusDays(7));
        payload.put("published", false);

        String createResponse = mockMvc.perform(post("/api/v1/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String id = objectMapper.readTree(createResponse).get("id").asText();

        mockMvc.perform(delete("/api/v1/coupons/{id}", id))
                .andExpect(status().isNoContent());
    }
}