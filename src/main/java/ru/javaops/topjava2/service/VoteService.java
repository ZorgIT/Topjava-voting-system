package ru.javaops.topjava2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.topjava2.dto.VoteDto;
import ru.javaops.topjava2.error.NotFoundException;
import ru.javaops.topjava2.error.VoteChangeNotAllowedException;
import ru.javaops.topjava2.model.Menu;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.model.Vote;
import ru.javaops.topjava2.repository.MenuRepository;
import ru.javaops.topjava2.repository.RestaurantRepository;
import ru.javaops.topjava2.repository.VoteRepository;
import ru.javaops.topjava2.web.AuthUser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VoteService {
    private final VoteRepository voteRepository;
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    @Autowired

    public VoteService(VoteRepository voteRepository, MenuRepository menuRepository, RestaurantRepository restaurantRepository) {
        this.voteRepository = voteRepository;
        this.menuRepository = menuRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public Collection<Vote> getAllVotes() {
        return voteRepository.findAll();
    }

    public Optional<Vote> getVoteById(Long id) {
        return voteRepository.findById(id);
    }

    @Transactional
    public void saveVote(VoteDto voteDto) {
        AuthUser authUser = AuthUser.get();
        LocalDateTime voteBoundaries = LocalDate.now().atTime(11, 0);
        if (LocalDateTime.now().isAfter(voteBoundaries)) {
            voteBoundaries = voteBoundaries.plusDays(1);
        }
        final LocalDateTime menuDate = voteBoundaries;

        Optional<Vote> voteOptional = voteRepository.findByUserIdAndDate(authUser.id(), menuDate.toLocalDate());
        if (voteOptional.isPresent()) {
            Vote vote = voteOptional.get();
            if (LocalDateTime.now().isAfter(voteBoundaries)) {
                throw new VoteChangeNotAllowedException();
            }
            Menu menu = vote.getMenu();
            Restaurant restaurant = menu.getRestaurant();
            if (restaurant.getId().equals(voteDto.getRestaurantId())) {
                return;
            }
            List<Menu> menuList = menuRepository.findByRestaurantIdAndDate(voteDto.getRestaurantId(), menuDate.toLocalDate());
            if (!menuList.isEmpty()) {
                Menu menuDay = menuList.get(0);
                vote.setMenu(menuDay);
                voteRepository.save(vote);
            } else {
                throw new NotFoundException("Menu not found for restaurant with id: " + voteDto.getRestaurantId() + " and date: " + menuDate.toLocalDate());
            }
        } else {
            Restaurant restaurant = restaurantRepository.findById(voteDto.getRestaurantId())
                    .orElseThrow(() -> new NotFoundException("Restaurant with id: "
                            + voteDto.getRestaurantId() + " not found"));
            List<Menu> menuList = menuRepository.findByRestaurantIdAndDate(restaurant.getId(), menuDate.toLocalDate());
            if (!menuList.isEmpty()) {
                Menu menu = menuList.get(0);
                Vote vote = new Vote();
                vote.setUser(authUser.getUser());
                vote.setDate(menuDate.toLocalDate());
                vote.setMenu(menu);
                voteRepository.save(vote);
            } else {
                throw new NotFoundException("Menu not found for restaurant with id: " + restaurant.getId() + " and date: " + menuDate.toLocalDate());
            }
        }
    }

    @Transactional(readOnly = true)
    public List<Restaurant> getRestaurantsWithMenus() {
        LocalDateTime voteBoundaries = LocalDate.now().atTime(11, 0);
        if (LocalDateTime.now().isAfter(voteBoundaries)) {
            voteBoundaries = voteBoundaries.plusDays(1);
        }
        final LocalDateTime menuDate = voteBoundaries;
        return restaurantRepository.findAll().stream()
                .filter(restaurant -> !menuRepository.findByRestaurantIdAndDate(restaurant.getId(),
                        menuDate.toLocalDate()).isEmpty())
                .peek(restaurant -> {
                    List<Menu> menus = menuRepository.findByRestaurantIdAndDate(restaurant.getId(),
                            menuDate.toLocalDate());
                    restaurant.setMenus(menus);
                })
                .collect(Collectors.toList());
    }

    public void deleteVoteById(Long voteId) {
        Optional<Vote> vote = voteRepository.findById(voteId);
        if (vote.isPresent()) {
            voteRepository.delete(vote.get());
        } else {
            throw new IllegalArgumentException("Vote with id" + voteId +" not found");
        }
    }
}
