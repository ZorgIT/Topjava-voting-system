package com.github.zorgit.restaurantvotingsystem.service;

import com.github.zorgit.restaurantvotingsystem.dto.UserVoteDto;
import com.github.zorgit.restaurantvotingsystem.error.NotFoundException;
import com.github.zorgit.restaurantvotingsystem.error.VoteChangeNotAllowedException;
import com.github.zorgit.restaurantvotingsystem.model.Restaurant;
import com.github.zorgit.restaurantvotingsystem.model.Vote;
import com.github.zorgit.restaurantvotingsystem.repository.MenuRepository;
import com.github.zorgit.restaurantvotingsystem.repository.RestaurantRepository;
import com.github.zorgit.restaurantvotingsystem.repository.VoteRepository;
import com.github.zorgit.restaurantvotingsystem.util.VoteUtil;
import com.github.zorgit.restaurantvotingsystem.util.user.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class VoteService {
    private final VoteRepository voteRepository;
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    @Autowired

    public VoteService(VoteRepository voteRepository,
                       MenuRepository menuRepository,
                       RestaurantRepository restaurantRepository) {
        this.voteRepository = voteRepository;
        this.menuRepository = menuRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public Collection<Vote> getAllVotes() {
        return voteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<UserVoteDto> getAllByUserId(Integer userId) {
        List<Vote> votes = voteRepository.findByUserId(userId).orElse(Collections.emptyList());
        if (votes.isEmpty()) {
            throw new NotFoundException("No votes found for user with id: " + userId);
        }
        return votes.stream()
                .map(v -> new UserVoteDto(v.getRestaurant().getId(), v.getDateTime()))
                .collect(Collectors.toList());
    }

    public UserVoteDto saveVote(UserVoteDto userVoteDto) {
        AuthUser authUser = AuthUser.get();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime voteDeadline = LocalDate.now().atTime(11, 0);

        if (now.isAfter(voteDeadline)) {
            voteDeadline = voteDeadline.plusDays(1);
        }

        Optional<Vote> existingVote =
                voteRepository.findByUserIdAndDateTime(authUser.id(), voteDeadline);

        if (existingVote.isPresent() && now.isAfter(voteDeadline)) {
            throw new VoteChangeNotAllowedException();
        }

        Restaurant restaurant = restaurantRepository.findById(userVoteDto.getRestaurantId())
                .orElseThrow(() -> new NotFoundException(userVoteDto.getRestaurantId().toString()));

        if (existingVote.isPresent()) {
            existingVote.get().setRestaurant(restaurant);
            voteRepository.save(existingVote.get());
        } else {
            Vote newVote = new Vote(authUser.getUser(), restaurant, voteDeadline);
            voteRepository.save(newVote);
        }
        return new UserVoteDto(userVoteDto.getRestaurantId(), voteDeadline);
    }

    @Transactional(readOnly = true)
    public Optional<UserVoteDto> getMostRecentUserVote(Integer userId) {
        Optional<Vote> vote = voteRepository.findFirstByUserId(userId);
        return VoteUtil.asToOptional(vote);
    }
}
