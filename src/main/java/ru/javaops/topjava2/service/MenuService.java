package ru.javaops.topjava2.service;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.topjava2.dto.MenuDto;
import ru.javaops.topjava2.dto.MenuWithoutDateDto;
import ru.javaops.topjava2.error.NotFoundException;
import ru.javaops.topjava2.model.Menu;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.repository.MenuRepository;
import ru.javaops.topjava2.repository.RestaurantRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    @Autowired
    public MenuService(MenuRepository menuRepository, RestaurantRepository restaurantRepository) {
        this.menuRepository = menuRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional(readOnly = true)
    public List<Menu> getMenuForRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant with id " +
                        restaurantId + " not found"));
        Hibernate.initialize(restaurant.getMenus());
        return restaurant.getMenus();
    }

    public void deleteMenu(Long restaurantId, Long menuId) {
        Optional<Menu> menu = menuRepository.findById(menuId);

        if (menu.isPresent()) {
            Menu existingMenu = menu.get();

            if (existingMenu.getRestaurant().getId().equals(restaurantId)) {
                menuRepository.delete(existingMenu);
            } else {
                throw new NotFoundException("Menu with id " + menuId + "does not belong to restaurant with id " +
                        restaurantId);
            }
        } else {
            throw new NotFoundException("Menu with id " + menuId + " not found");
        }
    }


    public Menu createMenu(Long restaurantId, MenuWithoutDateDto menuDto) {
        LocalDateTime voteBoundaries = LocalDate.now().atTime(11, 0);
        if (LocalDateTime.now().isAfter(voteBoundaries)) {
            voteBoundaries = voteBoundaries.plusDays(1);
        }
        final LocalDateTime menuDate = voteBoundaries;

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant with id " + restaurantId + " not found"));

        Menu existingMenu = menuRepository.findByRestaurantAndDate(restaurant, menuDate.toLocalDate());

        if (existingMenu != null) {
            return updateMenu(restaurantId, existingMenu.getId(),
                    new MenuDto(menuDate.toLocalDate(), menuDto.getDish(), menuDto.getPrice()));
        } else {
            Menu menu = new Menu(menuDate.toLocalDate(), menuDto.getDish(), menuDto.getPrice(), restaurant);
            return menuRepository.save(menu);
        }
    }

    @Transactional(readOnly = false)
    public Menu updateMenu(Long restaurantId, Long menuId, MenuDto updatedMenu) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant with id " + restaurantId + " not found"));

        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new NotFoundException("Menu with id " + menuId + " not found"));

        menu.setDate(updatedMenu.getDate());
        menu.setDish(updatedMenu.getDish());
        menu.setPrice(updatedMenu.getPrice());
        menu.setRestaurant(restaurant);

        return menuRepository.save(menu);
    }
}
