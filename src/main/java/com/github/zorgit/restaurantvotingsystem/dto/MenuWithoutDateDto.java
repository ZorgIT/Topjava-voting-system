package com.github.zorgit.restaurantvotingsystem.dto;

import java.math.BigDecimal;

public class MenuWithoutDateDto extends AbstractMenuDto {
    public MenuWithoutDateDto(String dish, BigDecimal price) {
        super(dish, price);
    }
}
