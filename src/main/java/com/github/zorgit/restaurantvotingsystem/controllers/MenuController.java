package com.github.zorgit.restaurantvotingsystem.controllers;

import com.github.zorgit.restaurantvotingsystem.dto.RestaurantWithDaymenuDto;
import com.github.zorgit.restaurantvotingsystem.service.RestaurantService;
import com.github.zorgit.restaurantvotingsystem.util.RestaurantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/menu")
public class MenuController {
    private final RestaurantService RestaurantService;

    @Autowired
    public MenuController(RestaurantService restaurantService) {
        this.RestaurantService = restaurantService;
    }

    @GetMapping("/day-menus")
    @ResponseStatus(HttpStatus.OK)
    public List<RestaurantWithDaymenuDto> getRestaurantsWithMenus() {
        return RestaurantService.getRestaurantsWithMenus().stream()
                .map(RestaurantUtil::asToWithMenu)
                .collect(Collectors.toList());
    }
}
