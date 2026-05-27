package com.autotrade.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.Data;

@Data
public class UserDto {
    private UUID id;

    @Email
    @NotBlank
    private String email;

    private String oldPassword;
    private String password;
    private String code;
    private String userName;
    private String phoneNumber;
    private String address;
    private Integer townId;
    private String townName;
    private boolean rememberMe;
    private boolean emailConfirmed;

    @JsonProperty("isAdmin")
    @JsonAlias("admin")
    private boolean admin;
}
