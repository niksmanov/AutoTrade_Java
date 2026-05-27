package com.autotrade.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class VehicleDto {
    private UUID id;
    private UUID userId;
    private UserDto user;

    @NotNull
    private Integer makeId;
    private String make;

    @NotNull
    private Integer modelId;
    private String model;

    @NotNull
    private Integer colorId;
    private String color;

    @NotNull
    private Integer typeId;
    private String type;

    @NotNull
    private Integer fuelTypeId;
    private String fuelType;

    @NotNull
    private Integer gearboxTypeId;
    private String gearboxType;

    private int horsePower;
    private BigDecimal price = BigDecimal.ZERO;
    private int cubicCapacity;
    private boolean airbags;
    private boolean abs;
    private boolean esp;
    private boolean centralLocking;
    private boolean airConditioning;
    private boolean autoPilot;
    private LocalDate productionDate = LocalDate.of(1900, 1, 1);
    private String url;
    private String coverImageUrl;
    private List<MultipartFile> uploadImages = new ArrayList<>();
    private List<ImageDto> images = new ArrayList<>();
    private Instant dateCreated;

    public String getDisplayDate() {
        return productionDate == null ? null : productionDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
