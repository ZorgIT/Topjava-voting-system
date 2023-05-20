package com.github.zorgit.restaurantvotingsystem.dto;

import com.github.zorgit.restaurantvotingsystem.model.Menu;

import java.util.List;

public class RestaurantWithDaymenuDto extends RestaurantWithIdDto {
    List<MenuDto> menuDtos;

    public RestaurantWithDaymenuDto(Long id, String name,
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
