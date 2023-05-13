package ru.javaops.topjava2.dto;

public class RestaurantWithIdDto extends AbstractRestaurantDto{

    private Long id;


    public RestaurantWithIdDto() {
    }

    public RestaurantWithIdDto(Long id, String name) {
        super(name);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
