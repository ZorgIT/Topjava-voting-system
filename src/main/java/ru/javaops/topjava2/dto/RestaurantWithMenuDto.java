package ru.javaops.topjava2.dto;

import java.util.List;

//TODO add valid data
public class RestaurantWithMenuDto {
    private Long id;
    private String name;
    private List<MenuDto> menus;

    public RestaurantWithMenuDto() {

    }

    public RestaurantWithMenuDto(Long id, String name, List<MenuDto> menus) {
        this.id = id;
        this.name = name;
        this.menus = menus;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<MenuDto> getMenus() {
        return menus;
    }

}
