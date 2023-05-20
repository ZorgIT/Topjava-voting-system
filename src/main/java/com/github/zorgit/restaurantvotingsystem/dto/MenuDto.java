package com.github.zorgit.restaurantvotingsystem.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class MenuDto extends AbstractMenuDto {
    @NotNull
    @PastOrPresent
    private LocalDateTime date;

    public MenuDto(LocalDateTime date, String dish, BigDecimal price) {
        super(dish, price);
        this.date = date;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
