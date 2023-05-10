package ru.javaops.topjava2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javaops.topjava2.model.User;
import ru.javaops.topjava2.model.Vote;
import ru.javaops.topjava2.repository.VoteRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class VoteService {
    private final VoteRepository voteRepository;

    @Autowired
    public VoteService(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    public List<Vote> getAllVotes() {
        return voteRepository.findAll();
    }

    public Optional<Vote> getVoteById(Long id) {
        return voteRepository.findById(id);
    }



    public Optional<Vote> getVoteByUserAndVoteDate(User user, LocalDate voteDate) {
        return voteRepository.findByUserIdAndDate(user.getId().longValue(), voteDate);
    }

    public List<Vote> getVotesByVoteDate(LocalDate voteDate) {
        return voteRepository.findByDate(voteDate);
    }

    public Vote createVote(Vote vote) {
        return voteRepository.save(vote);
    }

    public void updateVote(Vote vote) {
        voteRepository.save(vote);
    }

    public void deleteVote(Vote vote) {
        voteRepository.delete(vote);
    }
}
