package com.github.zorgit.restaurantvotingsystem.dto;

public class RestaurantWithIdDto extends AbstractRestaurantDto {
    Long id;

    public RestaurantWithIdDto(Long id, String name) {
        super(name);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
