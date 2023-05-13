package ru.javaops.topjava2.dto;

//TODO add valid data
public class RestaurantDto extends AbstractRestaurantDto {

    public RestaurantDto() {
        super();
    }

    public RestaurantDto(long id, String name) {
        super(name);
    }

}
