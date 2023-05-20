package com.github.zorgit.restaurantvotingsystem.dto;

import java.time.LocalDateTime;

public class UserVoteDto {
    private Long restaurantId;
    private LocalDateTime dateTime;

    public UserVoteDto(Long restaurantId, LocalDateTime dateTime) {
        this.restaurantId = restaurantId;
        this.dateTime = dateTime;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

}
