package com.autotrade.controller;

import com.autotrade.core.BaseController;
import com.autotrade.core.Response;
import com.autotrade.service.CommonService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/common")
@RequiredArgsConstructor
public class CommonController extends BaseController
{
    private final CommonService commons;

    @GetMapping("/gettowns")
    public Response getTowns() {
        return result(true, commons.getTowns());
    }

    @GetMapping("/getcolors")
    public Response getColors() {
        return result(true, commons.getColors());
    }

    @GetMapping("/getvehicletypes")
    public Response getVehicleTypes() {
        return result(true, commons.getVehicleTypes());
    }

    @GetMapping("/getfueltypes")
    public Response getFuelTypes() {
        return result(true, commons.getFuelTypes());
    }

    @GetMapping("/getgearboxtypes")
    public Response getGearboxTypes() {
        return result(true, commons.getGearboxTypes());
    }

    @GetMapping("/getallcommons")
    public Response getAllCommons() {
        return result(true, commons.getAllCommons());
    }

    @GetMapping("/getimages")
    public Response getImages(@RequestParam UUID vehicleId) {
        return result(true, commons.getImages(vehicleId));
    }
}
