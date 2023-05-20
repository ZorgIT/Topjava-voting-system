package com.github.zorgit.restaurantvotingsystem.service;

import com.github.zorgit.restaurantvotingsystem.dto.MenuDto;
import com.github.zorgit.restaurantvotingsystem.dto.RestaurantDto;
import com.github.zorgit.restaurantvotingsystem.dto.RestaurantWithDayMenuDto;
import com.github.zorgit.restaurantvotingsystem.error.NotFoundException;
import com.github.zorgit.restaurantvotingsystem.model.Restaurant;
import com.github.zorgit.restaurantvotingsystem.repository.RestaurantRepository;
import com.github.zorgit.restaurantvotingsystem.util.RestaurantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    @Autowired
    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public Restaurant create(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    @Transactional(readOnly = true)
    public Restaurant findById(Long id) {
        return getRestaurantById(id);
    }

    @Transactional(readOnly = true)
    public List<RestaurantDto> getAllAsTo() {
        return restaurantRepository.findAll().stream()
                .map(RestaurantUtil::asTo)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Restaurant> findAll() {
        return restaurantRepository.findAll();
    }

    public Restaurant updateById(Long id, RestaurantDto updatedRestaurant) {
        Restaurant restaurant = getRestaurantById(id);
        restaurant.setName(updatedRestaurant.getName());
        return restaurantRepository.save(restaurant);
    }

    public void delete(Long restaurantId) {
        Optional<Restaurant> restaurant =
                Optional.ofNullable(getRestaurantById(restaurantId));
        restaurantRepository.delete(restaurant.get());
    }

    @Transactional(readOnly = true)
    public List<RestaurantWithDayMenuDto> getDayMenu() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime voteBoundaries = LocalDate.now().atTime(11, 0);
        if (now.isBefore(voteBoundaries)) {
            voteBoundaries = voteBoundaries.minusDays(1);
        }
        final LocalDateTime startDateTime = voteBoundaries;
        final LocalDateTime endDateTime = startDateTime.plusDays(1).minusSeconds(1);

        List<Object[]> result = restaurantRepository.getDayMenu(startDateTime, endDateTime);

        Map<Long, RestaurantWithDayMenuDto> restaurantsWithMenus = new HashMap<>();
        for (Object[] row : result) {
            long restaurantId = (Long) row[0];
            String restaurantName = (String) row[1];
            LocalDateTime dateTime = (LocalDateTime) row[2];
            String dishName = (String) row[3];
            BigDecimal price = (BigDecimal) row[4];

            if (!restaurantsWithMenus.containsKey(restaurantId)) {
                RestaurantWithDayMenuDto restaurantDto =
                        new RestaurantWithDayMenuDto(restaurantId, restaurantName, new ArrayList<>());
                restaurantsWithMenus.put(restaurantId, restaurantDto);
            }

            if (dishName != null && price != null) {
                restaurantsWithMenus.get(restaurantId).getMenuDtos().add(new MenuDto(startDateTime, dishName, price));
            }
        }

        return new ArrayList<>(restaurantsWithMenus.values());
    }

    private Restaurant getRestaurantById(Long id) {
        return restaurantRepository
                .findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Restaurant with " +
                                "id: " +
                                id + " not found"));
    }
}
