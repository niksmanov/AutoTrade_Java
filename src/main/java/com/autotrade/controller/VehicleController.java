package com.autotrade.controller;

import com.autotrade.core.BaseController;
import com.autotrade.core.Response;
import com.autotrade.dto.SearchVehiclesDto;
import com.autotrade.dto.VehicleDto;
import com.autotrade.service.VehicleService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vehicle")
@RequiredArgsConstructor
public class VehicleController extends BaseController
{
    private final VehicleService vehicles;

    @GetMapping("/getvehiclemakes")
    public Response getVehicleMakes()
    {
        return result(true, vehicles.getMakes());
    }

    @GetMapping("/getvehiclemodels")
    public Response getVehicleModels(@RequestParam Integer makeId, @RequestParam(defaultValue = "0") Integer vehicleTypeId)
    {
        if (makeId != null && makeId > 0)
        {
            return result(true, vehicles.getModels(makeId, vehicleTypeId));
        }
        return result();
    }

    @GetMapping("/getvehicle")
    public Response getVehicle(@RequestParam(required = false) UUID id)
    {
        VehicleDto vehicle = id == null ? new VehicleDto() : vehicles.getVehicle(id);
        return result(true, vehicle);
    }

    @GetMapping("/getvehicles")
    public Response getVehicles(@RequestParam int page,
                                @RequestParam int size,
                                @RequestParam(required = false) UUID userId)
    {
        return result(true, vehicles.getVehicles(page, size, userId, null));
    }

    @PostMapping("/searchvehicles")
    public Response searchVehicles(@RequestBody SearchVehiclesDto search)
    {
        return result(true, vehicles.getVehicles(search.getPage(), search.getSize(), null, search));
    }
}
