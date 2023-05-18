package com.github.zorgit.restaurantvotingsystem.controllers;

import com.github.zorgit.restaurantvotingsystem.util.RestaurantUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.github.zorgit.restaurantvotingsystem.dto.RestaurantWithDaymenuDto;
import com.github.zorgit.restaurantvotingsystem.dto.VoteDto;
import com.github.zorgit.restaurantvotingsystem.service.VoteService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/votes")
public class VoteController {
    private final VoteService voteService;

    @Autowired
    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping
    public ResponseEntity<VoteDto> saveVote(@RequestBody @Valid VoteDto voteDto) {
        voteService.saveVote(voteDto);
        return ResponseEntity.ok(voteDto);
    }

}
