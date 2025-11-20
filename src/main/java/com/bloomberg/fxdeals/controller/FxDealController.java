package com.bloomberg.fxdeals.controller;

import com.bloomberg.fxdeals.dto.request.FxDealRequestDto;
import com.bloomberg.fxdeals.dto.response.FxDealResponseDto;
import com.bloomberg.fxdeals.service.FxDealService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deals")
@RequiredArgsConstructor
@Slf4j
public class FxDealController {

    private final FxDealService service;

    @PostMapping
    public ResponseEntity<FxDealResponseDto> importDeal(@Valid @RequestBody FxDealRequestDto requestDto) {
        FxDealResponseDto response = service.importDeal(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<FxDealResponseDto>> getAllDeals() {
        List<FxDealResponseDto> deals = service.getAllDeals();
        return ResponseEntity.ok(deals);
    }
}
