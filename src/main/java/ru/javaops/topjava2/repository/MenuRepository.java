package ru.javaops.topjava2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.javaops.topjava2.model.Menu;
import ru.javaops.topjava2.model.Restaurant;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByRestaurantIdAndDate(Long restaurantId, LocalDate date);

    List<Menu> findByRestaurantId(Long restaurantId);

    List<Menu> findByDate(LocalDate date);

    Optional<Menu> findByRestaurantAndDate(Restaurant restaurant, LocalDate date);

    @Query("SELECT m FROM Menu m JOIN FETCH m.restaurant WHERE m.date = :date")
    List<Menu> findByDateWithRestaurant(@Param("date") LocalDate date);


}
