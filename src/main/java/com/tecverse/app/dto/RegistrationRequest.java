package com.tecverse.app.dto;

import com.tecverse.app.util.Constants;
import com.tecverse.app.validator.Gstin;
import com.tecverse.app.validator.IndianMobile;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

/**
 * Payload accepted by {@code POST /register}. Field names match the {@code name} attributes
 * of the exhibitor-registration form exactly, so it binds directly from the submitted JSON.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequest {

    // ---------------- Step 1: Company Information ----------------

    @NotBlank(message = "Company name is required")
    @Size(min = 3, max = 200, message = "Company name must be between 3 and 200 characters")
    private String companyName;

    @URL(message = "Please enter a valid URL starting with http:// or https://")
    @Size(max = 255)
    private String website;

    @NotBlank(message = "Industry is required")
    @Size(max = 100)
    private String industry;

    @NotBlank(message = "City is required")
    @Size(max = 100)
    private String city;

    @NotBlank(message = "Country is required")
    @Size(max = 100)
    private String country;

    @Gstin
    private String gst;

    @NotBlank(message = "Company size is required")
    @Size(max = 50)
    private String companySize;

    // ---------------- Step 2: Contact Information ----------------

    @NotBlank(message = "Full name is required")
    @Pattern(regexp = "^[A-Za-z][A-Za-z .'-]{1,149}$", message = "Full name may only contain letters, spaces, dots, apostrophes and hyphens")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Please enter a valid email address")
    @Size(max = 150)
    private String email;

    @IndianMobile
    private String phone;

    @NotBlank(message = "Designation is required")
    @Size(max = 100)
    private String designation;

    @URL(message = "Please enter a valid URL starting with http:// or https://")
    @Pattern(regexp = Constants.LINKEDIN_REGEX, message = "Please enter a valid LinkedIn profile URL")
    @Size(max = 255)
    private String linkedin;

    // ---------------- Step 3: Exhibition Details ----------------

    @NotBlank(message = "Stall package is required")
    @Size(max = 100)
    private String stallPackage;

    @NotBlank(message = "Technology area is required")
    @Size(max = 100)
    private String technologyArea;

    @NotBlank(message = "Power requirement is required")
    @Size(max = 100)
    private String powerRequirement;

    @NotBlank(message = "Products / Services is required")
    @Size(max = 500)
    private String products;

    @Size(max = 1000, message = "Special requirements must not exceed 1000 characters")
    private String specialRequirements;

    @Size(max = 1000, message = "Remarks must not exceed 1000 characters")
    private String remarks;
}
