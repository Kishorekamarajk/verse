package com.tecverse.app.controller;

import com.tecverse.app.dto.*;
import com.tecverse.app.response.ApiResponse;
import com.tecverse.app.service.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
public class RegistrationController {
    private final RegistrationService registrationService;
    private final TicketOtpService ticketOtpService;
    private final TicketRegistrationService ticketRegistrationService;
    private final MessageSource messageSource;

    public RegistrationController(RegistrationService registrationService, TicketOtpService ticketOtpService,
                                  TicketRegistrationService ticketRegistrationService, MessageSource messageSource) {
        this.registrationService=registrationService; this.ticketOtpService=ticketOtpService;
        this.ticketRegistrationService=ticketRegistrationService; this.messageSource=messageSource;
    }

    @PostMapping(value={"/register","/register.html"},consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<RegistrationResponse>> register(@Valid @RequestBody RegistrationRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(message("api.registration.submitted"),registrationService.register(request)));
    }
    @PostMapping(value={"/register","/register.html"},consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<RegistrationResponse>> registerForm(@Valid @ModelAttribute RegistrationRequest request){return register(request);}
    @GetMapping(value={"/check-email","/check-email.html"},produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<Boolean>> checkEmail(@RequestParam @Email @NotBlank String email){
        boolean exists=registrationService.checkEmailExists(email);
        return ResponseEntity.ok(ApiResponse.success(exists?message("api.email.registered"):message("api.email.available"),exists));
    }
    @GetMapping(value={"/check-phone","/check-phone.html"},produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<Boolean>> checkPhone(@RequestParam @NotBlank String phone){
        boolean exists=registrationService.checkPhoneExists(phone);
        return ResponseEntity.ok(ApiResponse.success(exists?message("api.phone.registered"):message("api.phone.available"),exists));
    }

    @PostMapping(value="/ticket/send-otp",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<Void>> sendTicketOtp(@Valid @RequestBody TicketOtpSendRequest request){
        ticketOtpService.sendOtp(request); return ResponseEntity.ok(ApiResponse.success("OTP has been sent to your email."));
    }
    @PostMapping(value="/ticket/verify-otp",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<Void>> verifyTicketOtp(@Valid @RequestBody TicketOtpVerifyRequest request){
        ticketOtpService.verifyOtp(request.getEmail(),request.getOtp()); return ResponseEntity.ok(ApiResponse.success("OTP verified successfully."));
    }
    @PostMapping(value="/ticket/register",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<TicketRegistrationResponse>> ticketRegister(@Valid @RequestBody TicketRegistrationRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Registration completed.",ticketRegistrationService.register(request)));
    }
    private String message(String code){return messageSource.getMessage(code,null,LocaleContextHolder.getLocale());}
}
