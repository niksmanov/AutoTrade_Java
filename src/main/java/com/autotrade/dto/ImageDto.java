package com.autotrade.dto;

import java.util.UUID;
import lombok.Data;

@Data
public class ImageDto {
    private Integer id;
    private String name;
    private UUID vehicleId;
    private byte[] data;
    private String url;
}
