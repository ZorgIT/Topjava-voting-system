package ru.javaops.topjava2.dto;

import java.math.BigDecimal;

public class MenuWithoutDateDto extends AbstractMenuDto {

    public MenuWithoutDateDto() {

    }

    public MenuWithoutDateDto(RestaurantDto restaurant, String dish, BigDecimal price) {
        super(dish, price);

    }

}