package ru.javaops.topjava2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.repository.RestaurantRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    @Autowired
    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    //TODO причесать CRUDs, добавить проверки, оптимизировать лишнее
    public Optional<Restaurant> getRestaurantById(Long id) {
        return restaurantRepository.findById(id);
    }

    public Restaurant createRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    public void updateRestaurant(Restaurant restaurant) {
        restaurantRepository.save(restaurant);
    }

    public void deleteRestaurant(Restaurant restaurant) {
        restaurantRepository.delete(restaurant);
    }


}
