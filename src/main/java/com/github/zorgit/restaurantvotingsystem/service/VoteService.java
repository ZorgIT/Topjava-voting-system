package com.github.zorgit.restaurantvotingsystem.service;

import com.github.zorgit.restaurantvotingsystem.dto.UserVoteDto;
import com.github.zorgit.restaurantvotingsystem.error.NotFoundException;
import com.github.zorgit.restaurantvotingsystem.error.VoteChangeNotAllowedException;
import com.github.zorgit.restaurantvotingsystem.model.Menu;
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
                .map(v -> new UserVoteDto(v.getMenu().getRestaurant().getId(), v.getDateTime()))
                .collect(Collectors.toList());
    }

    public Optional<Vote> getVoteById(Long id) {
        return voteRepository.findById(id);
    }

    @Transactional
    public void saveVote(UserVoteDto userVoteDto) {
        AuthUser authUser = AuthUser.get();
        LocalDateTime voteBoundaries = LocalDate.now().atTime(11, 0);
        if (LocalDateTime.now().isAfter(voteBoundaries)) {
            voteBoundaries = voteBoundaries.plusDays(1);
        }
        final LocalDateTime menuDate = voteBoundaries;

        Optional<Vote> voteOptional =
                voteRepository.findByUserIdAndDateTime(authUser.id(),
                        menuDate);
        if (voteOptional.isPresent()) {
            Vote vote = voteOptional.get();
            if (LocalDateTime.now().isAfter(voteBoundaries)) {
                throw new VoteChangeNotAllowedException();
            }
            Menu menu = vote.getMenu();
            Restaurant restaurant = menu.getRestaurant();
            if (restaurant.getId().equals(userVoteDto.getRestaurantId())) {
                return;
            }
            List<Menu> menuList =
                    menuRepository.findByRestaurantIdAndDate(userVoteDto.getRestaurantId(),
                            menuDate.toLocalDate());
            if (!menuList.isEmpty()) {
                Menu menuDay = menuList.get(0);
                vote.setMenu(menuDay);
                voteRepository.save(vote);
            } else {
                throw new NotFoundException("Menu not found for restaurant with id: " +
                        userVoteDto.getRestaurantId() + " and date: " +
                        menuDate.toLocalDate());
            }
        } else {
            Restaurant restaurant = restaurantRepository.findById(userVoteDto.getRestaurantId())
                    .orElseThrow(() -> new NotFoundException("Restaurant with id: "
                            + userVoteDto.getRestaurantId() + " not found"));
            List<Menu> menuList =
                    menuRepository.findByRestaurantIdAndDate(restaurant.getId(),
                            menuDate.toLocalDate());
            if (!menuList.isEmpty()) {
                Menu menu = menuList.get(0);
                Vote vote = new Vote();
                vote.setUser(authUser.getUser());
                vote.setDate(menuDate);
                vote.setMenu(menu);
                voteRepository.save(vote);
            } else {
                throw new NotFoundException("Menu not found for restaurant with id: " +
                        restaurant.getId() + " and date: " +
                        menuDate.toLocalDate());
            }
        }
    }

    public void deleteVoteById(Long voteId) {
        Optional<Vote> vote = voteRepository.findById(voteId);
        if (vote.isPresent()) {
            voteRepository.delete(vote.get());
        } else {
            throw new IllegalArgumentException("Vote with id" +
                    voteId +
                    " not found");
        }
    }

    @Transactional(readOnly = true)
    public Optional<UserVoteDto> getMostRecentUserVote(Integer userId) {
        Optional<Vote> vote = voteRepository.findFirstByUserId(userId);
        return VoteUtil.asToOptional(vote);
    }
}
