package com.autotrade.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
public class SearchVehiclesDto {
    private int page;
    private int size;
    private Integer townId;
    private Integer makeId;
    private Integer modelId;
    private Integer colorId;
    private Integer typeId;
    private Integer fuelTypeId;
    private Integer gearboxTypeId;
    private Boolean airbags;
    private Boolean abs;
    private Boolean esp;
    private Boolean centralLocking;
    private Boolean airConditioning;
    private Boolean autoPilot;
    private Integer fromCubicCapacity;
    private Integer toCubicCapacity;
    private Integer fromHorsePower;
    private Integer toHorsePower;
    private BigDecimal fromPrice;
    private BigDecimal toPrice;
    private LocalDate fromProductionDate;
    private LocalDate toProductionDate;
}
