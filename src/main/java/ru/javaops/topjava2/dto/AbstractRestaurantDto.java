package ru.javaops.topjava2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AbstractRestaurantDto {
    @NotBlank
    @Size(min = 1, max = 255)
    private String name;

    public AbstractRestaurantDto() {

    }

    public AbstractRestaurantDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
