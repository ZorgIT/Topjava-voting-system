package ru.javaops.topjava2.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javaops.topjava2.dto.RestaurantWithDaymenuDto;
import ru.javaops.topjava2.dto.VoteDto;
import ru.javaops.topjava2.repository.MenuRepository;
import ru.javaops.topjava2.repository.RestaurantRepository;
import ru.javaops.topjava2.service.UserService;
import ru.javaops.topjava2.service.VoteService;
import ru.javaops.topjava2.util.RestaurantUtil;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/votes")
public class VoteController {
    private final VoteService voteService;

    @Autowired
    public VoteController(VoteService voteService, UserService userService, MenuRepository menuRepository, RestaurantRepository restaurantRepository) {
        this.voteService = voteService;
    }

    @PostMapping
    public ResponseEntity<VoteDto> saveVote(@RequestBody @Valid VoteDto voteDto) {
        voteService.saveVote(voteDto);
        return ResponseEntity.ok(voteDto);
    }

    @GetMapping("/dayMenu")
    public ResponseEntity<List<RestaurantWithDaymenuDto>> getRestaurantsWithMenus() {
        return ResponseEntity.ok(
                voteService.getRestaurantsWithMenus().stream()
                        .map(RestaurantUtil::asToWithMenu)
                        .collect(Collectors.toList())
        );
    }

}
