package com.tecverse.app.controller;

import com.tecverse.app.dto.RegistrationRequest;
import com.tecverse.app.dto.RegistrationResponse;
import com.tecverse.app.response.ApiResponse;
import com.tecverse.app.service.RegistrationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Public REST endpoints backing the exhibitor registration form: submission and the
 * AJAX duplicate-check lookups used while the user is filling in the form.
 */
@Validated
@RestController
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegistrationResponse>> register(@Valid @RequestBody RegistrationRequest request) {
        RegistrationResponse response = registrationService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Registration submitted successfully.", response));
    }

    @GetMapping("/check-email")
    public ResponseEntity<ApiResponse<Boolean>> checkEmail(@RequestParam @Email @NotBlank String email) {
        boolean exists = registrationService.checkEmailExists(email);
        String message = exists ? "This email address is already registered." : "Email address is available.";
        return ResponseEntity.ok(ApiResponse.success(message, exists));
    }

    @GetMapping("/check-phone")
    public ResponseEntity<ApiResponse<Boolean>> checkPhone(@RequestParam @NotBlank String phone) {
        boolean exists = registrationService.checkPhoneExists(phone);
        String message = exists ? "This phone number is already registered." : "Phone number is available.";
        return ResponseEntity.ok(ApiResponse.success(message, exists));
    }
}
