package com.github.zorgit.restaurantvotingsystem.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MenuDto extends AbstractMenuDto {
    @NotNull
    @PastOrPresent
    private LocalDate date;

    public MenuDto(LocalDate date, String dish, BigDecimal price) {
        super(dish, price);
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }
}
