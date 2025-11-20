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
        log.info("Received request to import deal: {}", requestDto.getDealUniqueId());
        FxDealResponseDto response = service.importDeal(requestDto);
        log.info("Deal imported successfully: {}", requestDto.getDealUniqueId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<FxDealResponseDto>> getAllDeals() {
        log.info("Received request to fetch all deals");
        List<FxDealResponseDto> deals = service.getAllDeals();
        log.info("Returning {} deals", deals.size());
        return ResponseEntity.ok(deals);
    }
}
