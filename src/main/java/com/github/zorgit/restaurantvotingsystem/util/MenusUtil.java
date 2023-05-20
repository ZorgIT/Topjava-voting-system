package com.github.zorgit.restaurantvotingsystem.util;

import com.github.zorgit.restaurantvotingsystem.dto.MenuWithoutDateDto;
import com.github.zorgit.restaurantvotingsystem.model.Menu;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MenusUtil {

    public static MenuWithoutDateDto createNewFromToWithoutDate(Menu menu) {
        return new MenuWithoutDateDto(
                menu.getDishName(),
                menu.getPrice()
        );

    }


}
