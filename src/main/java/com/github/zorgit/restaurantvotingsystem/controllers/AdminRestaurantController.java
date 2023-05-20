package com.github.zorgit.restaurantvotingsystem.controllers;

import com.github.zorgit.restaurantvotingsystem.dto.RestaurantDto;
import com.github.zorgit.restaurantvotingsystem.dto.RestaurantWithIdDto;
import com.github.zorgit.restaurantvotingsystem.error.IllegalRequestDataException;
import com.github.zorgit.restaurantvotingsystem.error.NotFoundException;
import com.github.zorgit.restaurantvotingsystem.service.RestaurantService;
import com.github.zorgit.restaurantvotingsystem.util.RestaurantUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
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
    public ResponseEntity<RestaurantWithIdDto> createRestaurant(@RequestBody @Valid RestaurantDto restaurant,
                                                                BindingResult bindingResult,
                                                                UriComponentsBuilder uriBuilder) {
        if (bindingResult.hasErrors()) {
            throw new IllegalRequestDataException("Incorrect input data" + bindingResult);
        }

        RestaurantWithIdDto createdRestaurant = RestaurantUtil.asToWithId(
                restaurantService.create(RestaurantUtil.createNewFromTo(restaurant)));

        URI locationUri = uriBuilder.path(REST_URL + "/{id}")
                .buildAndExpand(createdRestaurant.getId()).toUri();

        return ResponseEntity.created(locationUri).body(createdRestaurant);
    }

    @GetMapping()
    public List<RestaurantWithIdDto> getAllRestaurants() {
        return RestaurantUtil.asToListWithId(restaurantService.findAll());
    }

    @GetMapping("/{restaurantId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<RestaurantDto> getRestaurantById(@PathVariable Long restaurantId) {
        try {
            return ResponseEntity.ok(
                    RestaurantUtil.asTo(restaurantService.findById(restaurantId))
            );
        } catch (NotFoundException e) {
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRestaurant(@PathVariable Long restaurantId) {
        restaurantService.delete(restaurantId);
    }
}
