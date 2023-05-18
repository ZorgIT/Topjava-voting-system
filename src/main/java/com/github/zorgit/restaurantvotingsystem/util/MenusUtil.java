package com.github.zorgit.restaurantvotingsystem.util;

import lombok.experimental.UtilityClass;
import com.github.zorgit.restaurantvotingsystem.dto.MenuWithoutDateDto;
import com.github.zorgit.restaurantvotingsystem.model.Menu;

@UtilityClass
public class MenusUtil {

    public static MenuWithoutDateDto createNewFromToWithoutDate(Menu menu) {
        return new MenuWithoutDateDto(
                menu.getDish(),
                menu.getPrice()
        );

    }




}
