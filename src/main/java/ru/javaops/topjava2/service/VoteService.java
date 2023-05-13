package ru.javaops.topjava2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
            voteBoundaries = voteBoundaries.plusDays(1); // update the new variable instead of currentDate
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
                Menu menuDay = menuList.get(0); // одно меню на одну дату
                vote.setMenu(menuDay);
                voteRepository.save(vote);
            } else {
                throw new NotFoundException("Menu not found for restaurant with id: " + voteDto.getRestaurantId() + " and date: " + menuDate.toLocalDate());
            }
        } else {
            Restaurant restaurant = restaurantRepository.findById(voteDto.getRestaurantId())
                    .orElseThrow(() -> new NotFoundException("Restaurant with id: "
                            + voteDto.getRestaurantId()+ " not found"));
            List<Menu> menuList = menuRepository.findByRestaurantIdAndDate(restaurant.getId(), menuDate.toLocalDate());
            if (!menuList.isEmpty()) {
                Menu menu = menuList.get(0); // одно меню на одну дату
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
//    @Transactional
//    public void saveVote(VoteDto voteDto, User user) {
//        LocalDate currentDate = LocalDate.now();
//
//        Optional<Vote> voteOptional = voteRepository.findByUserIdAndDate(user.getId(), currentDate);
//        if (voteOptional.isPresent()) {
//            Vote vote = voteOptional.get();
//            if (LocalDateTime.now().isAfter(currentDate.atTime(11, 0))) {
//                throw new VoteChangeNotAllowedException();
//            }
//            Menu menu = vote.getMenu();
//            Restaurant restaurant = menu.getRestaurant();
//            if (restaurant.getId().equals(voteDto.getRestaurantId())) {
//                return;
//            }
//            List<Menu> menuList = menuRepository.findByRestaurantIdAndDate(voteDto.getRestaurantId(), currentDate);
//            if (!menuList.isEmpty()) {
//                Menu menuDay = menuList.get(0); // одно меню на одну дату
//                vote.setMenu(menuDay);
//                voteRepository.save(vote);
//            } else {
//                throw new NotFoundException("Menu with id" + menuList.get(0) +
//                        " in restaurant" + voteDto.getRestaurantId() + " not found");
//            }
//        } else {
//            Vote vote = new Vote();
//            Restaurant restaurant = restaurantRepository.findById(voteDto.getRestaurantId())
//                    .orElseThrow(() -> new NotFoundException("Restaurant with id: "
//                            + voteDto.getRestaurantId()+ " not found"));
//            List<Menu> menuList = menuRepository.findByRestaurantIdAndDate(restaurant.getId(), currentDate);
//            if (!menuList.isEmpty()) {
//                Menu menu = menuList.get(0); // одно меню на одну дату
//                vote.setMenu(menu);
//                voteRepository.save(vote);
//            } else {
//                throw new NotFoundException("Menu not found for restaurant with id: " + restaurant.getId() + " and date: " + currentDate);
//            }
//            vote.setUser(user);
//            vote.setMenu(menuList.get(0));
//            vote.setDate(currentDate);
//            voteRepository.save(vote);
//        }
//    }

    @Transactional(readOnly = true)
    public List<Restaurant> getRestaurantsWithMenus() {
        LocalDateTime voteBoundaries = LocalDate.now().atTime(11, 0);
        if (LocalDateTime.now().isAfter(voteBoundaries)) {
            voteBoundaries = voteBoundaries.plusDays(1); // update the new variable instead of currentDate
        }
        final LocalDateTime menuDate = voteBoundaries;
        List<Restaurant> restaurantsWithMenus = restaurantRepository.findAll().stream()
                .filter(restaurant -> !menuRepository.findByRestaurantIdAndDate(restaurant.getId(),
                        menuDate.toLocalDate()).isEmpty())
                .map(restaurant -> {
                    List<Menu> menus = menuRepository.findByRestaurantIdAndDate(restaurant.getId(),
                            menuDate.toLocalDate());
                    restaurant.setMenus(menus);
                    return restaurant;
                })
                .collect(Collectors.toList());
        return restaurantsWithMenus;
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
