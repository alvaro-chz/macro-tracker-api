package com.alvaro_chz.macrotracker.dto.daily;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DailyLogResponse(
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
