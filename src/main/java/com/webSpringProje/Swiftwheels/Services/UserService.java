package com.webSpringProje.Swiftwheels.Services;

import com.webSpringProje.Swiftwheels.DTO.UsersDto;
import com.webSpringProje.Swiftwheels.Entity.Users;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<Users> findByEmail(String email);

    boolean emailTaken(String email);

    /** Yeni kullanıcıyı ROLE_USER ile, şifresini BCrypt ile kaydeder. */
    Users register(UsersDto dto);

    List<Users> all();

    /** Admin rolünü verir/geri alır. Kendi rolünü kaldırmaya izin vermez. */
    void setAdmin(Long userId, boolean admin, String actorEmail);
}
