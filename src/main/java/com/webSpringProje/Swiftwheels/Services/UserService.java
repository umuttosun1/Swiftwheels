package com.webSpringProje.Swiftwheels.Services;

import com.webSpringProje.Swiftwheels.Entity.Users;
import java.util.Optional;

public interface UserService {
    Optional<Users> findByEmail(String email);
    void saveUser(Users user);
}