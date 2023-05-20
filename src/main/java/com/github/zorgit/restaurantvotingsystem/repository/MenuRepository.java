package com.github.zorgit.restaurantvotingsystem.repository;

import com.github.zorgit.restaurantvotingsystem.model.Menu;
import com.github.zorgit.restaurantvotingsystem.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface MenuRepository extends JpaRepository<Menu, Long> {
//    List<Menu> findByRestaurantIdAndAndDateTime(Long restaurantId,
//                                                LocalDateTime dateTime);

    Menu findByRestaurantAndDateTime(Restaurant restaurant,
                                     LocalDateTime dateTime);

    Optional<Menu> findByRestaurantIdAndDateTime(Long id,
                                                 LocalDateTime voteDeadline);

//    List<Menu> findByRestaurantIdAndDateTimeBetween(Long id, LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<Menu> findByDateTimeBetween(LocalDateTime startDateTime,
                            LocalDateTime endDateTime);
}
