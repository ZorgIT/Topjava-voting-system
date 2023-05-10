package ru.javaops.topjava2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.javaops.topjava2.model.Vote;

import java.time.LocalDate;
import java.util.Optional;

public interface VoteRepository extends JpaRepository {
    Optional<Vote> findByUserIdAndDate(Long userId, LocalDate date);
}
