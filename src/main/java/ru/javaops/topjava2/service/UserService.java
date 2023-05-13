package ru.javaops.topjava2.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javaops.topjava2.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    public UserService(UserRepository userRepository, ModelMapper mapper) {
    }


}
