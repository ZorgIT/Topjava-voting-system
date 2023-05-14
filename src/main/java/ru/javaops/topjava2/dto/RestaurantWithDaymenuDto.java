package ru.javaops.topjava2.dto;

public class RestaurantWithDaymenuDto extends RestaurantWithIdDto {
    MenuWithoutDateDto menu;

    public RestaurantWithDaymenuDto(Long id, String name,
                                    MenuWithoutDateDto menu) {
        super(id, name);
        this.menu = menu;
    }

    public MenuWithoutDateDto getMenuDto() {
        return menu;
    }

    public void setMenuDto(MenuWithoutDateDto menuDto) {
        this.menu = menuDto;
    }
}
