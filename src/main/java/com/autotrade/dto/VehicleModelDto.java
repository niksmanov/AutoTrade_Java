package com.autotrade.dto;

import lombok.Data;

@Data
public class VehicleModelDto {
    private Integer id;
    private String name;
    private Integer makeId;
    private Integer vehicleTypeId;
}
