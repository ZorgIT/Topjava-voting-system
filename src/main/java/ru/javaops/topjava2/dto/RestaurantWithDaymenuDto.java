package ru.javaops.topjava2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.javaops.topjava2.util.validation.NoHtml;

import java.math.BigDecimal;

public class RestaurantWithDaymenuDto extends RestaurantWithIdDto{
    MenuWithoutDateDto menu;

    public RestaurantWithDaymenuDto(Long id, String name,MenuWithoutDateDto menu) {
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
