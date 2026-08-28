package com.alvaro_chz.macrotracker.dto.food;

import com.alvaro_chz.macrotracker.model.enums.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record FoodItemRequest(
        @NotBlank(message = "El nombre del alimento es obligatorio.")
        @Size(max = 255, message = "El nombre no debe exceder los 255 caracteres.")
        String name,

        @NotBlank(message = "El categoría del alimento es obligatoria.")
        Category category,

        @NotNull(message = "Las calorías base son obligatorias.")
        @Positive
        BigDecimal baseCalories,

        @Positive
        BigDecimal baseProtein,

        @Positive
        BigDecimal baseCarbs,

        @Positive
        BigDecimal baseFats
) {
}
