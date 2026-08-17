package com.tecverse.app.dto;

import org.hibernate.validator.constraints.URL;

import com.tecverse.app.util.Constants;
import com.tecverse.app.validator.Gstin;
import com.tecverse.app.validator.IndianMobile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Payload accepted by {@code POST /register}. Field names match the {@code name} attributes
 * of the exhibitor-registration form exactly, so it binds directly from the submitted JSON.
 */
public class RegistrationRequest {

    // ---------------- Step 1: Company Information ----------------

    @NotBlank(message = "{validation.companyName.required}")
    @Size(min = 3, max = 200, message = "{validation.companyName.size}")
    private String companyName;

    @URL(message = "{validation.url}")
    @Size(max = 255)
    private String website;

    @NotBlank(message = "{validation.industry.required}")
    @Size(max = 100)
    private String industry;

    @NotBlank(message = "{validation.city.required}")
    @Size(max = 100)
    private String city;

    @NotBlank(message = "{validation.country.required}")
    @Size(max = 100)
    private String country;

    @Gstin
    private String gst;

    @NotBlank(message = "{validation.companySize.required}")
    @Size(max = 50)
    private String companySize;

    // ---------------- Step 2: Contact Information ----------------

    @NotBlank(message = "{validation.fullName.required}")
    @Pattern(regexp = "^[A-Za-z][A-Za-z .'-]{1,149}$", message = "{validation.fullName.pattern}")
    private String fullName;

    @NotBlank(message = "{validation.email.required}")
    @Email(message = "{validation.email}")
    @Size(max = 150)
    private String email;

    @IndianMobile
    private String phone;

    @NotBlank(message = "{validation.designation.required}")
    @Size(max = 100)
    private String designation;

    @URL(message = "{validation.url}")
    @Pattern(regexp = Constants.LINKEDIN_REGEX, message = "{validation.linkedin}")
    @Size(max = 255)
    private String linkedin;

    // ---------------- Step 3: Exhibition Details ----------------

    @NotBlank(message = "{validation.stallPackage.required}")
    @Size(max = 100)
    private String stallPackage;

    @NotBlank(message = "{validation.technologyArea.required}")
    @Size(max = 100)
    private String technologyArea;

    @NotBlank(message = "{validation.powerRequirement.required}")
    @Size(max = 100)
    private String powerRequirement;

    @NotBlank(message = "{validation.products.required}")
    @Size(max = 500)
    private String products;

    @Size(max = 1000, message = "{validation.specialRequirements.size}")
    private String specialRequirements;

    @Size(max = 1000, message = "{validation.remarks.size}")
    private String remarks;

    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = blankToNull(website);
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = blankToNull(gst);
    }

    public String getCompanySize() {
        return companySize;
    }

    public void setCompanySize(String companySize) {
        this.companySize = companySize;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = blankToNull(linkedin);
    }

    public String getStallPackage() {
        return stallPackage;
    }

    public void setStallPackage(String stallPackage) {
        this.stallPackage = stallPackage;
    }

    public String getTechnologyArea() {
        return technologyArea;
    }

    public void setTechnologyArea(String technologyArea) {
        this.technologyArea = technologyArea;
    }

    public String getPowerRequirement() {
        return powerRequirement;
    }

    public void setPowerRequirement(String powerRequirement) {
        this.powerRequirement = powerRequirement;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    public String getSpecialRequirements() {
        return specialRequirements;
    }

    public void setSpecialRequirements(String specialRequirements) {
        this.specialRequirements = blankToNull(specialRequirements);
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = blankToNull(remarks);
    }
}
