package ru.javaops.topjava2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.topjava2.dto.VoteDto;
import ru.javaops.topjava2.error.NotFoundException;
import ru.javaops.topjava2.error.VoteChangeNotAllowedException;
import ru.javaops.topjava2.model.Menu;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.model.User;
import ru.javaops.topjava2.model.Vote;
import ru.javaops.topjava2.repository.MenuRepository;
import ru.javaops.topjava2.repository.RestaurantRepository;
import ru.javaops.topjava2.repository.VoteRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
    public void saveVote(VoteDto voteDto, User user) {
        LocalDate currentDate = LocalDate.now();
        Optional<Vote> voteOptional = voteRepository.findByUserIdAndDate(user.getId(), currentDate);
        if (voteOptional.isPresent()) {
            Vote vote = voteOptional.get();
            if (LocalDateTime.now().isAfter(currentDate.atTime(11, 0))) {
                throw new VoteChangeNotAllowedException();
            }
            Menu menu = vote.getMenu();
            Restaurant restaurant = menu.getRestaurant();
            if (restaurant.getId().equals(voteDto.getRestaurantId())) {
                return;
            }
            vote.setMenu(menu);
            voteRepository.save(vote);
        } else {
            Optional<Restaurant> restaurant = Optional.ofNullable(restaurantRepository.findById(voteDto.getRestaurantId())
                    .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + voteDto.getRestaurantId())));
            Menu menu = (Menu) Optional.ofNullable(menuRepository.findByRestaurantIdAndDate(restaurant.get().getId(), currentDate))
                    .orElseThrow(() -> new NotFoundException("Menu not found"));
            Vote vote = new Vote();
            vote.setUser(user);
            vote.setMenu(menu);
            vote.setDate(currentDate);
            voteRepository.save(vote);
        }
    }

    @Transactional(readOnly = true)
    public List<Restaurant> getRestaurantsWithMenus() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        LocalDate currentDate = LocalDate.now();
        for (Restaurant restaurant : restaurants) {
            List<Menu> menus = menuRepository.findByRestaurantIdAndDate(restaurant.getId(), currentDate);
            restaurant.setMenus(menus);
        }
        return restaurants;
    }
//
//    public void saveVote(VoteDto voteDto) {
//        User user = userService.getCurrentUser().orElseThrow(UserNotFoundException::new);
//        Optional<Vote> voteOptional = voteRepository.findByUserIdAndDate(user.getId(), LocalDate.now());
//        if (voteOptional.isPresent()) {
//            Vote vote = voteOptional.get();
//            if (LocalDateTime.now().isBefore(LocalDate.now().atTime(11, 0))) {
//                Menu menu = vote.getMenu();
//                Restaurant restaurant = menu.getRestaurant();
//                if (restaurant.getId().equals(voteDto.getRestaurantId())) {
//                    return;
//                }
//                menu = menuRepository.findByRestaurantAndDate(restaurant, LocalDate.now())
//                        .orElseThrow(MenuNotFoundException::new);
//                vote.setMenu(menu);
//                voteRepository.save(vote);
//            } else {
//                throw new VoteChangeNotAllowedException();
//            }
//        } else {
//            Restaurant restaurant = restaurantRepository.findById(voteDto.getRestaurantId())
//                    .orElseThrow(() -> new NotFoundException("Restaurant not found with id: "
//                            + voteDto.getRestaurantId()));
//            Menu menu = menuRepository.findByRestaurantAndDate(restaurant, LocalDate.now())
//                    .orElseThrow(MenuNotFoundException::new);
//            Vote vote = new Vote();
//            vote.setUser(user);
//            vote.setMenu(menu);
//            vote.setDate(LocalDate.now());
//            voteRepository.save(vote);
//        }
//    }
}
