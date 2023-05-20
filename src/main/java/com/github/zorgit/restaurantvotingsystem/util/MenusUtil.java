package com.github.zorgit.restaurantvotingsystem.util;

import com.github.zorgit.restaurantvotingsystem.dto.MenuDto;
import com.github.zorgit.restaurantvotingsystem.model.Menu;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class MenusUtil {

    public static List<MenuDto> asMenuDtos(List<Menu> menus) {
        return menus.stream().map(MenusUtil::asMenuDto).collect(Collectors.toList());
    }

    public static MenuDto asMenuDto(Menu menu) {
        return new MenuDto(menu.getDateTime(), menu.getDishName(), menu.getPrice());
    }


}
