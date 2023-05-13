package ru.javaops.topjava2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.javaops.topjava2.model.Vote;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findByUserIdAndDate(Integer userId, LocalDate date);

    Optional<Vote> findById(Long id);

}
