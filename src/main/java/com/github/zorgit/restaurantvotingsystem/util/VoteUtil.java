package com.github.zorgit.restaurantvotingsystem.util;

import com.github.zorgit.restaurantvotingsystem.dto.UserVoteDto;
import com.github.zorgit.restaurantvotingsystem.model.Vote;

import java.time.LocalDateTime;
import java.util.Optional;

public class VoteUtil {
    public static Optional<UserVoteDto> asToOptional(Optional<Vote> voteOptional) {
        return voteOptional
                .map(v -> {
                    Long restaurantId = v.getRestaurant().getId();
                    LocalDateTime dateTime = v.getDateTime();
                    return new UserVoteDto(restaurantId, dateTime);
                });
    }
}
