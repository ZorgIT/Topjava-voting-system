package com.github.zorgit.restaurantvotingsystem.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MenuDto {
    @NotNull
    @PastOrPresent
    private LocalDateTime date;

    @NotBlank
    @Size(min = 1, max = 255)
    private String dishName;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 4, fraction = 2)
    private BigDecimal price;

    public MenuDto(LocalDateTime date, String dishName, BigDecimal price) {
        this.date = date;
        this.dishName = dishName;
        this.price = price;
    }

    public MenuDto() {

    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
