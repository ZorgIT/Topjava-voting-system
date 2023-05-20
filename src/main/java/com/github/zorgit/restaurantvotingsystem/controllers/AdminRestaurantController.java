package com.github.zorgit.restaurantvotingsystem.controllers;

import com.github.zorgit.restaurantvotingsystem.dto.RestaurantDto;
import com.github.zorgit.restaurantvotingsystem.dto.RestaurantWithIdDto;
import com.github.zorgit.restaurantvotingsystem.service.RestaurantService;
import com.github.zorgit.restaurantvotingsystem.util.RestaurantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                .create(RestaurantUtil.createNewFromTo(restaurant)));
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRestaurant);
    }

    @GetMapping()
    public ResponseEntity<List<RestaurantWithIdDto>> getAllRestaurants() {
        List<RestaurantWithIdDto> restaurants =
                RestaurantUtil.asToListWithId(restaurantService.findAll());
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/{restaurantId}")
    public ResponseEntity<RestaurantDto> getRestaurantById(@PathVariable Long restaurantId) {
        RestaurantDto restaurant = RestaurantUtil.asTo(restaurantService.findById(restaurantId));
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
        return ResponseEntity
                .ok(RestaurantUtil
                        .asToWithId(restaurantService
                                .updateById(restaurantId, updatedRestaurant)));
    }

    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long restaurantId) {
        restaurantService.delete(restaurantId);
        return ResponseEntity.noContent().build();
    }
}
