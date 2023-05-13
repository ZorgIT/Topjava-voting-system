package ru.javaops.topjava2.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javaops.topjava2.dto.VoteDto;
import ru.javaops.topjava2.error.NotFoundException;
import ru.javaops.topjava2.error.UnauthorizedException;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.model.Vote;
import ru.javaops.topjava2.repository.MenuRepository;
import ru.javaops.topjava2.repository.RestaurantRepository;
import ru.javaops.topjava2.service.UserService;
import ru.javaops.topjava2.service.VoteService;


import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/votes")
public class VoteController {
    private final VoteService voteService;
    private final UserService userService;
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    @Autowired
    public VoteController(VoteService voteService, UserService userService, MenuRepository menuRepository, RestaurantRepository restaurantRepository) {
        this.voteService = voteService;
        this.userService = userService;
        this.menuRepository = menuRepository;
        this.restaurantRepository = restaurantRepository;
    }


    @PostMapping
    public ResponseEntity<Void> saveVote(@RequestBody @Valid VoteDto voteDto) {
        voteService.saveVote(voteDto);
        return ResponseEntity.noContent().build();
    }
   //Нужно ли выносить в отдельный контроллер?
    @GetMapping("/dayMenu")
    public ResponseEntity<List<Restaurant>> getRestaurantsWithMenus(){
        List<Restaurant> Restaurant = voteService.getRestaurantsWithMenus();
        return ResponseEntity.ok(Restaurant);
    }

    @GetMapping
    public ResponseEntity<Collection<Vote>> getAllVotes() {
        return ResponseEntity.ok(voteService.getAllVotes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vote> getVoteById(@PathVariable Long id) {
        Optional<Vote> vote = voteService.getVoteById(id);
        if (vote.isPresent()) {
            return ResponseEntity.ok(vote.get());
        } else {
            throw new NotFoundException("Vote with id: " + id + " not found");
        }
    }

}
