package com.bloomberg.fxdeals.mapper;

import com.bloomberg.fxdeals.dto.request.FxDealRequestDto;
import com.bloomberg.fxdeals.dto.response.FxDealResponseDto;
import com.bloomberg.fxdeals.entity.FxDeal;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FxDealMapper {

    FxDeal toEntity(FxDealRequestDto requestDto);

    FxDealResponseDto toResponseDto(FxDeal entity);
}
