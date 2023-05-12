package ru.javaops.topjava2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javaops.topjava2.dto.RestaurantWithMenuDto;
import ru.javaops.topjava2.dto.VoteDto;
import ru.javaops.topjava2.service.VoteService;

import java.util.List;

@RestController
@RequestMapping("/api/votes")
public class VoteController {
    private final VoteService voteService;

    @Autowired
    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @GetMapping
    public ResponseEntity<List<RestaurantWithMenuDto>> getRestaurantsWIthMenus() {
        List<RestaurantWithMenuDto> restaurantWithMenuDtos = voteService.getRestaurantsWithMenus();
        return ResponseEntity.ok(restaurantWithMenuDtos);
    }

    @PostMapping
    public ResponseEntity<Void> vote(@RequestBody VoteDto voteDto) {
        voteService.vote(voteDto.getRestaurantId());
        return ResponseEntity.noContent().build();
    }
}
