package com.alvaro_chz.macrotracker.repository;

import com.alvaro_chz.macrotracker.model.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {
    List<FoodItem> findByUserFirebaseUidAndIsActiveTrue(String firebaseUid);
}
