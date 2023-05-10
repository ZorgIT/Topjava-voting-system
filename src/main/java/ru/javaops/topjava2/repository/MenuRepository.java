package ru.javaops.topjava2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.javaops.topjava2.model.Menu;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    Optional<Menu> findByRestaurantIdAndDate(Long restaurantId, LocalDate date);
}
