package com.bloomberg.fxdeals.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FxDealRequestDto {

    @NotBlank(message = "Deal Unique ID is required")
    private String dealUniqueId;

    @NotBlank(message = "From Currency is required")
    @Size(min = 3, max = 3, message = "Currency code must be 3 characters")
    private String fromCurrency;

    @NotBlank(message = "To Currency is required")
    @Size(min = 3, max = 3, message = "Currency code must be 3 characters")
    private String toCurrency;

    @NotNull(message = "Deal Timestamp is required")
    private LocalDateTime dealTimestamp;

    @NotNull(message = "Deal Amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
    private BigDecimal dealAmount;
}
