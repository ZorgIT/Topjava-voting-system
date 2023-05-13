package ru.javaops.topjava2.util;

import lombok.experimental.UtilityClass;
import ru.javaops.topjava2.dto.MenuDto;
import ru.javaops.topjava2.dto.MenuWithRestaurantDto;
import ru.javaops.topjava2.dto.MenuWithoutDateDto;
import ru.javaops.topjava2.dto.RestaurantDto;
import ru.javaops.topjava2.model.Menu;

import java.time.LocalDate;

@UtilityClass
public class MenusUtil {

    public static Menu createNewFromTo(MenuWithRestaurantDto menuDto) {
        return new Menu(menuDto.getDate(), menuDto.getDish(), menuDto.getPrice(),
                RestaurantUtil.createNewFromTo(menuDto.getRestaurant()));
    }
    public static Menu createNewFromTo(MenuWithoutDateDto menuDto) {
        return new Menu(null, menuDto.getDish(), menuDto.getPrice(),
                null);
    }

    public static Menu updateFromTo(Menu menu, MenuWithRestaurantDto menuDto) {
        menu.setDate(menuDto.getDate());
        menu.setDish(menuDto.getDish());
        menu.setPrice(menuDto.getPrice());
        menu.setRestaurant(RestaurantUtil.createNewFromTo(menuDto.getRestaurant()));
        return menu;
    }

    public static Menu createNewFromTo(MenuWithoutDateDto menuDto, RestaurantDto restaurantDto) {
        return new Menu(LocalDate.now(), menuDto.getDish(), menuDto.getPrice(),
                RestaurantUtil.createNewFromTo(restaurantDto));
    }


    public static MenuDto asTo(Menu menu) {
        return new MenuDto(menu.getDate(), menu.getDish(), menu.getPrice());
    }
    public static MenuWithRestaurantDto withRestaurantAsTo(Menu menu) {
        return new MenuWithRestaurantDto(menu.getDate(), menu.getDish(), menu.getPrice(),
                RestaurantUtil.asTo(menu.getRestaurant()));
    }
}
