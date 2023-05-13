package ru.javaops.topjava2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javaops.topjava2.error.NotFoundException;
import ru.javaops.topjava2.model.Vote;
import ru.javaops.topjava2.service.VoteService;

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
