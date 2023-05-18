package com.github.zorgit.restaurantvotingsystem.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.github.zorgit.restaurantvotingsystem.error.NotFoundException;
import com.github.zorgit.restaurantvotingsystem.model.Vote;
import com.github.zorgit.restaurantvotingsystem.service.VoteService;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/votes")
public class AdminVoteController {
    private final VoteService voteService;

    @Autowired
    public AdminVoteController(VoteService voteService) {
        this.voteService = voteService;
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

    @DeleteMapping("/{VoteId}")
    public ResponseEntity<Void> deleteVote(@PathVariable Long VoteId) {
        voteService.deleteVoteById(VoteId);
        return ResponseEntity.noContent().build();
    }

}
