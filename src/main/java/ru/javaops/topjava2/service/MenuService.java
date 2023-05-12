package ru.javaops.topjava2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javaops.topjava2.model.Menu;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.repository.MenuRepository;
import ru.javaops.topjava2.repository.RestaurantRepository;

import java.time.LocalDate;
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

    public List<Menu> getMenuForRestaurantAdnDate(Long restaurantId, LocalDate date) {
        return menuRepository.findByRestaurantIdAndDate(restaurantId, date);
    }

    public List<Menu> getMenuForRestaurant(Long restaurantId) {
        return menuRepository.findByRestaurantId(restaurantId);
    }

    public Menu saveMenu(Menu menu) {
        return menuRepository.save(menu);
    }

    public void deleteMenu(Long menuId) {
        menuRepository.deleteById(menuId);
    }

    public void deleteMenu(Long restaurantId, Long menuId) {
        Optional<Menu> menu = menuRepository.findById(menuId);

        if (menu.isPresent()) {
            Menu existingMenu = menu.get();

            if (existingMenu.getRestaurant().getId().equals(restaurantId)) {
                menuRepository.delete(existingMenu);
            } else {
                throw new IllegalArgumentException("Menu with id " + menuId + "does not belong to restaurant with id " +
                        restaurantId);
            } }
        else {
                throw  new IllegalArgumentException("Menu with id " + menuId + " not found");
            }
    }


    public Menu createMenu(Long restaurantId, Menu menu) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantId);
        if (restaurant.isPresent()) {
            menu.setRestaurant(restaurant.get());
            return menuRepository.save(menu);
        } else {
            throw new IllegalArgumentException("Restaurant with id" + restaurantId + " not found");
        }
    }

    public Menu updateMenu(Long restaurantId, Long menuId, Menu updatedMenu) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantId);
        if (restaurant.isPresent()) {
            Optional<Menu> menu = menuRepository.findById(menuId);
            if (menu.isPresent()) {
                Menu existingMenu = menu.get();
                //TODO проверить ТЗ на возможность применения мапперов
                existingMenu.setDate(updatedMenu.getDate());
                existingMenu.setDish(updatedMenu.getDish());
                existingMenu.setPrice(updatedMenu.getPrice());
                return menuRepository.save(existingMenu);
            } else {
                throw new IllegalArgumentException("Menu not found");
            }
        } else {
            throw new IllegalArgumentException("Restaurant not found");
        }
    }
}
