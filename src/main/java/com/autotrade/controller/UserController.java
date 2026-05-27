package com.autotrade.controller;

import com.autotrade.core.BaseController;
import com.autotrade.core.Messages;
import com.autotrade.core.Response;
import com.autotrade.dto.UserDto;
import com.autotrade.service.MailService;
import com.autotrade.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController extends BaseController
{
    private final UserService users;
    private final MailService mail;
    private final AuthenticationManager authenticationManager;

    @GetMapping("/current")
    public Response current(Principal principal) {
        if (principal == null) {
            return result();
        }
        return result(true, users.getByEmail(principal.getName()));
    }

    @PostMapping("/register")
    public Response register(@Valid @ModelAttribute UserDto model, HttpServletRequest request) {
        if (users.exists(model.getEmail())) {
            return error(Messages.ERROR_ENTITY_EXISTS);
        }

        var user = users.register(model);
        mail.send(user.getEmail(), "Confirm your email", "Please confirm your email by opening /user/confirmemail?id=" + user.getId());
        authenticate(model.getEmail(), model.getPassword(), request);
        return result(true);
    }

    @GetMapping("/resendconfirmationemail")
    public Response resendConfirmationEmail(@RequestParam UUID id) {
        var user = users.getById(id);
        if (user != null) {
            mail.send(user.getEmail(), "Confirm your email", "Please confirm your email by opening /user/confirmemail?id=" + id);
            return error(Messages.INFO_EMAIL_SENT);
        }
        return result();
    }

    @PostMapping("/login")
    public Response login(@ModelAttribute UserDto model, HttpServletRequest request) {
        if (!users.exists(model.getEmail())) {
            return error(Messages.ERROR_INVALID_EMAIL_OR_PASSWORD);
        }

        try {
            authenticate(model.getEmail(), model.getPassword(), request);
            return result(true);
        } catch (RuntimeException ex) {
            return error(Messages.ERROR_INVALID_EMAIL_OR_PASSWORD);
        }
    }

    @GetMapping("/logout")
    public Response logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        new SecurityContextLogoutHandler().logout(request, response, authentication);
        return result(true);
    }

    @PostMapping("/forgotpassword")
    public Response forgotPassword(@Valid @ModelAttribute UserDto model) {
        String newPassword = UUID.randomUUID().toString().split("-")[0];
        if (users.resetPassword(model.getEmail(), newPassword)) {
            mail.send(model.getEmail(), "New Password", "Your new password is: " + newPassword);
            return error(Messages.INFO_EMAIL_SENT);
        }
        return error(Messages.ERROR_INVALID_EMAIL);
    }

    @GetMapping({"/confirmemail", "/confirmemail/"})
    public void confirmEmail(@RequestParam(required = false) UUID id, HttpServletResponse response) throws Exception {
        if (id != null) {
            users.confirmEmail(id);
        }
        response.sendRedirect("/");
    }

    @PostMapping("/resetpassword")
    public Response resetPassword(@ModelAttribute UserDto model, Principal principal) {
        var current = users.getByEmail(principal.getName());
        if (current == null || !current.getEmail().equalsIgnoreCase(model.getEmail())) {
            return error(Messages.ERROR_INVALID_EMAIL);
        }

        boolean changed = users.resetPasswordForCurrentUser(current.getId(), model.getOldPassword(), model.getPassword());
        return error(changed ? Messages.INFO_PASSWORD_CHANGED : Messages.ERROR_INVALID_PASSWORD);
    }

    private void authenticate(String email, String password, HttpServletRequest request) {
        var token = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManager.authenticate(token);
        var context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        request.getSession(true).setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
    }
}
