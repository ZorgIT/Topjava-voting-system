package ru.javaops.topjava2.util;

import lombok.experimental.UtilityClass;
import ru.javaops.topjava2.dto.MenuWithRestaurantDto;
import ru.javaops.topjava2.dto.MenuWithoutDateDto;
import ru.javaops.topjava2.dto.RestaurantDto;
import ru.javaops.topjava2.model.Menu;

import java.time.LocalDate;

@UtilityClass
public class MenusUtil {

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

    public static MenuWithoutDateDto createNewFromToWithoutDate(Menu menu) {
        return new MenuWithoutDateDto(
                menu.getDish(),
                menu.getPrice()
        );

    }




}
