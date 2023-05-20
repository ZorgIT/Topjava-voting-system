package com.github.zorgit.restaurantvotingsystem.dto;

import java.util.List;

public class RestaurantWithDayMenuDto extends RestaurantWithIdDto {
    List<MenuDto> menuDtos;

    public RestaurantWithDayMenuDto(Long id, String name,
                                    List<MenuDto> menuDtos) {
        super(id, name);
        this.menuDtos = menuDtos;
    }

    public List<MenuDto> getMenuDtos() {
        return menuDtos;
    }

    public void setMenuDtos(List<MenuDto> menuDtos) {
        this.menuDtos = menuDtos;
    }
}
