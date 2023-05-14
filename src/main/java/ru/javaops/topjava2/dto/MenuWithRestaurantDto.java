package ru.javaops.topjava2.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.math.BigDecimal;
import java.time.LocalDate;

//TODO add valid data
public class MenuWithRestaurantDto extends AbstractMenuDto {
    @NotNull
    @PastOrPresent
    private LocalDate date;
    @NotNull
    private RestaurantDto restaurant;

    public MenuWithRestaurantDto(LocalDate date,
                                 String dish,
                                 BigDecimal price,
                                 RestaurantDto restaurant) {
        super(dish, price);
        this.date = date;
        this.restaurant = restaurant;
    }

    public RestaurantDto getRestaurant() {
        return restaurant;
    }

    public LocalDate getDate() {
        return date;
    }
}
