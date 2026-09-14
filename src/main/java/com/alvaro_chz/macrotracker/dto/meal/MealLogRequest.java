package com.alvaro_chz.macrotracker.dto.meal;

import com.alvaro_chz.macrotracker.model.enums.MealType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record MealLogRequest(
        @NotNull(message = "El ID del alimento es obligatorio.")
        Long foodItemId,

        @NotNull(message = "El tipo de comida es obligatorio.")
        MealType mealType,

        @Positive
        BigDecimal servingSize,

        @NotNull(message = "La fecha es obligatoria.")
        LocalDateTime logDate
) {
}