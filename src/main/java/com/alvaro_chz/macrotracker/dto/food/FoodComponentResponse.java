package com.alvaro_chz.macrotracker.dto.food;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FoodComponentResponse(
        String name,
        String category,
        BigDecimal baseCalories,
        BigDecimal baseProtein,
        BigDecimal baseCarbs,
        BigDecimal baseFats,
        BigDecimal portionAmount,
        String unit,
        LocalDateTime createdAt
) {
}
