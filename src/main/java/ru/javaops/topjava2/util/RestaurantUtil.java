package ru.javaops.topjava2.util;

import lombok.experimental.UtilityClass;
import ru.javaops.topjava2.dto.MenuWithoutDateDto;
import ru.javaops.topjava2.dto.RestaurantDto;
import ru.javaops.topjava2.dto.RestaurantWithDaymenuDto;
import ru.javaops.topjava2.dto.RestaurantWithIdDto;
import ru.javaops.topjava2.model.Menu;
import ru.javaops.topjava2.model.Restaurant;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static java.awt.SystemColor.menu;

@UtilityClass
public class RestaurantUtil {
    public Restaurant createNewFromTo(RestaurantDto restaurantDto) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(restaurantDto.getName());
        return restaurant;
    }

    public Restaurant updateFromTo(Restaurant restaurant, RestaurantDto restaurantDto) {
        restaurant.setName(restaurantDto.getName());
        return restaurant;
    }

    public RestaurantDto asTo(Restaurant restaurant) {
        return new RestaurantDto(restaurant.getName());
    }

    public RestaurantWithIdDto asToWithId(Restaurant restaurant) {
        return new RestaurantWithIdDto(restaurant.getId(), restaurant.getName());
    }

    public RestaurantWithDaymenuDto asToWithMenu(Restaurant restaurant) {
        return new RestaurantWithDaymenuDto(
                restaurant.getId(),
                restaurant.getName(),
                MenusUtil.createNewFromToWithoutDate(restaurant.getMenus().get(0))
        );
    }






}
