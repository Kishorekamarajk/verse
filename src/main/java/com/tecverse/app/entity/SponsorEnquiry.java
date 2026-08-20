package com.tecverse.app.entity;

import java.time.LocalDateTime;

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

/**
 * A message submitted through the public "Become a Sponsor" enquiry form.
 * Maps 1:1 to the pre-existing {@code sponsor_enquiries} table.
 */
@Entity
@Table(
        name = "sponsor_enquiries",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_sponsor_enquiries_email", columnNames = "email"),
                @UniqueConstraint(name = "uk_sponsor_enquiries_phone", columnNames = "phone")
        },
        indexes = @Index(name = "idx_sponsor_enquiries_created_at", columnList = "created_at")
)
public class SponsorEnquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company", nullable = false, length = 200)
    private String company;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "designation", length = 150)
    private String designation;

    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @Column(name = "sponsorship_interest", nullable = false, length = 100)
    private String sponsorshipInterest;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

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

    public SponsorEnquiry() {
    }

    public SponsorEnquiry(Long id, String company, String name, String designation, String email,
                          String phone, String sponsorshipInterest, String message,
                          LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.company = company;
        this.name = name;
        this.designation = designation;
        this.email = email;
        this.phone = phone;
        this.sponsorshipInterest = sponsorshipInterest;
        this.message = message;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static SponsorEnquiryBuilder builder() {
        return new SponsorEnquiryBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
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

    public String getSponsorshipInterest() {
        return sponsorshipInterest;
    }

    public void setSponsorshipInterest(String sponsorshipInterest) {
        this.sponsorshipInterest = sponsorshipInterest;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static class SponsorEnquiryBuilder {
        private Long id;
        private String company;
        private String name;
        private String designation;
        private String email;
        private String phone;
        private String sponsorshipInterest;
        private String message;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public SponsorEnquiryBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public SponsorEnquiryBuilder company(String company) {
            this.company = company;
            return this;
        }

        public SponsorEnquiryBuilder name(String name) {
            this.name = name;
            return this;
        }

        public SponsorEnquiryBuilder designation(String designation) {
            this.designation = designation;
            return this;
        }

        public SponsorEnquiryBuilder email(String email) {
            this.email = email;
            return this;
        }

        public SponsorEnquiryBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public SponsorEnquiryBuilder sponsorshipInterest(String sponsorshipInterest) {
            this.sponsorshipInterest = sponsorshipInterest;
            return this;
        }

        public SponsorEnquiryBuilder message(String message) {
            this.message = message;
            return this;
        }

        public SponsorEnquiryBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public SponsorEnquiryBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public SponsorEnquiry build() {
            return new SponsorEnquiry(id, company, name, designation, email, phone,
                    sponsorshipInterest, message, createdAt, updatedAt);
        }
    }
}
