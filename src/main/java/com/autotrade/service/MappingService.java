package com.autotrade.service;

import com.autotrade.core.Urls;
import com.autotrade.domain.AppUser;
import com.autotrade.domain.Image;
import com.autotrade.domain.UserRole;
import com.autotrade.domain.Vehicle;
import com.autotrade.domain.VehicleMake;
import com.autotrade.domain.VehicleModel;
import com.autotrade.dto.ImageDto;
import com.autotrade.dto.UserDto;
import com.autotrade.dto.VehicleDto;
import com.autotrade.dto.VehicleMakeDto;
import com.autotrade.dto.VehicleModelDto;
import java.util.Comparator;
import org.springframework.stereotype.Service;

@Service
public class MappingService {
    public UserDto toUserDto(AppUser user) {
        if (user == null) {
            return null;
        }

        var dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setUserName(user.getUsername());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setAddress(user.getAddress());
        dto.setEmailConfirmed(user.isEmailConfirmed());
        dto.setAdmin(user.getRoles().contains(UserRole.ADMIN));
        if (user.getTown() != null) {
            dto.setTownId(user.getTown().getId());
            dto.setTownName(user.getTown().getName());
        }
        return dto;
    }

    public VehicleDto toVehicleDto(Vehicle vehicle) {
        if (vehicle == null) {
            return null;
        }

        var dto = new VehicleDto();
        dto.setId(vehicle.getId());
        dto.setUserId(vehicle.getUser().getId());
        dto.setUser(toUserDto(vehicle.getUser()));
        dto.setMakeId(vehicle.getMake().getId());
        dto.setMake(vehicle.getMake().getName());
        dto.setModelId(vehicle.getModel().getId());
        dto.setModel(vehicle.getModel().getName());
        dto.setColorId(vehicle.getColor().getId());
        dto.setColor(vehicle.getColor().getName());
        dto.setTypeId(vehicle.getType().getId());
        dto.setType(vehicle.getType().getName());
        dto.setFuelTypeId(vehicle.getFuelType().getId());
        dto.setFuelType(vehicle.getFuelType().getName());
        dto.setGearboxTypeId(vehicle.getGearboxType().getId());
        dto.setGearboxType(vehicle.getGearboxType().getName());
        dto.setHorsePower(vehicle.getHorsePower());
        dto.setPrice(vehicle.getPrice());
        dto.setCubicCapacity(vehicle.getCubicCapacity());
        dto.setAirbags(vehicle.isAirbags());
        dto.setAbs(vehicle.isAbs());
        dto.setEsp(vehicle.isEsp());
        dto.setCentralLocking(vehicle.isCentralLocking());
        dto.setAirConditioning(vehicle.isAirConditioning());
        dto.setAutoPilot(vehicle.isAutoPilot());
        dto.setProductionDate(vehicle.getProductionDate());
        dto.setDateCreated(vehicle.getDateCreated());
        dto.setUrl(Urls.vehicle(vehicle.getId()));
        dto.setImages(vehicle.getImages().stream().map(this::toImageDto).toList());
        dto.setCoverImageUrl(vehicle.getImages().stream()
                .min(Comparator.comparing(Image::getId))
                .map(image -> Urls.vehicleImage(vehicle.getId(), image.getData()))
                .orElse(Urls.vehicleImage(vehicle.getId(), null)));
        return dto;
    }

    public ImageDto toImageDto(Image image) {
        var dto = new ImageDto();
        dto.setId(image.getId());
        dto.setName(image.getName());
        dto.setVehicleId(image.getVehicle().getId());
        dto.setUrl(Urls.vehicleImage(image.getVehicle().getId(), image.getData()));
        return dto;
    }

    public VehicleMakeDto toMakeDto(VehicleMake make) {
        var dto = new VehicleMakeDto();
        dto.setId(make.getId());
        dto.setName(make.getName());
        return dto;
    }

    public VehicleModelDto toModelDto(VehicleModel model) {
        var dto = new VehicleModelDto();
        dto.setId(model.getId());
        dto.setName(model.getName());
        dto.setMakeId(model.getMake().getId());
        dto.setVehicleTypeId(model.getVehicleType().getId());
        return dto;
    }
}
