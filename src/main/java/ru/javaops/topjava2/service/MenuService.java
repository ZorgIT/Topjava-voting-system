package ru.javaops.topjava2.service;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.topjava2.dto.MenuDto;
import ru.javaops.topjava2.dto.MenuWithRestaurantDto;
import ru.javaops.topjava2.dto.MenuWithoutDateDto;
import ru.javaops.topjava2.error.NotFoundException;
import ru.javaops.topjava2.model.Menu;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.repository.MenuRepository;
import ru.javaops.topjava2.repository.RestaurantRepository;
import ru.javaops.topjava2.util.MenusUtil;
import ru.javaops.topjava2.util.RestaurantUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    @Autowired
    public MenuService(MenuRepository menuRepository, RestaurantRepository restaurantRepository) {
        this.menuRepository = menuRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public List<Menu> getMenuForRestaurantAndDate(Long restaurantId, LocalDate date) {
        return menuRepository.findByRestaurantIdAndDate(restaurantId, date);
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
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant with id " + restaurantId + " not found"));
        Menu menu = MenusUtil.createNewFromTo(menuDto,RestaurantUtil.asTo(restaurant));
        menu.setDate(LocalDate.now());
        menu.setRestaurant(restaurant);
        return menuRepository.save(menu);
    }

    public Menu updateMenu(Long restaurantId, Long menuId, MenuDto updatedMenu) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant with id" + restaurantId + " not found"));
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new NotFoundException("Menu with id " + menuId + " not found"));
        MenusUtil.updateFromTo(menu, new MenuWithRestaurantDto(updatedMenu.getDate(),
                updatedMenu.getDish(), updatedMenu.getPrice(),
                RestaurantUtil.asTo(restaurant)));
        return menuRepository.save(menu);
    }
}
