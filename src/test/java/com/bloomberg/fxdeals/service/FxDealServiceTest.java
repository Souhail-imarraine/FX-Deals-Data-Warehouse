package com.bloomberg.fxdeals.service;

import com.bloomberg.fxdeals.dto.request.FxDealRequestDto;
import com.bloomberg.fxdeals.dto.response.FxDealResponseDto;
import com.bloomberg.fxdeals.entity.FxDeal;
import com.bloomberg.fxdeals.exception.DuplicateDealException;
import com.bloomberg.fxdeals.exception.ValidationException;
import com.bloomberg.fxdeals.mapper.FxDealMapper;
import com.bloomberg.fxdeals.repository.FxDealRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FxDealServiceTest {

    @Mock
    private FxDealRepository repository;

    @Mock
    private FxDealMapper mapper;

    @InjectMocks
    private FxDealService service;

    private FxDealRequestDto requestDto;
    private FxDeal entity;
    private FxDealResponseDto responseDto;

    @BeforeEach
    void setUp() {
        requestDto = new FxDealRequestDto(
                "DEAL001",
                "USD",
                "EUR",
                LocalDateTime.now(),
                BigDecimal.valueOf(1000.50)
        );

        entity = new FxDeal();
        entity.setId(1L);
        entity.setDealUniqueId("DEAL001");
        entity.setFromCurrency("USD");
        entity.setToCurrency("EUR");
        entity.setDealTimestamp(LocalDateTime.now());
        entity.setDealAmount(BigDecimal.valueOf(1000.50));

        responseDto = new FxDealResponseDto(
                1L,
                "DEAL001",
                "USD",
                "EUR",
                LocalDateTime.now(),
                BigDecimal.valueOf(1000.50),
                LocalDateTime.now()
        );
    }

    @Test
    void importDeal_Success() {
        // Arrange
        when(repository.existsByDealUniqueId("DEAL001")).thenReturn(false);
        when(mapper.toEntity(requestDto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toResponseDto(entity)).thenReturn(responseDto);

        // Act
        FxDealResponseDto result = service.importDeal(requestDto);

        assertNotNull(result);
        assertEquals("DEAL001", result.getDealUniqueId());
        verify(repository).existsByDealUniqueId("DEAL001");
        verify(repository).save(entity);
    }

    @Test
    void importDeal_ThrowsDuplicateException() {
        when(repository.existsByDealUniqueId("DEAL001")).thenReturn(true);

        assertThrows(DuplicateDealException.class, () -> service.importDeal(requestDto));
        verify(repository).existsByDealUniqueId("DEAL001");
        verify(repository, never()).save(any());
    }

    @Test
    void importDeal_ThrowsValidationException_InvalidFromCurrency() {
        requestDto.setFromCurrency("XXX");

        assertThrows(ValidationException.class, () -> service.importDeal(requestDto));
        verify(repository, never()).save(any());
    }

    @Test
    void importDeal_ThrowsValidationException_InvalidToCurrency() {
        // Arrange
        requestDto.setToCurrency("YYY");

        // Act & Assert
        assertThrows(ValidationException.class, () -> service.importDeal(requestDto));
        verify(repository, never()).save(any());
    }

    @Test
    void getAllDeals_Success() {
        when(repository.findAll()).thenReturn(List.of(entity, entity));
        when(mapper.toResponseDto(any())).thenReturn(responseDto);

        List<FxDealResponseDto> result = service.getAllDeals();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository).findAll();
    }

    @Test
    void getAllDeals_EmptyList() {
        when(repository.findAll()).thenReturn(List.of());

        List<FxDealResponseDto> result = service.getAllDeals();

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(repository).findAll();
    }
}
