package ru.javaops.topjava2.util;

import lombok.experimental.UtilityClass;
import ru.javaops.topjava2.dto.MenuWithRestaurantDto;
import ru.javaops.topjava2.dto.MenuWithoutDateDto;
import ru.javaops.topjava2.dto.RestaurantDto;
import ru.javaops.topjava2.model.Menu;

import java.time.LocalDate;

@UtilityClass
public class MenusUtil {

    public static MenuWithoutDateDto createNewFromToWithoutDate(Menu menu) {
        return new MenuWithoutDateDto(
                menu.getDish(),
                menu.getPrice()
        );

    }




}
