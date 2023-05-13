package ru.javaops.topjava2.dto;

import java.util.List;

//TODO add valid data
public class RestaurantWithMenuDto extends AbstractRestaurantDto {
    private Long id;
    private List<MenuDto> menus;

    public RestaurantWithMenuDto() {

    }

    public RestaurantWithMenuDto(Long id, String name, List<MenuDto> menus) {
        super(name);
        this.id = id;
        this.menus = menus;
    }

    public Long getId() {
        return id;
    }

    public List<MenuDto> getMenus() {
        return menus;
    }

    public void setMenuDtos(List<MenuDto> menuDtos) {
        this.menus = menuDtos;
    }

}
