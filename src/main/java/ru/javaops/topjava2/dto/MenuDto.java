package ru.javaops.topjava2.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MenuDto {
    private Long id;
    private LocalDate date;
    private String dish;
    private BigDecimal price;
    private RestaurantDto restaurant;

    public MenuDto() {
    }

    public MenuDto(Long id, LocalDate date, String dish, BigDecimal price, RestaurantDto restaurant) {
        this.id = id;
        this.date = date;
        this.dish = dish;
        this.price = price;
        this.restaurant = restaurant;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDish() {
        return dish;
    }

    public void setDish(String dish) {
        this.dish = dish;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public RestaurantDto getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantDto restaurant) {
        this.restaurant = restaurant;
    }
}
