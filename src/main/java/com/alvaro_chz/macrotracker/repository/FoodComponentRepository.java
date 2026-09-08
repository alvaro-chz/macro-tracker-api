package com.alvaro_chz.macrotracker.repository;

import com.alvaro_chz.macrotracker.model.FoodComponent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodComponentRepository extends JpaRepository<FoodComponent, Long> {
}
