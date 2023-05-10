package ru.javaops.topjava2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.javaops.topjava2.model.User;

import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
