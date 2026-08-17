package com.tecverse.app.controller;

import com.tecverse.app.dto.RegistrationRequest;
import com.tecverse.app.dto.RegistrationResponse;
import com.tecverse.app.response.ApiResponse;
import com.tecverse.app.service.RegistrationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
public class RegistrationController {

    private final RegistrationService registrationService;
    private final MessageSource messageSource;

    public RegistrationController(RegistrationService registrationService, MessageSource messageSource) {
        this.registrationService = registrationService;
        this.messageSource = messageSource;
    }

    @PostMapping(
            value = {"/register", "/register.html"},
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ApiResponse<RegistrationResponse>> register(@Valid @RequestBody RegistrationRequest request) {
        RegistrationResponse response = registrationService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(message("api.registration.submitted"), response));
    }

    @PostMapping(
            value = {"/register", "/register.html"},
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ApiResponse<RegistrationResponse>> registerForm(@Valid @ModelAttribute RegistrationRequest request) {
        return register(request);
    }

    @GetMapping(value = {"/check-email", "/check-email.html"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<Boolean>> checkEmail(@RequestParam @Email @NotBlank String email) {
        boolean exists = registrationService.checkEmailExists(email);
        String message = exists ? message("api.email.registered") : message("api.email.available");
        return ResponseEntity.ok(ApiResponse.success(message, exists));
    }

    @GetMapping(value = {"/check-phone", "/check-phone.html"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<Boolean>> checkPhone(@RequestParam @NotBlank String phone) {
        boolean exists = registrationService.checkPhoneExists(phone);
        String message = exists ? message("api.phone.registered") : message("api.phone.available");
        return ResponseEntity.ok(ApiResponse.success(message, exists));
    }

    private String message(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }
}
