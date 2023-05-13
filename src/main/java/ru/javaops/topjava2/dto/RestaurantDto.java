package ru.javaops.topjava2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

//TODO add valid data
public class RestaurantDto {
    private Long id;

    public RestaurantDto(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @NotBlank
    @Size(min = 1, max = 255)
    private String name;

    public RestaurantDto() {
    }

    public String getName() {
        return name;
    }
    public long getId() {
        return id;
    }

}
