package com.tecverse.app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class TicketOtpVerifyRequest {

    @NotBlank(message = "{validation.email.required}")
    @Email(message = "{validation.email}")
    private String email;

    @NotBlank
    @Pattern(regexp = "\\d{6}", message = "Please enter the 6-digit OTP")
    private String otp;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
