package ru.javaops.topjava2.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.math.BigDecimal;
import java.time.LocalDate;

//TODO add valid data
public class MenuDto extends AbstractMenuDto {
    @NotNull
    @PastOrPresent
    private LocalDate date;

    private Long id;

    public MenuDto() {

    }

    public MenuDto(LocalDate date, String dish, BigDecimal price) {
        super(dish, price);
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }


    public Long getId() {
        return this.id;
    }
    public void setId(Integer id) {

    }
}
