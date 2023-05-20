package com.github.zorgit.restaurantvotingsystem.service;

import com.github.zorgit.restaurantvotingsystem.dto.UserVoteDto;
import com.github.zorgit.restaurantvotingsystem.error.NotFoundException;
import com.github.zorgit.restaurantvotingsystem.error.VoteChangeNotAllowedException;
import com.github.zorgit.restaurantvotingsystem.model.Restaurant;
import com.github.zorgit.restaurantvotingsystem.model.Vote;
import com.github.zorgit.restaurantvotingsystem.repository.RestaurantRepository;
import com.github.zorgit.restaurantvotingsystem.repository.VoteRepository;
import com.github.zorgit.restaurantvotingsystem.util.VoteUtil;
import com.github.zorgit.restaurantvotingsystem.util.user.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class VoteService {
    private final VoteRepository voteRepository;
    private final RestaurantRepository restaurantRepository;

    @Autowired

    public VoteService(VoteRepository voteRepository,
                       RestaurantRepository restaurantRepository) {
        this.voteRepository = voteRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional(readOnly = true)
    public Vote get(Long id) {
        AuthUser authUser = AuthUser.get();
        Vote vote = voteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No votes found for " +
                        "user with id: " + id));
        if (!vote.getUser().getId().equals(authUser.id())) {
            throw new AccessDeniedException("You are not authorized to access this vote");
        }
        return vote;
    }

    public List<UserVoteDto> getAllByUserId(Integer userId) {
        List<Vote> votes = voteRepository.findByUserId(userId).orElse(Collections.emptyList());
        if (votes.isEmpty()) {
            throw new NotFoundException("No votes found for user with id: " + userId);
        }
        return votes.stream()
                .map(v -> new UserVoteDto(v.getRestaurant().getId(), v.getDateTime()))
                .collect(Collectors.toList());
    }

    @Transactional
    public UserVoteDto saveVote(Long restaurantId) {
        AuthUser authUser = AuthUser.get();
        LocalDate today = LocalDate.now();
        LocalDateTime voteDeadline = today.atTime(11, 0);
        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(voteDeadline)) {
            Optional<Vote> existingVote = voteRepository.findByUserIdAndDateTime(authUser.id(), voteDeadline);
            if (existingVote.isPresent()) {
                if (existingVote.get().getRestaurant().getId().equals(restaurantId)) {
                    return new UserVoteDto(existingVote.get().getRestaurant().getId(), existingVote.get().getDateTime());
                } else {
                    existingVote.get().setRestaurant(restaurantRepository.findById(restaurantId)
                            .orElseThrow(() -> new NotFoundException(restaurantId.toString())));
                    voteRepository.save(existingVote.get());
                    return new UserVoteDto(existingVote.get().getRestaurant().getId(), existingVote.get().getDateTime());
                }
            } else {
                Restaurant restaurant = restaurantRepository.findById(restaurantId)
                        .orElseThrow(() -> new NotFoundException(restaurantId.toString()));
                Vote newVote = new Vote(authUser.getUser(), restaurant, voteDeadline);
                voteRepository.save(newVote);
                return new UserVoteDto(newVote.getRestaurant().getId(), newVote.getDateTime());
            }
        } else {
            throw new VoteChangeNotAllowedException();
        }
    }

    @Transactional(readOnly = true)
    public Optional<UserVoteDto> getMostRecentUserVote(Integer userId) {
        Optional<Vote> vote = voteRepository.findFirstByUserId(userId);
        return VoteUtil.asToOptional(vote);
    }
}
