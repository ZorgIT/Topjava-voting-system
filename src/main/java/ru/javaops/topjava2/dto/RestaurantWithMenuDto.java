package ru.javaops.topjava2.dto;

import java.util.List;

public class RestaurantWithMenuDto {
    private Long id;
    private String name;
    private List<MenuDto> menus;

    public RestaurantWithMenuDto(){

    }
    public RestaurantWithMenuDto(Long id, String name, List<MenuDto> menus) {
        this.id = id;
        this.name = name;
        this.menus = menus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MenuDto> getMenus() {
        return menus;
    }

    public void setMenus(List<MenuDto> menus) {
        this.menus = menus;
    }
}
