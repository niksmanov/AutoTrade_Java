package com.autotrade.dto;

import java.util.List;
import lombok.Data;

@Data
public class AllCommonsDto {
    private List<CommonDto> colors;
    private List<CommonDto> towns;
    private List<CommonDto> vehicleTypes;
    private List<CommonDto> fuelTypes;
    private List<CommonDto> gearboxTypes;
}
