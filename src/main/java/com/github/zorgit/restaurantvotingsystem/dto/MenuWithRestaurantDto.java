package com.github.zorgit.restaurantvotingsystem.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public class MenuWithRestaurantDto extends AbstractMenuDto {
    @NotNull
    @PastOrPresent
    private LocalDate date;
    @NotNull
    private RestaurantDto restaurant;

    public RestaurantDto getRestaurant() {
        return restaurant;
    }

    public LocalDate getDate() {
        return date;
    }
}
