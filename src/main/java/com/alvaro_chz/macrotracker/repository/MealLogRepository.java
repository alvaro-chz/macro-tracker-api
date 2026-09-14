package com.alvaro_chz.macrotracker.repository;

import com.alvaro_chz.macrotracker.model.MealLog;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MealLogRepository extends JpaRepository<MealLog, Long> {
    List<MealLog> findByUserFirebaseUidAndConsumedAtBetween(String firebaseUid, LocalDateTime startOfDay, LocalDateTime endOfDay);
}
