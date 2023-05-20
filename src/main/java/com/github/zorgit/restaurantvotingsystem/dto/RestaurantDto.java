package com.github.zorgit.restaurantvotingsystem.dto;

public class RestaurantDto extends AbstractRestaurantDto {
    public RestaurantDto(String name) {
        super(name);
    }

    public RestaurantDto() {
        //need to Jackson work properly
    }

}
