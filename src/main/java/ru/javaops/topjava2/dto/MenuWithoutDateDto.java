package ru.javaops.topjava2.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MenuWithoutDateDto extends AbstractMenuDto{

    public MenuWithoutDateDto() {

    }
    public MenuWithoutDateDto(RestaurantDto restaurant, String dish, BigDecimal price) {
        super(dish, price);

    }

}
