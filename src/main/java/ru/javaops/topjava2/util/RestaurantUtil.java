package ru.javaops.topjava2.util;

import lombok.experimental.UtilityClass;
import ru.javaops.topjava2.dto.RestaurantDto;
import ru.javaops.topjava2.model.Restaurant;

@UtilityClass
public class RestaurantUtil {
    public Restaurant createNewFromTo(RestaurantDto restaurantDto) {
        return new Restaurant(restaurantDto.getId(), restaurantDto.getName());
    }

    public Restaurant updateFromTo(Restaurant restaurant, RestaurantDto restaurantDto) {
        restaurant.setName(restaurantDto.getName());
        return restaurant;
    }

    public RestaurantDto asTo(Restaurant restaurant) {
        return new RestaurantDto(restaurant.getId(), restaurant.getName());
    }
}
