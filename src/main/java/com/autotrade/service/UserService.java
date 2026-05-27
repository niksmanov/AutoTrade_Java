package com.autotrade.service;

import com.autotrade.domain.AppUser;
import com.autotrade.domain.UserRole;
import com.autotrade.dto.UserDto;
import com.autotrade.repository.AppUserRepository;
import com.autotrade.repository.TownRepository;
import jakarta.persistence.criteria.JoinType;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UserService {
    private final AppUserRepository users;
    private final TownRepository towns;
    private final PasswordEncoder passwordEncoder;
    private final MappingService mapper;

    @Transactional(readOnly = true)
    public UserDto getByEmail(String email) {
        return users.findByEmailIgnoreCase(email).map(mapper::toUserDto).orElse(null);
    }

    @Transactional(readOnly = true)
    public UserDto getById(UUID id) {
        return users.findById(id).map(mapper::toUserDto).orElse(null);
    }

    public boolean exists(String email) {
        return users.existsByEmailIgnoreCase(email);
    }

    @Transactional
    public AppUser register(UserDto model) {
        var user = new AppUser();
        user.setEmail(model.getEmail());
        user.setUsername(StringUtils.hasText(model.getUserName()) ? model.getUserName() : model.getEmail());
        user.setPasswordHash(passwordEncoder.encode(model.getPassword()));
        user.setLockoutEnabled(false);
        user.getRoles().add(UserRole.USER);
        return users.save(user);
    }

    @Transactional
    public boolean editUser(UserDto model) {
        return users.findById(model.getId()).map(user -> {
            user.setAddress(model.getAddress());
            user.setPhoneNumber(model.getPhoneNumber());
            user.setTown(model.getTownId() == null ? null : towns.findById(model.getTownId()).orElse(null));
            return true;
        }).orElse(false);
    }

    @Transactional
    public boolean removeUser(UUID id) {
        if (!users.existsById(id)) {
            return false;
        }
        users.deleteById(id);
        return true;
    }

    @Transactional
    public boolean changeRole(UserDto model) {
        return users.findById(model.getId()).map(user -> {
            user.getRoles().clear();
            user.getRoles().add(UserRole.USER);
            if (model.isAdmin()) {
                user.getRoles().add(UserRole.ADMIN);
            }
            return true;
        }).orElse(false);
    }

    @Transactional(readOnly = true)
    public List<UserDto> getUsers(int page, int size, String search) {
        Specification<AppUser> spec = (root, query, cb) -> {
            if (!Long.class.equals(query.getResultType()) && !long.class.equals(query.getResultType())) {
                root.fetch("roles", JoinType.LEFT);
                query.distinct(true);
            }
            if (!StringUtils.hasText(search)) {
                return cb.conjunction();
            }
            var value = "%" + search.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("email")), value),
                    cb.like(cb.lower(root.get("username")), value));
        };

        return users.findAll(spec, PageRequest.of(page, size)).stream()
                .map(mapper::toUserDto)
                .sorted(Comparator.comparing(UserDto::isAdmin).reversed().thenComparing(UserDto::getEmail))
                .toList();
    }

    @Transactional
    public boolean resetPassword(String email, String newPassword) {
        return users.findByEmailIgnoreCase(email).map(user -> {
            user.setPasswordHash(passwordEncoder.encode(newPassword));
            return true;
        }).orElse(false);
    }

    @Transactional
    public boolean resetPasswordForCurrentUser(UUID userId, String oldPassword, String newPassword) {
        return users.findById(userId).map(user -> {
            if (!passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
                return false;
            }
            user.setPasswordHash(passwordEncoder.encode(newPassword));
            return true;
        }).orElse(false);
    }

    @Transactional
    public boolean confirmEmail(UUID id) {
        return users.findById(id).map(user -> {
            user.setEmailConfirmed(true);
            return true;
        }).orElse(false);
    }
}
