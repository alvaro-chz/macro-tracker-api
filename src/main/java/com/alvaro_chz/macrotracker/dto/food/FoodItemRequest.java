package com.alvaro_chz.macrotracker.dto.food;

import com.alvaro_chz.macrotracker.model.enums.Category;
import com.alvaro_chz.macrotracker.model.enums.Unit;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.Map;

public record FoodItemRequest(
        @NotBlank(message = "El nombre del alimento es obligatorio.")
        @Size(max = 255, message = "El nombre no debe exceder los 255 caracteres.")
        String name,

        @NotBlank(message = "La categoría del alimento es obligatoria.")
        Category category,

        @NotNull(message = "Las cantidad base es obligatoria.")
        @Positive
        BigDecimal baseServingAmount,

        @NotNull(message = "La unidad es obligatoria.")
        Unit baseServingUnit,

        @NotNull(message = "Las calorías base son obligatorias.")
        @Positive
        BigDecimal baseCalories,

        @Positive
        BigDecimal baseProtein,

        @Positive
        BigDecimal baseCarbs,

        @Positive
        BigDecimal baseFats,

        Map<String, Object> aiMetadata
) {
}
