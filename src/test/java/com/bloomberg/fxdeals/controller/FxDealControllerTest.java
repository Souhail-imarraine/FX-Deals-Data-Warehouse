package com.bloomberg.fxdeals.controller;

import com.bloomberg.fxdeals.dto.request.FxDealRequestDto;
import com.bloomberg.fxdeals.dto.response.FxDealResponseDto;
import com.bloomberg.fxdeals.exception.DuplicateDealException;
import com.bloomberg.fxdeals.exception.ValidationException;
import com.bloomberg.fxdeals.service.FxDealService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FxDealController.class)
class FxDealControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FxDealService service;

    private FxDealRequestDto requestDto;
    private FxDealResponseDto responseDto;

    @BeforeEach
    void setUp() {
        requestDto = new FxDealRequestDto(
                "DEAL001",
                "USD",
                "EUR",
                LocalDateTime.of(2024, 1, 15, 10, 30),
                BigDecimal.valueOf(1000.50)
        );

        responseDto = new FxDealResponseDto(
                1L,
                "DEAL001",
                "USD",
                "EUR",
                LocalDateTime.of(2024, 1, 15, 10, 30),
                BigDecimal.valueOf(1000.50),
                LocalDateTime.now()
        );
    }

    @Test
    void importDeal_Success() throws Exception {
        when(service.importDeal(any(FxDealRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/deals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.dealUniqueId").value("DEAL001"))
                .andExpect(jsonPath("$.fromCurrency").value("USD"));
    }

    @Test
    void importDeal_ValidationError_MissingDealUniqueId() throws Exception {
        requestDto.setDealUniqueId(null);

        mockMvc.perform(post("/api/deals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void importDeal_ValidationError_InvalidCurrencyLength() throws Exception {
        requestDto.setFromCurrency("US");

        mockMvc.perform(post("/api/deals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void importDeal_DuplicateException() throws Exception {
        when(service.importDeal(any(FxDealRequestDto.class)))
                .thenThrow(new DuplicateDealException("Deal already exists"));

        mockMvc.perform(post("/api/deals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isConflict());
    }

    @Test
    void importDeal_ValidationException() throws Exception {
        when(service.importDeal(any(FxDealRequestDto.class)))
                .thenThrow(new ValidationException("Invalid currency"));

        mockMvc.perform(post("/api/deals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllDeals_Success() throws Exception {
        when(service.getAllDeals()).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/api/deals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].dealUniqueId").value("DEAL001"));
    }

    @Test
    void getAllDeals_EmptyList() throws Exception {
        when(service.getAllDeals()).thenReturn(List.of());

        mockMvc.perform(get("/api/deals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}
