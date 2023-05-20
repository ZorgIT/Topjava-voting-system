package com.github.zorgit.restaurantvotingsystem.repository;

import com.github.zorgit.restaurantvotingsystem.model.Menu;
import com.github.zorgit.restaurantvotingsystem.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByRestaurantIdAndAndDateTime(Long restaurantId,
                                                LocalDateTime dateTime);

    Menu findByRestaurantAndDateTime(Restaurant restaurant,
                                     LocalDateTime dateTime);
}
