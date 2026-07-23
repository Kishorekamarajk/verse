package com.tecverse.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * What the client receives back after a successful {@code POST /register}.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationResponse {

    private Long id;
    private String companyName;
    private String email;
    private String phone;
    private String stallPackage;
    private LocalDateTime createdAt;
}
