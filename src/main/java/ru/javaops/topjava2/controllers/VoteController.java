package ru.javaops.topjava2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javaops.topjava2.model.User;
import ru.javaops.topjava2.model.Vote;
import ru.javaops.topjava2.service.UserService;
import ru.javaops.topjava2.service.VoteService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/votes")
public class VoteController {
    private final VoteService voteService;

    private final UserService userService;

    @Autowired
    public VoteController(VoteService voteService, UserService userService) {
        this.voteService = voteService;
        this.userService = userService;
    }

    @GetMapping("")
    public List<Vote> getAllVotes() {
        return voteService.getAllVotes();
    }

    @GetMapping("/{userId}/{voteDate}")
    public ResponseEntity<Vote> getVoteByUserAndVoteDate(
            @PathVariable Long userId,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate voteDate) {
        /*
        todo сервис на пользователя или откуда его доставать?
        Проверить ТЗ - нужно ли методы на cruds пользователя
        */
        Optional<User> user = userService.getUserById(userId.intValue());
        if (user.isPresent()) {
            Optional<Vote> vote = voteService.getVoteByUserAndVoteDate(user.get(), voteDate);
            if (vote.isPresent()) {
                return ResponseEntity.ok(vote.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("")
    public ResponseEntity<Vote> createVote(@RequestBody Vote vote) {
        Optional<Vote> existingVote = voteService.getVoteByUserAndVoteDate(vote.getUser(), vote.getDate());

        if(existingVote.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } else {
            Vote newVote = voteService.createVote(vote);
            return ResponseEntity.status(HttpStatus.CREATED).body(newVote);
        }
    }

    @PutMapping("")
    public ResponseEntity<Vote> updateVote(@RequestBody Vote vote) {
        Optional<Vote> existingVote = voteService.getVoteByUserAndVoteDate(vote.getUser(),vote.getDate());
        if (existingVote.isPresent()) {
            if (LocalTime.now().isBefore(LocalTime.of(11,0))) {
                vote.setId(existingVote.get().getId());
                voteService.updateVote(vote);
                return ResponseEntity.ok(vote);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVote(@PathVariable Long id) {
        Optional<Vote> existingVote = voteService.getVoteById(id);

        if (existingVote.isPresent()) {
            voteService.deleteVote(existingVote.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
