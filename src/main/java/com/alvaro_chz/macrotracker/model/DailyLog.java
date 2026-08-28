package com.alvaro_chz.macrotracker.model;

import com.alvaro_chz.macrotracker.model.enums.MealType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "daily_log")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class DailyLog {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "daily_log_seq_gen")
    @SequenceGenerator(name = "daily_log_seq_gen", sequenceName = "daily_log_seq", allocationSize = 50)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_item_id", nullable = false)
    private FoodItem foodItem;

    @Column(name = "consumed_at", nullable = false)
    private LocalDateTime consumedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "meal_type", length = 50, nullable = false)
    private MealType mealType;

    @Column(name = "serving_size", precision = 10, scale = 2)
    private BigDecimal servingSize;

    @Column(name = "calculated_calories", precision = 10, scale = 2)
    private BigDecimal calculatedCalories;

    @Column(name = "calculated_protein", precision = 10, scale = 2)
    private BigDecimal calculatedProtein;

    @Column(name = "calculated_carbs", precision = 10, scale = 2)
    private BigDecimal calculatedCarbs;

    @Column(name = "calculated_fats", precision = 10, scale = 2)
    private BigDecimal calculatedFats;
}
