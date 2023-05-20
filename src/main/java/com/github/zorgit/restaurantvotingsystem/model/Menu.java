package com.github.zorgit.restaurantvotingsystem.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "menus_item",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {
                        "restaurant_id",
                        "date_time_stamp",
                        "dish_name"
                })
        })
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_time_stamp", nullable = false)
    private LocalDateTime dateTime;
    @Column(name = "dish_name", nullable = false)
    private String dishName;
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @JsonBackReference
    private Restaurant restaurant;
}
