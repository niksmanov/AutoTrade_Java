package com.autotrade.config;

import com.autotrade.domain.AppUser;
import com.autotrade.domain.UserRole;
import com.autotrade.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataSeeder implements ApplicationRunner {
    private final AppUserRepository users;
    private final PasswordEncoder passwordEncoder;
    private final String adminEmail;
    private final String adminUsername;
    private final String adminPassword;

    public DataSeeder(AppUserRepository users,
                      PasswordEncoder passwordEncoder,
                      @Value("${autotrade.seed.admin-email}") String adminEmail,
                      @Value("${autotrade.seed.admin-username}") String adminUsername,
                      @Value("${autotrade.seed.admin-password}") String adminPassword) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.adminEmail = adminEmail;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (users.findByEmailIgnoreCase(adminEmail).isPresent()) {
            return;
        }

        var admin = new AppUser();
        admin.setEmail(adminEmail);
        admin.setUsername(adminUsername);
        admin.setEmailConfirmed(true);
        admin.setPasswordHash(passwordEncoder.encode(adminPassword));
        admin.getRoles().add(UserRole.USER);
        admin.getRoles().add(UserRole.ADMIN);
        users.save(admin);
    }
}
