package com.github.zorgit.restaurantvotingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.github.zorgit.restaurantvotingsystem.model.Menu;
import com.github.zorgit.restaurantvotingsystem.model.Restaurant;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByRestaurantIdAndAndDateTime(Long restaurantId,
                                                LocalDateTime dateTime);

    Menu findByRestaurantAndDateTime(Restaurant restaurant,
                                     LocalDateTime dateTime);


}
