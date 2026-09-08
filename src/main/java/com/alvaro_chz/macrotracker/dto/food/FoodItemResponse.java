package com.alvaro_chz.macrotracker.dto.food;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FoodItemResponse(
        Long id,
        String name,
        String category,
        BigDecimal baseServingAmount,
        String baseServingUnit,
        BigDecimal baseCalories,
        BigDecimal baseProtein,
        BigDecimal baseCarbs,
        BigDecimal baseFats,
        String aiMetadata,
        LocalDateTime createdAt
) {
}
