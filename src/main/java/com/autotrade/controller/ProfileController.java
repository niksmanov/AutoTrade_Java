package com.autotrade.controller;

import com.autotrade.core.BaseController;
import com.autotrade.core.Messages;
import com.autotrade.domain.UserRole;
import com.autotrade.core.Response;
import com.autotrade.dto.UserDto;
import com.autotrade.dto.VehicleDto;
import com.autotrade.service.CurrentUserService;
import com.autotrade.service.UserService;
import com.autotrade.service.VehicleService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class ProfileController extends BaseController
{
    private final UserService users;
    private final VehicleService vehicles;
    private final CurrentUserService currentUsers;

    @PostMapping("/editinfo")
    public Response editInfo(@ModelAttribute UserDto model, Principal principal) {
        var current = currentUsers.requireCurrentUser(principal);
        boolean edited = model.getId() != null && model.getId().equals(current.getId()) && users.editUser(model);
        return result(edited, Messages.INFO_ENTITY_EDITED, Messages.ERROR_EDIT_PROBLEM);
    }

    @PostMapping("/addvehicle")
    public Response addVehicle(@Valid @ModelAttribute VehicleDto model, Principal principal) {
        var current = currentUsers.requireCurrentUser(principal);
        UUID id = vehicles.addVehicle(model, current);
        return result(true, id, Messages.INFO_ENTITY_ADDED);
    }

    @PostMapping("/editvehicle")
    public Response editVehicle(@Valid @ModelAttribute VehicleDto model, Principal principal) {
        var current = currentUsers.requireCurrentUser(principal);
        var existing = vehicles.getVehicle(model.getId());
        boolean isAdmin = current.getRoles().contains(UserRole.ADMIN);

        if (existing != null && (existing.getUserId().equals(current.getId()) || isAdmin)) {
            UUID id = vehicles.editVehicle(model);
            return result(true, id, Messages.INFO_ENTITY_EDITED);
        }

        return error(Messages.ERROR_EDIT_PROBLEM);
    }

    @PostMapping("/removevehicle")
    public Response removeVehicle(@RequestParam UUID id, Principal principal) {
        var current = currentUsers.requireCurrentUser(principal);
        var existing = vehicles.getVehicle(id);
        boolean isAdmin = current.getRoles().contains(UserRole.ADMIN);
        boolean deleted = existing != null && (existing.getUserId().equals(current.getId()) || isAdmin) && vehicles.removeVehicle(id);
        return result(deleted, Messages.INFO_ENTITY_DELETED, Messages.ERROR_DELETE_PROBLEM);
    }
}
