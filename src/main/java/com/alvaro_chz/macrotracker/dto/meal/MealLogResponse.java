package com.alvaro_chz.macrotracker.dto.meal;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MealLogResponse(
        Long id,
        LocalDateTime consumedAt,
        String mealType,
        Long foodItemId,
        String foodName,
        BigDecimal servingSize,
        BigDecimal calculatedCalories,
        BigDecimal calculatedProtein,
        BigDecimal calculatedCarbs,
        BigDecimal calculatedFats
) {
}
