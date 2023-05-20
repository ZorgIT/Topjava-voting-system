package com.github.zorgit.restaurantvotingsystem.service;

import com.github.zorgit.restaurantvotingsystem.dto.MenuDto;
import com.github.zorgit.restaurantvotingsystem.dto.MenuWithoutDateDto;
import com.github.zorgit.restaurantvotingsystem.error.NotFoundException;
import com.github.zorgit.restaurantvotingsystem.model.Menu;
import com.github.zorgit.restaurantvotingsystem.model.Restaurant;
import com.github.zorgit.restaurantvotingsystem.repository.MenuRepository;
import com.github.zorgit.restaurantvotingsystem.repository.RestaurantRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    @Autowired
    public MenuService(MenuRepository menuRepository,
                       RestaurantRepository restaurantRepository) {
        this.menuRepository = menuRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional(readOnly = true)
    public List<Menu> findAllByRestaurantId(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant with id " +
                        restaurantId + " not found"));
        Hibernate.initialize(restaurant.getMenus());
        return restaurant.getMenus();
    }

    @Transactional(readOnly = true)
    public Menu getOneByIdAndRestaurantId(Long menuId, Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant with id " +
                        restaurantId + " not found"));
        Hibernate.initialize(restaurant.getMenus());
        List<Menu> menus = restaurant.getMenus();
        for (Menu menu : menus) {
            if (menu.getId().equals(menuId)) {
                return menu;
            }
        }
        throw new NotFoundException("Menu with id " + menuId + " not found in" +
                " restaurant with id " + restaurantId);
    }

    public void delete(Long restaurantId, Long menuId) {
        Optional<Menu> menu = menuRepository.findById(menuId);

        if (menu.isPresent()) {
            Menu existingMenu = menu.get();

            if (existingMenu.getRestaurant().getId().equals(restaurantId)) {
                menuRepository.delete(existingMenu);
            } else {
                throw new NotFoundException("Menu with id " +
                        menuId + "does not belong to restaurant with id " +
                        restaurantId);
            }
        } else {
            throw new NotFoundException("Menu with id " + menuId + " not found");
        }
    }

    public Menu saveOrUpdate(Long restaurantId, MenuWithoutDateDto menuDto) {
        LocalDateTime voteBoundaries = LocalDate.now().atTime(11, 0);
        if (LocalDateTime.now().isAfter(voteBoundaries)) {
            voteBoundaries = voteBoundaries.plusDays(1);
        }
        final LocalDateTime menuDate = voteBoundaries;

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant with id "
                        + restaurantId + " not found"));

        Menu existingMenu = menuRepository.findByRestaurantAndDateTime(restaurant,
                menuDate);

        Menu menu;
        if (existingMenu != null) {
            menu = existingMenu;
        } else {
            menu = new Menu();
            menu.setDateTime(menuDate);
            menu.setRestaurant(restaurant);
        }

        menu.setDish(menuDto.getDish());
        menu.setPrice(menuDto.getPrice());

        return menuRepository.save(menu);
    }


    public Menu update(Long restaurantId, Long menuId, MenuDto updatedMenu) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new NotFoundException("Menu with id " +
                        menuId + " not found"));
        Restaurant restaurant = menu.getRestaurant();
        if (!restaurant.getId().equals(restaurantId)) {
            throw new NotFoundException("Menu tih id " + menuId + " does not " +
                    "belong to restaurant wit id " + restaurantId);
        }

        menu.setDateTime(updatedMenu.getDate());
        menu.setDish(updatedMenu.getDish());
        menu.setPrice(updatedMenu.getPrice());

        return menuRepository.save(menu);
    }
}
