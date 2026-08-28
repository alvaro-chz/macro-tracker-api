package com.alvaro_chz.macrotracker.dto.food;

import com.alvaro_chz.macrotracker.model.enums.Category;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record RecipeRequest(
        @NotBlank(message = "El nombre es obligatorio.")
        @Size(max = 255, message = "El nombre no debe exceder los 255 caracteres.")
        String name,

        @NotBlank(message = "La categoría es obligatoria.")
        Category category,

        @NotEmpty(message = "Los componentes de la comida son obligatorias.")
        List<@Valid ComponentDto> components
) {
}
