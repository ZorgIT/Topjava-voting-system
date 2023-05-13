package ru.javaops.topjava2.dto;

public class RestaurantWithIdDto extends AbstractRestaurantDto{
    Long id;

    public RestaurantWithIdDto(Long id, String name) {
        super(name);
        this.id=id;
    }

}
