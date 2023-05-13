package ru.javaops.topjava2.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public abstract class AbstractMenuDto {


    @NotBlank
    @Size(min = 1, max = 255)
    private String dish;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 4, fraction = 2)
    private BigDecimal price;

    public AbstractMenuDto() {
    }

    public AbstractMenuDto(String dish, BigDecimal price) {
        this.dish = dish;
        this.price = price;
    }


    public String getDish() {
        return dish;
    }

    public BigDecimal getPrice() {
        return price;
    }

}
