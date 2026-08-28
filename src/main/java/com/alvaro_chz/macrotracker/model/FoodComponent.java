package com.alvaro_chz.macrotracker.model;

import com.alvaro_chz.macrotracker.model.enums.Unit;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "food_component")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class FoodComponent {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "food_component_seq_gen")
    @SequenceGenerator(name = "food_component_seq_gen", sequenceName = "food_component_seq", allocationSize = 50)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_food_id", nullable = false)
    private FoodItem parentFood;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_food_id", nullable = false)
    private FoodItem childFood;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal portionAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Unit unit;
}
