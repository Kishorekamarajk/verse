package com.tecverse.app.entity;

import com.tecverse.app.validator.Gstin;
import com.tecverse.app.validator.IndianMobile;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

/**
 * An exhibitor's registration for TecVerse. Maps 1:1 to {@code tecverse_registrations}.
 */
@Entity
@Table(
        name = "tecverse_registrations",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_tecverse_registrations_email", columnNames = "email"),
                @UniqueConstraint(name = "uk_tecverse_registrations_phone", columnNames = "phone")
        },
        indexes = {
                @Index(name = "idx_tecverse_registrations_created_at", columnList = "created_at"),
                @Index(name = "idx_tecverse_registrations_company_name", columnList = "company_name")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TecverseRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ---------------- Company Information ----------------

    @NotBlank
    @Size(min = 3, max = 200)
    @Column(name = "company_name", nullable = false, length = 200)
    private String companyName;

    @URL
    @Size(max = 255)
    @Column(name = "website", length = 255)
    private String website;

    @NotBlank
    @Size(max = 100)
    @Column(name = "industry", nullable = false, length = 100)
    private String industry;

    @NotBlank
    @Size(max = 100)
    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @NotBlank
    @Size(max = 100)
    @Column(name = "country", nullable = false, length = 100)
    private String country;

    @Gstin
    @Column(name = "gst_number", length = 15)
    private String gstNumber;

    @NotBlank
    @Size(max = 50)
    @Column(name = "company_size", nullable = false, length = 50)
    private String companySize;

    // ---------------- Contact Information ----------------

    @NotBlank
    @Size(max = 150)
    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @NotBlank
    @Email
    @Size(max = 150)
    @Column(name = "email", nullable = false, length = 150)
    private String email;

    @NotBlank
    @IndianMobile
    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @NotBlank
    @Size(max = 100)
    @Column(name = "designation", nullable = false, length = 100)
    private String designation;

    @URL
    @Size(max = 255)
    @Column(name = "linkedin_profile", length = 255)
    private String linkedinProfile;

    // ---------------- Exhibition Details ----------------

    @NotBlank
    @Size(max = 100)
    @Column(name = "stall_package", nullable = false, length = 100)
    private String stallPackage;

    @NotBlank
    @Size(max = 100)
    @Column(name = "technology_area", nullable = false, length = 100)
    private String technologyArea;

    @NotBlank
    @Size(max = 100)
    @Column(name = "power_requirement", nullable = false, length = 100)
    private String powerRequirement;

    @NotBlank
    @Size(max = 500)
    @Column(name = "products_services", nullable = false, length = 500)
    private String productsServices;

    @Size(max = 1000)
    @Column(name = "special_requirements", columnDefinition = "TEXT")
    private String specialRequirements;

    @Size(max = 1000)
    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    // ---------------- Audit ----------------

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
