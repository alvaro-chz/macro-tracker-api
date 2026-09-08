package com.alvaro_chz.macrotracker.service;

import com.alvaro_chz.macrotracker.dto.food.FoodItemRequest;
import com.alvaro_chz.macrotracker.dto.food.FoodItemResponse;
import com.alvaro_chz.macrotracker.dto.food.RecipeRequest;

import java.util.List;

public interface FoodItemService {
    FoodItemResponse createIngredient(FoodItemRequest request, String firebaseUid);
    FoodItemResponse createRecipe(RecipeRequest request, String firebaseUid);
    List<FoodItemResponse> getAllFoodItems(String firebaseUid);
    FoodItemResponse deleteIngredient(String firebaseUid, Long foodId);
}
