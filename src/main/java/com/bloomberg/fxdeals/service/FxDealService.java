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
        // Validate currencies
        validateCurrency(requestDto.getFromCurrency());
        validateCurrency(requestDto.getToCurrency());

        // Check for duplicates
        if (repository.existsByDealUniqueId(requestDto.getDealUniqueId())) {
            throw new DuplicateDealException("Deal with ID " + requestDto.getDealUniqueId() + " already exists");
        }

        // Convert and save
        FxDeal entity = mapper.toEntity(requestDto);
        FxDeal savedEntity = repository.save(entity);
        
        return mapper.toResponseDto(savedEntity);
    }

    public List<FxDealResponseDto> getAllDeals() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponseDto)
                .toList();
    }

    private void validateCurrency(String currency) {
        if (!VALID_CURRENCIES.contains(currency.toUpperCase())) {
            throw new ValidationException("Invalid currency code: " + currency);
        }
    }
}
