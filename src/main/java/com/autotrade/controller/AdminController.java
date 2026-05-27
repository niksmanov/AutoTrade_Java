package com.autotrade.controller;

import com.autotrade.core.BaseController;
import com.autotrade.core.Messages;
import com.autotrade.dto.CommonDto;
import com.autotrade.core.Response;
import com.autotrade.dto.UserDto;
import com.autotrade.dto.VehicleMakeDto;
import com.autotrade.dto.VehicleModelDto;
import com.autotrade.service.CommonService;
import com.autotrade.service.UserService;
import com.autotrade.service.VehicleService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController extends BaseController
{
    private final UserService users;
    private final VehicleService vehicles;
    private final CommonService commons;

    @PostMapping("/addvehiclemake")
    public Response addVehicleMake(@ModelAttribute VehicleMakeDto model) {
        return added(vehicles.addMake(model));
    }

    @PostMapping("/removevehiclemake")
    public Response removeVehicleMake(@RequestParam Integer id) {
        return deleted(vehicles.removeMake(id));
    }

    @PostMapping("/addvehiclemodel")
    public Response addVehicleModel(@ModelAttribute VehicleModelDto model) {
        return added(vehicles.addModel(model));
    }

    @PostMapping("/removevehiclemodel")
    public Response removeVehicleModel(@RequestParam Integer id) {
        return deleted(vehicles.removeModel(id));
    }

    @PostMapping("/addtown")
    public Response addTown(@ModelAttribute CommonDto model) {
        return added(commons.addTown(model));
    }

    @PostMapping("/removetown")
    public Response removeTown(@RequestParam Integer id) {
        return deleted(commons.removeTown(id));
    }

    @PostMapping("/addcolor")
    public Response addColor(@ModelAttribute CommonDto model) {
        return added(commons.addColor(model));
    }

    @PostMapping("/removecolor")
    public Response removeColor(@RequestParam Integer id) {
        return deleted(commons.removeColor(id));
    }

    @PostMapping("/addvehicletype")
    public Response addVehicleType(@ModelAttribute CommonDto model) {
        return added(commons.addVehicleType(model));
    }

    @PostMapping("/removevehicletype")
    public Response removeVehicleType(@RequestParam Integer id) {
        return deleted(commons.removeVehicleType(id));
    }

    @PostMapping("/addfueltype")
    public Response addFuelType(@ModelAttribute CommonDto model) {
        return added(commons.addFuelType(model));
    }

    @PostMapping("/removefueltype")
    public Response removeFuelType(@RequestParam Integer id) {
        return deleted(commons.removeFuelType(id));
    }

    @PostMapping("/addgearboxtype")
    public Response addGearboxType(@ModelAttribute CommonDto model) {
        return added(commons.addGearboxType(model));
    }

    @PostMapping("/removegearboxtype")
    public Response removeGearboxType(@RequestParam Integer id) {
        return deleted(commons.removeGearboxType(id));
    }

    @PostMapping("/changerole")
    public Response changeRole(@ModelAttribute UserDto model) {
        boolean changed = users.changeRole(model);
        return result(changed, Messages.INFO_ENTITY_EDITED, Messages.ERROR_EDIT_PROBLEM);
    }

    @PostMapping("/removeuser")
    public Response removeUser(@RequestParam UUID id) {
        return deleted(users.removeUser(id));
    }

    @GetMapping("/getusers")
    public Response getUsers(@RequestParam int page,
                             @RequestParam int size,
                             @RequestParam(required = false) String search) {
        return result(true, users.getUsers(page, size, search == null ? null : search.trim()));
    }
}
