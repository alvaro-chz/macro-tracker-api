package com.alvaro_chz.macrotracker.service.impl;

import com.alvaro_chz.macrotracker.dto.food.*;
import com.alvaro_chz.macrotracker.model.FoodComponent;
import com.alvaro_chz.macrotracker.model.FoodItem;
import com.alvaro_chz.macrotracker.model.User;
import com.alvaro_chz.macrotracker.model.enums.Unit;
import com.alvaro_chz.macrotracker.repository.FoodComponentRepository;
import com.alvaro_chz.macrotracker.repository.FoodItemRepository;
import com.alvaro_chz.macrotracker.repository.UserRepository;
import com.alvaro_chz.macrotracker.service.FoodItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional

public class FoodItemServiceImpl implements FoodItemService {
    private final UserRepository userRepository;
    private final FoodItemRepository foodItemRepository;
    private final FoodComponentRepository foodComponentRepository;

    @Override
    public FoodItemResponse createIngredient(FoodItemRequest request, String firebaseUid) {
        User user = userRepository.findByFirebaseUid(firebaseUid)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // TODO: Implement food item creation via any generative AI API.
        // if (request.aiMetadata() != null)

        FoodItem foodItem = FoodItem.builder()
                .user(user)
                .name(request.name())
                .category(request.category())
                .baseServingAmount(request.baseServingAmount())
                .baseServingUnit(request.baseServingUnit())
                .baseCalories(request.baseCalories())
                .baseProtein(request.baseProtein())
                .baseCarbs(request.baseCarbs())
                .baseFats(request.baseFats())
                .aiMetadata(null)
                .build();

        FoodItem savedFoodItem = foodItemRepository.save(foodItem);

        return mapToFoodItemResponse(savedFoodItem);
    }

    @Override
    public FoodItemResponse createRecipe(RecipeRequest request, String firebaseUid) {
        User user = userRepository.findByFirebaseUid(firebaseUid)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        FoodItem recipeParent = FoodItem.builder()
                .user(user)
                .name(request.name())
                .category(request.category())
                .baseServingAmount(BigDecimal.ONE)
                .baseServingUnit(Unit.UNIDAD)
                .baseCalories(BigDecimal.ZERO)
                .baseProtein(BigDecimal.ZERO)
                .baseCarbs(BigDecimal.ZERO)
                .baseFats(BigDecimal.ZERO)
                .build();

        recipeParent = foodItemRepository.save(recipeParent);

        BigDecimal baseCalories = BigDecimal.ZERO;
        BigDecimal baseProtein = BigDecimal.ZERO;
        BigDecimal baseCarbs = BigDecimal.ZERO;
        BigDecimal baseFats = BigDecimal.ZERO;

        List<FoodComponent> componentsToSave = new ArrayList<>();

        for (ComponentDto component : request.components()) {
            FoodItem childFood = foodItemRepository.findById(component.childFoodId())
                    .orElseThrow(() -> new RuntimeException("ID: " + component.childFoodId() + " no encontrado"));

            BigDecimal ratio = component.portionAmount()
                    .divide(childFood.getBaseServingAmount(), 4, RoundingMode.HALF_UP);

            baseCalories = baseCalories.add(ratio.multiply(childFood.getBaseCalories()));
            baseProtein = baseProtein.add(ratio.multiply(childFood.getBaseProtein()));
            baseCarbs = baseCarbs.add(ratio.multiply(childFood.getBaseCarbs()));
            baseFats = baseFats.add(ratio.multiply(childFood.getBaseFats()));

            FoodComponent foodComponent = FoodComponent.builder()
                    .parentFood(recipeParent)
                    .childFood(childFood)
                    .portionAmount(component.portionAmount())
                    .unit(component.unit())
                    .build();

            componentsToSave.add(foodComponent);
        }

        foodComponentRepository.saveAll(componentsToSave);

        recipeParent.setBaseCalories(baseCalories);
        recipeParent.setBaseProtein(baseProtein);
        recipeParent.setBaseCarbs(baseCarbs);
        recipeParent.setBaseFats(baseFats);

        foodItemRepository.save(recipeParent);

        return mapToFoodItemResponse(recipeParent);
    }

    @Override
    public List<FoodItemResponse> getAllFoodItems(String firebaseUid) {
        return foodItemRepository.findByUserFirebaseUidAndIsActiveTrue(firebaseUid).stream()
                .map(this::mapToFoodItemResponse)
                .toList();
    }

    @Override
    public FoodItemResponse deleteIngredient(String firebaseUid, Long foodId) {
        FoodItem foodItem = foodItemRepository.findById(foodId)
                .orElseThrow(() -> new RuntimeException("Comida no encontrada."));

        if (!foodItem.getUser().getFirebaseUid().equals(firebaseUid)) {
            throw new RuntimeException("Usuario no encontrado o acceso denegado.");
        }

        if (!foodItem.getIsActive()) throw new RuntimeException("Comida no disponible.");

        foodItem.setIsActive(false);
        foodItemRepository.save(foodItem);

        return mapToFoodItemResponse(foodItem);
    }

    private FoodItemResponse mapToFoodItemResponse(FoodItem foodItem) {
        return new FoodItemResponse(
                foodItem.getId(),
                foodItem.getName(),
                foodItem.getCategory().toString(),
                foodItem.getBaseServingAmount(),
                foodItem.getBaseServingUnit().toString(),
                foodItem.getBaseCalories(),
                foodItem.getBaseProtein(),
                foodItem.getBaseCarbs(),
                foodItem.getBaseFats(),
                foodItem.getAiMetadata() == null ? null :  foodItem.getAiMetadata().toString(),
                foodItem.getCreatedAt()
        );
    }
}
