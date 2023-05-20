package com.github.zorgit.restaurantvotingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.github.zorgit.restaurantvotingsystem.model.Restaurant;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Optional<Restaurant> findById(Long id);

    @Query("SELECT r.id, r.name, m.dateTime, m.dishName, m.price " +
            "FROM Restaurant r " +
            "LEFT JOIN r.menus m " +
            "WHERE m.dateTime BETWEEN :startDateTime AND :endDateTime " +
            "ORDER BY r.id, m.dateTime")
    List<Object[]> getDayMenu(@Param("startDateTime") LocalDateTime startDateTime,
                              @Param("endDateTime") LocalDateTime endDateTime);

}
