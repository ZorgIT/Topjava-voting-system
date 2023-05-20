package com.github.zorgit.restaurantvotingsystem.service;

import com.github.zorgit.restaurantvotingsystem.dto.RestaurantDto;
import com.github.zorgit.restaurantvotingsystem.dto.RestaurantWithDaymenuDto;
import com.github.zorgit.restaurantvotingsystem.error.NotFoundException;
import com.github.zorgit.restaurantvotingsystem.model.Menu;
import com.github.zorgit.restaurantvotingsystem.model.Restaurant;
import com.github.zorgit.restaurantvotingsystem.repository.MenuRepository;
import com.github.zorgit.restaurantvotingsystem.repository.RestaurantRepository;
import com.github.zorgit.restaurantvotingsystem.util.MenusUtil;
import com.github.zorgit.restaurantvotingsystem.util.RestaurantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
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
    public List<RestaurantWithDaymenuDto> getDayMenu() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime voteBoundaries = LocalDate.now().atTime(11, 0);
        if (now.isBefore(voteBoundaries)) {
            voteBoundaries = voteBoundaries.minusDays(1);
        }
        final LocalDateTime startDateTime = voteBoundaries;
        final LocalDateTime endDateTime = startDateTime.plusDays(1).minusSeconds(1);

        List<Menu> allMenus = menuRepository.findByDateTimeBetween(startDateTime, endDateTime);

        Map<Restaurant, List<Menu>> menusByRestaurant =
                allMenus.stream()
                        .collect(Collectors.groupingBy(Menu::getRestaurant));

        return restaurantRepository.findAll().stream()
                .map(restaurant -> {
                    RestaurantWithDaymenuDto restaurantDto =
                            new RestaurantWithDaymenuDto(restaurant.getId(),
                                    restaurant.getName(), null);

                    List<Menu> menus = menusByRestaurant.get(restaurant);

                    if (menus != null) {
                        restaurantDto.setMenuDtos(MenusUtil.asMenuDtos(menus));
                    }
                    return restaurantDto;
                })
                .filter(restaurantDto -> restaurantDto.getMenuDtos() != null)
                .collect(Collectors.toList());
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
