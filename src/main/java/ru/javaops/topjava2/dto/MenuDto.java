package ru.javaops.topjava2.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

//TODO add valid data
public class MenuDto extends AbstractMenuDto {
    @NotNull
    @PastOrPresent
    private LocalDate date;

    public MenuDto() {

    }
    public MenuDto(LocalDate date, String dish, BigDecimal price) {
        super(dish, price);
        this.date=date;
    }

    public LocalDate getDate() {
        return date;
    }
}
