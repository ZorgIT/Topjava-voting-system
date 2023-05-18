package com.github.zorgit.restaurantvotingsystem.controllers;

import com.github.zorgit.restaurantvotingsystem.util.RestaurantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.github.zorgit.restaurantvotingsystem.dto.RestaurantDto;
import com.github.zorgit.restaurantvotingsystem.dto.RestaurantWithIdDto;
import com.github.zorgit.restaurantvotingsystem.model.Restaurant;
import com.github.zorgit.restaurantvotingsystem.service.RestaurantService;

import java.util.List;

@RestController
@RequestMapping(value = AdminRestaurantController.REST_URL,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminRestaurantController {
    static final String REST_URL = "/api/admin/restaurants";
    private final RestaurantService restaurantService;

    @Autowired
    public AdminRestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestaurantWithIdDto> createRestaurant(@RequestBody RestaurantDto restaurant) {
        RestaurantWithIdDto createdRestaurant = RestaurantUtil.asToWithId(restaurantService
                .createRestaurant(RestaurantUtil.createNewFromTo(restaurant)));
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRestaurant);
    }

    @GetMapping()
    public ResponseEntity<List<RestaurantDto>> getAllRestaurants() {
        List<RestaurantDto> restaurants = restaurantService.getAllRestaurants();
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/{restaurantId}")
    public ResponseEntity<RestaurantDto> getRestaurantById(@PathVariable Long restaurantId) {
        RestaurantDto restaurant = RestaurantUtil.asTo(restaurantService.getRestaurantById(restaurantId));
        if (restaurant != null) {
            return ResponseEntity.ok(restaurant);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(value = "/{restaurantId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestaurantWithIdDto> updateRestaurant(
            @PathVariable Long restaurantId,
            @RequestBody RestaurantDto updatedRestaurant) {
        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);
        if (restaurant != null) {
            RestaurantUtil.updateFromTo(restaurant, updatedRestaurant);
            restaurantService.updateRestaurant(restaurant);
            return ResponseEntity.ok(RestaurantUtil.asToWithId(restaurant));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long restaurantId) {
        restaurantService.deleteRestaurant(restaurantId);
        return ResponseEntity.noContent().build();
    }
}
