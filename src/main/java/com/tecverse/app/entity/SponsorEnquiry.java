package com.tecverse.app.entity;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
}
