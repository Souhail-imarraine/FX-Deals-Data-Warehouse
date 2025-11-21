package com.bloomberg.fxdeals.mapper;

import com.bloomberg.fxdeals.dto.request.FxDealRequestDto;
import com.bloomberg.fxdeals.dto.response.FxDealResponseDto;
import com.bloomberg.fxdeals.entity.FxDeal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FxDealMapperTest {

    @Autowired
    private FxDealMapper mapper;

    @Test
    void toEntity_Success() {
        FxDealRequestDto requestDto = new FxDealRequestDto(
                "DEAL001", "USD", "EUR",
                LocalDateTime.of(2024, 1, 15, 10, 30),
                BigDecimal.valueOf(1000.50)
        );

        FxDeal entity = mapper.toEntity(requestDto);

        assertNotNull(entity);
        assertEquals("DEAL001", entity.getDealUniqueId());
        assertEquals("USD", entity.getFromCurrency());
        assertEquals(BigDecimal.valueOf(1000.50), entity.getDealAmount());
    }

    @Test
    void toResponseDto_Success() {
        FxDeal entity = new FxDeal();
        entity.setId(1L);
        entity.setDealUniqueId("DEAL001");
        entity.setFromCurrency("USD");
        entity.setToCurrency("EUR");
        entity.setDealTimestamp(LocalDateTime.of(2024, 1, 15, 10, 30));
        entity.setDealAmount(BigDecimal.valueOf(1000.50));
        entity.setCreatedAt(LocalDateTime.now());

        FxDealResponseDto responseDto = mapper.toResponseDto(entity);

        assertNotNull(responseDto);
        assertEquals(1L, responseDto.getId());
        assertEquals("DEAL001", responseDto.getDealUniqueId());
        assertEquals(BigDecimal.valueOf(1000.50), responseDto.getDealAmount());
    }
}
