package com.alvaro_chz.macrotracker.service.impl;

import com.alvaro_chz.macrotracker.dto.meal.DailyTrackerResponse;
import com.alvaro_chz.macrotracker.dto.meal.MealLogRequest;
import com.alvaro_chz.macrotracker.dto.meal.MealLogResponse;
import com.alvaro_chz.macrotracker.model.FoodItem;
import com.alvaro_chz.macrotracker.model.MealLog;
import com.alvaro_chz.macrotracker.model.User;
import com.alvaro_chz.macrotracker.repository.FoodItemRepository;
import com.alvaro_chz.macrotracker.repository.MealLogRepository;
import com.alvaro_chz.macrotracker.repository.UserRepository;
import com.alvaro_chz.macrotracker.service.FoodItemService;
import com.alvaro_chz.macrotracker.service.MealLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MealLogServiceImpl implements MealLogService {
    private final MealLogRepository mealLogRepository;
    private final UserRepository userRepository;
    private final FoodItemRepository foodItemRepository;

    @Override
    public MealLogResponse createMealLog(MealLogRequest request, String firebaseUid) {
        User user = userRepository.findByFirebaseUid(firebaseUid)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        FoodItem foodItem = foodItemRepository.findById(request.foodItemId())
                .orElseThrow(() -> new RuntimeException("Alimento no encontrado."));

        BigDecimal ratio = request.servingSize()
                .divide(foodItem.getBaseServingAmount(), 4, RoundingMode.HALF_UP);

        BigDecimal calculatedCalories = ratio.multiply(foodItem.getBaseCalories());
        BigDecimal calculatedProtein = ratio.multiply(foodItem.getBaseProtein());
        BigDecimal calculatedFats = ratio.multiply(foodItem.getBaseFats());
        BigDecimal calculatedCarbs = ratio.multiply(foodItem.getBaseCarbs());

        MealLog mealLog = MealLog.builder()
                .user(user)
                .foodItem(foodItem)
                .consumedAt(request.logDate())
                .mealType(request.mealType())
                .servingSize(request.servingSize())
                .calculatedCalories(calculatedCalories)
                .calculatedProtein(calculatedProtein)
                .calculatedCarbs(calculatedCarbs)
                .calculatedFats(calculatedFats)
                .build();

        return mapToMealLogResponse(mealLogRepository.save(mealLog));
    }

    @Override
    public List<MealLogResponse> getLogsByDate(LocalDate date, String firebaseUid) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        return mealLogRepository.findByUserFirebaseUidAndConsumedAtBetween(firebaseUid, startOfDay, endOfDay).stream()
                .map(this::mapToMealLogResponse)
                .toList();
    }

    @Override
    public DailyTrackerResponse getDailySummary(LocalDate date, String firebaseUid) {
        List<MealLogResponse> logsDate = getLogsByDate(date, firebaseUid);

        BigDecimal totalCalories = BigDecimal.ZERO;
        BigDecimal totalProtein = BigDecimal.ZERO;
        BigDecimal totalCarbs = BigDecimal.ZERO;
        BigDecimal totalFats = BigDecimal.ZERO;

        for (MealLogResponse mealLog : logsDate) {
            totalCalories = totalCalories.add(mealLog.calculatedCalories());
            totalProtein = totalProtein.add(mealLog.calculatedProtein());
            totalCarbs = totalCarbs.add(mealLog.calculatedCarbs());
            totalFats = totalFats.add(mealLog.calculatedFats());
        }

        return new DailyTrackerResponse(
                date,
                logsDate,
                totalCalories,
                totalProtein,
                totalCarbs,
                totalFats
        );
    }

    @Override
    public MealLogResponse updateMealLog(Long logId, MealLogRequest request, String firebaseUid) {
        User user = userRepository.findByFirebaseUid(firebaseUid)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        MealLog mealLog = mealLogRepository.findById(logId)
                .orElseThrow(() -> new RuntimeException("Registro del alimento no encontrado."));

        if (!user.getFirebaseUid().equals(mealLog.getUser().getFirebaseUid())) {
            throw new RuntimeException("Acceso denegado.");
        }

        boolean foodChanged = !Objects.equals(mealLog.getFoodItem().getId(), request.foodItemId());
        boolean servingChanged = mealLog.getServingSize().compareTo(request.servingSize()) != 0;

        if (foodChanged) {
            FoodItem foodItem = foodItemRepository.findById(request.foodItemId())
                            .orElseThrow(() -> new RuntimeException("Alimento no encontrado."));

            BigDecimal ratio = request.servingSize()
                    .divide(foodItem.getBaseServingAmount(), 4, RoundingMode.HALF_UP);

            mealLog.setFoodItem(foodItem);
            mealLog.setServingSize(request.servingSize());
            mealLog.setCalculatedCalories(ratio.multiply(foodItem.getBaseCalories()));
            mealLog.setCalculatedProtein(ratio.multiply(foodItem.getBaseProtein()));
            mealLog.setCalculatedCarbs(ratio.multiply(foodItem.getBaseCarbs()));
            mealLog.setCalculatedFats(ratio.multiply(foodItem.getBaseFats()));

        } else if (servingChanged) {
            BigDecimal ratio = request.servingSize()
                    .divide(mealLog.getServingSize(), 4, RoundingMode.HALF_UP);

            mealLog.setServingSize(request.servingSize());
            mealLog.setCalculatedCalories(ratio.multiply(mealLog.getCalculatedCalories()));
            mealLog.setCalculatedProtein(ratio.multiply(mealLog.getCalculatedProtein()));
            mealLog.setCalculatedCarbs(ratio.multiply(mealLog.getCalculatedCarbs()));
            mealLog.setCalculatedFats(ratio.multiply(mealLog.getCalculatedFats()));
        }

        mealLog.setMealType(request.mealType());
        mealLog.setConsumedAt(request.logDate());

        return mapToMealLogResponse(mealLogRepository.save(mealLog));
    }

    @Override
    public void deleteMealLog(Long logId, String firebaseUid) {
        User user = userRepository.findByFirebaseUid(firebaseUid)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        MealLog mealLog = mealLogRepository.findById(logId)
                .orElseThrow(() -> new RuntimeException("Registro del alimento no encontrado."));

        if (!user.getFirebaseUid().equals(mealLog.getUser().getFirebaseUid())) {
            throw new RuntimeException("Acceso denegado.");
        }

        mealLogRepository.delete(mealLog);
    }

    private MealLogResponse mapToMealLogResponse(MealLog mealLog) {
        return new MealLogResponse(
                mealLog.getId(),
                mealLog.getConsumedAt(),
                mealLog.getMealType().toString(),
                mealLog.getFoodItem().getId(),
                mealLog.getFoodItem().getName(),
                mealLog.getServingSize(),
                mealLog.getCalculatedCalories(),
                mealLog.getCalculatedProtein(),
                mealLog.getCalculatedCarbs(),
                mealLog.getCalculatedFats()
        );
    }
}
