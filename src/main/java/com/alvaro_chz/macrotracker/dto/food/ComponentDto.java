package com.alvaro_chz.macrotracker.dto.food;

import com.alvaro_chz.macrotracker.model.enums.Unit;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ComponentDto(
        @NotNull(message = "El Id del alimento es obligatorio.")
        Long childFoodId,

        @Positive
        BigDecimal portionAmount,

        @NotEmpty(message = "La unidad es obligatoria.")
        Unit unit
) {
}
