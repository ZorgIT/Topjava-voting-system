package ru.javaops.topjava2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javaops.topjava2.dto.RestaurantDto;
import ru.javaops.topjava2.error.NotFoundException;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.repository.RestaurantRepository;
import ru.javaops.topjava2.util.RestaurantUtil;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    @Autowired
    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public Restaurant createRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    public Restaurant getRestaurantById(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Restaurant with id:" +
                        id + " not found"));
    }

    public List<RestaurantDto> getAllRestaurants() {
        return restaurantRepository.findAll().stream()
                .map(RestaurantUtil::asTo)
                .collect(Collectors.toList());
    }

    public Restaurant updateRestaurant(Restaurant updatedRestaurant) {
        Optional<Restaurant> restaurant =
                restaurantRepository.findById(updatedRestaurant.getId());
        if (restaurant.isPresent()) {
            Restaurant existingRestaurant = restaurant.get();
            existingRestaurant.setName(updatedRestaurant.getName());
            existingRestaurant.setMenus(updatedRestaurant.getMenus());
            return restaurantRepository.save(existingRestaurant);
        } else {
            throw new IllegalArgumentException("Restaurant not found");
        }
    }

    public void deleteRestaurant(Long restaurantId) {
        Optional<Restaurant> restaurant =
                restaurantRepository.findById(restaurantId);
        if (restaurant.isPresent()) {
            restaurantRepository.delete(restaurant.get());
        } else {
            throw new IllegalArgumentException("Restaurant with id"
                    + restaurantId + " not found");
        }
    }


}
