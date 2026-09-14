package com.alvaro_chz.macrotracker.service;

import com.alvaro_chz.macrotracker.dto.meal.DailyTrackerResponse;
import com.alvaro_chz.macrotracker.dto.meal.MealLogRequest;
import com.alvaro_chz.macrotracker.dto.meal.MealLogResponse;

import java.time.LocalDate;
import java.util.List;

public interface MealLogService {
    MealLogResponse createMealLog(MealLogRequest request, String firebaseUid);
    List<MealLogResponse> getLogsByDate(LocalDate date, String firebaseUid);
    DailyTrackerResponse getDailySummary(LocalDate date, String firebaseUid);
    MealLogResponse updateMealLog(Long logId, MealLogRequest request, String firebaseUid);
    void deleteMealLog(Long logId, String firebaseUid);
}
