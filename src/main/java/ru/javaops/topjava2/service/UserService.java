package ru.javaops.topjava2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javaops.topjava2.model.User;
import ru.javaops.topjava2.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Integer id ){
        return userRepository.findById(id);
    }
    //TODO причесать CRUD. добавить валидацию. Проверить ТЗ
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }
    public void deleteUser(User user) {
        userRepository.delete(user);
    }



}
