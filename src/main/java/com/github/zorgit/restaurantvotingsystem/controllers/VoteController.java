package com.github.zorgit.restaurantvotingsystem.controllers;

import com.github.zorgit.restaurantvotingsystem.dto.UserVoteDto;
import com.github.zorgit.restaurantvotingsystem.service.VoteService;
import com.github.zorgit.restaurantvotingsystem.util.user.AuthUser;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        voteService.saveVote(userVoteDto);
        return ResponseEntity.ok(userVoteDto);
    }

    @GetMapping()
    public ResponseEntity<List<UserVoteDto>> getAllUserVote() {
        AuthUser authUser = AuthUser.get();
        List<UserVoteDto> userVoteDtos = voteService.getAllByUserId(authUser.id());
        return ResponseEntity.ok(userVoteDtos);
    }

    @GetMapping("/last-vote")
    public ResponseEntity<Optional<UserVoteDto>> getLastUserVote() {
        AuthUser authUser = AuthUser.get();
        Optional<UserVoteDto> voteDto =
                voteService.getMostRecentUserVote(authUser.id());
        return ResponseEntity.ok(voteDto);
    }

}
