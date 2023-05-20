package com.github.zorgit.restaurantvotingsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AbstractRestaurantDto {
    @NotBlank
    @Size(min = 1, max = 255)
    private String name;

    public AbstractRestaurantDto(String name) {
        this.name = name;
    }

    public AbstractRestaurantDto() {

    }

    public String getName() {
        return name;
    }
}
