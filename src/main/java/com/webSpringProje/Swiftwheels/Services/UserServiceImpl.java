package com.webSpringProje.Swiftwheels.Services;

import com.webSpringProje.Swiftwheels.DTO.UsersDto;
import com.webSpringProje.Swiftwheels.Entity.Users;
import com.webSpringProje.Swiftwheels.Repos.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<Users> findByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }

    @Override
    public boolean emailTaken(String email) {
        return email != null && userRepository.existsByEmailIgnoreCase(email.trim());
    }

    @Override
    @Transactional
    public Users register(UsersDto dto) {
        Users user = new Users();
        user.setFirstName(dto.getFirstName().trim());
        user.setLastName(dto.getLastName().trim());
        user.setEmail(dto.getEmail().trim().toLowerCase(java.util.Locale.ROOT));
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setBirthDate(dto.getBirthDate());
        user.setPhoneNumber(dto.getPhoneNumber().replaceAll("\s", ""));
        user.setRoles(new HashSet<>(Set.of("ROLE_USER")));
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Users> all() {
        return userRepository.findAllByOrderByIdAsc();
    }

    @Override
    @Transactional
    public void setAdmin(Long userId, boolean admin, String actorEmail) {
        Users u = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("Kullanıcı bulunamadı"));
        if (!admin && u.getEmail().equalsIgnoreCase(actorEmail)) {
            throw new IllegalStateException("Kendi admin yetkinizi kaldıramazsınız.");
        }
        Set<String> roles = new HashSet<>(u.getRoles());
        if (admin) roles.add("ROLE_ADMIN"); else roles.remove("ROLE_ADMIN");
        roles.add("ROLE_USER");
        u.setRoles(roles);
    }
}
