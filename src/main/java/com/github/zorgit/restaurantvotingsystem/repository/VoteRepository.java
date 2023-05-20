package com.github.zorgit.restaurantvotingsystem.repository;

import com.github.zorgit.restaurantvotingsystem.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findByUserIdAndDateTime(Integer userId, LocalDateTime dateTime);

    @Nullable
    Optional<Vote> findById(Long id);

    @Query("SELECT v FROM Vote v " +
            "JOIN FETCH v.restaurant m " +
            "WHERE v.user.id = :userId " +
            "AND v.dateTime = (SELECT MAX(v2.dateTime) FROM Vote v2 WHERE v2.user.id = :userId)")
    Optional<Vote> findFirstByUserId(@Param("userId") Integer userId);

    Optional<List<Vote>> findByUserId(Integer id);

}
