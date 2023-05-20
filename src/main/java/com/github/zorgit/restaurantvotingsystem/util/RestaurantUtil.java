package com.github.zorgit.restaurantvotingsystem.util;

import com.github.zorgit.restaurantvotingsystem.dto.RestaurantDto;
import com.github.zorgit.restaurantvotingsystem.dto.RestaurantWithDaymenuDto;
import com.github.zorgit.restaurantvotingsystem.dto.RestaurantWithIdDto;
import com.github.zorgit.restaurantvotingsystem.model.Restaurant;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class RestaurantUtil {
    public Restaurant createNewFromTo(RestaurantDto restaurantDto) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(restaurantDto.getName());
        return restaurant;
    }

    public Restaurant updateFromTo(Restaurant restaurant,
                                   RestaurantDto restaurantDto) {
        restaurant.setName(restaurantDto.getName());
        return restaurant;
    }

    public RestaurantDto asTo(Restaurant restaurant) {
        return new RestaurantDto(restaurant.getName());
    }

    public RestaurantWithIdDto asToWithId(Restaurant restaurant) {
        return new RestaurantWithIdDto(restaurant.getId(),
                restaurant.getName());
    }

    public List<RestaurantWithIdDto> asToListWithId(List<Restaurant> restaurants) {
        return restaurants.stream()
                .map(r -> new RestaurantWithIdDto(r.getId(), r.getName()))
                .collect(Collectors.toList());
    }

    public RestaurantWithDaymenuDto asToWithMenu(Restaurant restaurant) {
        return new RestaurantWithDaymenuDto(
                restaurant.getId(),
                restaurant.getName(),
                MenusUtil.createNewFromToWithoutDate(restaurant.getMenus().get(0))
        );
    }
}
