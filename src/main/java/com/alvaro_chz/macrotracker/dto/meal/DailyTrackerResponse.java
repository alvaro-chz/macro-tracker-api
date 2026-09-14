package com.alvaro_chz.macrotracker.dto.meal;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record DailyTrackerResponse(
        LocalDate date,
        List<MealLogResponse> logs,
        BigDecimal totalCalories,
        BigDecimal totalProtein,
        BigDecimal totalCarbs,
        BigDecimal totalFats
) {
}
