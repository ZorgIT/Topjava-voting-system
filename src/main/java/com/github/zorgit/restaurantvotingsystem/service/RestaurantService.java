package com.github.zorgit.restaurantvotingsystem.service;

import com.github.zorgit.restaurantvotingsystem.dto.RestaurantDto;
import com.github.zorgit.restaurantvotingsystem.error.NotFoundException;
import com.github.zorgit.restaurantvotingsystem.model.Menu;
import com.github.zorgit.restaurantvotingsystem.model.Restaurant;
import com.github.zorgit.restaurantvotingsystem.repository.MenuRepository;
import com.github.zorgit.restaurantvotingsystem.repository.RestaurantRepository;
import com.github.zorgit.restaurantvotingsystem.util.RestaurantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;

    @Autowired
    public RestaurantService(RestaurantRepository restaurantRepository, MenuRepository menuRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuRepository = menuRepository;
    }

    public Restaurant create(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    public Restaurant findById(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Restaurant with id:" +
                        id + " not found"));
    }

    public List<RestaurantDto> getAllAsTo() {
        return restaurantRepository.findAll().stream()
                .map(RestaurantUtil::asTo)
                .collect(Collectors.toList());
    }

    public List<Restaurant> findAll() {
        return restaurantRepository.findAll();
    }

    public Restaurant update(Restaurant updatedRestaurant) {
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

    public void delete(Long restaurantId) {
        Optional<Restaurant> restaurant =
                restaurantRepository.findById(restaurantId);
        if (restaurant.isPresent()) {
            restaurantRepository.delete(restaurant.get());
        } else {
            throw new IllegalArgumentException("Restaurant with id"
                    + restaurantId + " not found");
        }
    }

    @Transactional(readOnly = true)
    public List<Restaurant> getRestaurantsWithMenus() {
        LocalDateTime voteBoundaries = LocalDate.now().atTime(11, 0);
        if (LocalDateTime.now().isAfter(voteBoundaries)) {
            voteBoundaries = voteBoundaries.plusDays(1);
        }
        final LocalDateTime menuDate = voteBoundaries;
        return restaurantRepository.findAll().stream()
                .filter(restaurant -> !menuRepository.findByRestaurantIdAndDate(restaurant.getId(),
                        menuDate.toLocalDate()).isEmpty())
                .peek(restaurant -> {
                    List<Menu> menus = menuRepository.findByRestaurantIdAndDate(restaurant.getId(),
                            menuDate.toLocalDate());
                    restaurant.setMenus(menus);
                })
                .collect(Collectors.toList());
    }


}
