package com.bloomberg.fxdeals.service;

import com.bloomberg.fxdeals.dto.request.FxDealRequestDto;
import com.bloomberg.fxdeals.dto.response.FxDealResponseDto;
import com.bloomberg.fxdeals.entity.FxDeal;
import com.bloomberg.fxdeals.exception.DuplicateDealException;
import com.bloomberg.fxdeals.exception.ValidationException;
import com.bloomberg.fxdeals.mapper.FxDealMapper;
import com.bloomberg.fxdeals.repository.FxDealRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FxDealService {

    private final FxDealRepository repository;
    private final FxDealMapper mapper;

    private static final List<String> VALID_CURRENCIES = Arrays.asList(
            "USD", "EUR", "GBP", "JPY", "CHF", "CAD", "AUD", "NZD",
            "CNY", "INR", "BRL", "ZAR", "MXN", "SGD", "HKD", "NOK",
            "SEK", "DKK", "PLN", "TRY", "RUB", "KRW", "THB", "MYR"
    );

    @Transactional
    public FxDealResponseDto importDeal(FxDealRequestDto requestDto) {
        log.info("Processing deal with ID: {}", requestDto.getDealUniqueId());
        
        // Validate currencies
        validateCurrency(requestDto.getFromCurrency());
        validateCurrency(requestDto.getToCurrency());
        log.debug("Currency validation passed for deal: {}", requestDto.getDealUniqueId());

        // Check for duplicates
        if (repository.existsByDealUniqueId(requestDto.getDealUniqueId())) {
            log.warn("Duplicate deal detected: {}", requestDto.getDealUniqueId());
            throw new DuplicateDealException("Deal with ID " + requestDto.getDealUniqueId() + " already exists");
        }

        // Convert and save
        FxDeal entity = mapper.toEntity(requestDto);
        FxDeal savedEntity = repository.save(entity);
        log.info("Deal saved successfully: {}", savedEntity.getDealUniqueId());
        
        return mapper.toResponseDto(savedEntity);
    }

    public List<FxDealResponseDto> getAllDeals() {
        log.info("Fetching all deals from database");
        List<FxDealResponseDto> deals = repository.findAll()
                .stream()
                .map(mapper::toResponseDto)
                .toList();
        log.info("Found {} deals", deals.size());
        return deals;
    }

    private void validateCurrency(String currency) {
        if (!VALID_CURRENCIES.contains(currency.toUpperCase())) {
            log.error("Invalid currency code: {}", currency);
            throw new ValidationException("Invalid currency code: " + currency);
        }
    }
}
