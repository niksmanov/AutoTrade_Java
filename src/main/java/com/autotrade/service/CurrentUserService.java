package com.autotrade.service;

import com.autotrade.domain.AppUser;
import com.autotrade.repository.AppUserRepository;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserService {
    private final AppUserRepository users;

    public AppUser requireCurrentUser(Principal principal) {
        if (principal == null) {
            throw new AccessDeniedException("Authentication required.");
        }

        return users.findByEmailIgnoreCase(principal.getName())
                .orElseThrow(() -> new AccessDeniedException("Authenticated user was not found."));
    }
}
