package com.foodapp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor
public class OrderItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity;
    private double unitPrice;

    @ManyToOne(fetch=FetchType.LAZY)
    private MenuItem menuItem;

    @ManyToOne(fetch=FetchType.LAZY)
    private FoodOrder order;
}
