package ru.javaops.topjava2.dto;

//TODO add valid data
public class VoteDto {
    private Long restaurantId;

    public VoteDto() {

    }

    public VoteDto(Long restaurantId) {
        this.restaurantId = restaurantId;
    }


    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }
}
