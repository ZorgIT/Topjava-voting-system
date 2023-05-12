package ru.javaops.topjava2.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javaops.topjava2.dto.MenuDto;
import ru.javaops.topjava2.dto.RestaurantWithMenuDto;
import ru.javaops.topjava2.error.MenuNotFoundException;
import ru.javaops.topjava2.error.UserNotFoundException;
import ru.javaops.topjava2.error.VoteChangeNotAllowedException;
import ru.javaops.topjava2.model.Menu;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.model.User;
import ru.javaops.topjava2.model.Vote;
import ru.javaops.topjava2.repository.MenuRepository;
import ru.javaops.topjava2.repository.VoteRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class VoteService {
    //Todo Добавить бизнес - логику.
    private final VoteRepository voteRepository;
    private final MenuRepository menuRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public VoteService(VoteRepository voteRepository, MenuRepository menuRepository, UserService userService,
                       ModelMapper modelMapper) {
        this.voteRepository = voteRepository;
        this.menuRepository = menuRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    public List<RestaurantWithMenuDto> getRestaurantsWithMenus() {
        List<Menu> menus = menuRepository.findByDate(LocalDate.now());
        Map<Restaurant, List<MenuDto>> restaurantMenuMap = new HashMap<>();
        for (Menu menu : menus) {
            Restaurant restaurant = menu.getRestaurant();
            List<MenuDto> menuDtos = restaurantMenuMap.getOrDefault(restaurant, new ArrayList<>());
            menuDtos.add(modelMapper.map(menu, MenuDto.class));
            restaurantMenuMap.put(restaurant, menuDtos);
        }
        List<RestaurantWithMenuDto> restaurantWithMenuDtos = new ArrayList<>();
        for (Map.Entry<Restaurant, List<MenuDto>> entry : restaurantMenuMap.entrySet()) {
            Restaurant restaurant = entry.getKey();
            List<MenuDto> menuDtos = entry.getValue();
            restaurantWithMenuDtos.add(new RestaurantWithMenuDto(restaurant.getId(), restaurant.getName(), menuDtos));
        }
        return restaurantWithMenuDtos;
    }

    public void vote(Long restaurantId) {
        User user = userService.getCurrentUser().orElseThrow(UserNotFoundException::new);
        Optional<Vote> voteOptional = voteRepository.findByUserIdAndDate(user.getId(), LocalDate.now());
        if (voteOptional.isPresent()) {
            Vote vote = voteOptional.get();
            if (LocalDateTime.now().isBefore(LocalDate.now().atTime(11, 0))) {
                Menu menu = menuRepository.findById(vote.getMenu().getId()).orElseThrow(MenuNotFoundException::new);
                Restaurant restaurant = menu.getRestaurant();
                if (restaurant.getId().equals(restaurantId)) {
                    return;
                }
                menu = menuRepository.findByRestaurantAndDate(restaurantId,
                        LocalDate.now()).orElseThrow(MenuNotFoundException::new);
                vote.setMenu(menu);
                voteRepository.save(vote);
            } else {
                throw new VoteChangeNotAllowedException();
            }
        } else {
            Menu menu = menuRepository.findByRestaurantAndDate(restaurantId,
                    LocalDate.now()).orElseThrow(MenuNotFoundException::new);
            Vote vote = new Vote();
            vote.setUser(user);
            vote.setMenu(menu);
            vote.setDate(LocalDate.now());
            voteRepository.save(vote);
        }
    }
}
