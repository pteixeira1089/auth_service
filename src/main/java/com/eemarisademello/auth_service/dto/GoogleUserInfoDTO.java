package com.eemarisademello.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GoogleUserInfoDTO {
    private final String name;
    private final String email;
}
