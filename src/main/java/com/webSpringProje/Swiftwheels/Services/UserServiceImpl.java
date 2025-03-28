package com.webSpringProje.Swiftwheels.Services;

import com.webSpringProje.Swiftwheels.Entity.Users;
import com.webSpringProje.Swiftwheels.Repos.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<Users> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void saveUser(Users user) {
        userRepository.save(user);
    }
}