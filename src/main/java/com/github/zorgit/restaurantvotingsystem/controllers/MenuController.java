package com.github.zorgit.restaurantvotingsystem.controllers;

import com.github.zorgit.restaurantvotingsystem.dto.RestaurantWithDayMenuDto;
import com.github.zorgit.restaurantvotingsystem.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuController {
    private final RestaurantService restaurantService;

    @Autowired
    public MenuController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/day-menus")
    @ResponseStatus(HttpStatus.OK)
    public List<RestaurantWithDayMenuDto> getDayMenu() {
        return restaurantService.getDayMenu();
    }
}
