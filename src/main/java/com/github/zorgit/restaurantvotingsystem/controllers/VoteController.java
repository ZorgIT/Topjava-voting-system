package com.github.zorgit.restaurantvotingsystem.controllers;

import com.github.zorgit.restaurantvotingsystem.dto.UserVoteDto;
import com.github.zorgit.restaurantvotingsystem.service.VoteService;
import com.github.zorgit.restaurantvotingsystem.util.user.AuthUser;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/votes")
public class VoteController {
    private final VoteService voteService;

    @Autowired
    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping
    public ResponseEntity<UserVoteDto> saveVote(@RequestBody @Valid UserVoteDto userVoteDto) {
        UserVoteDto savedVoteDto = voteService.saveVote(userVoteDto);
        URI locationUri = URI.create("/api/votes/" + savedVoteDto.getId());
        return ResponseEntity.created(locationUri).body(savedVoteDto);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<UserVoteDto> getAllUserVote() {
        AuthUser authUser = AuthUser.get();
        return voteService.getAllByUserId(authUser.id());
    }

    @GetMapping("/last-vote")
    @ResponseStatus(HttpStatus.OK)
    public Optional<UserVoteDto> getLastUserVote() {
        AuthUser authUser = AuthUser.get();
        return voteService.getMostRecentUserVote(authUser.id());
    }

}
