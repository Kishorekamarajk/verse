package com.tecverse.app.service.impl;

import com.tecverse.app.dto.RegistrationRequest;
import com.tecverse.app.dto.RegistrationResponse;
import com.tecverse.app.entity.TecverseRegistration;
import com.tecverse.app.exception.DuplicateEmailException;
import com.tecverse.app.exception.DuplicatePhoneException;
import com.tecverse.app.repository.TecverseRegistrationRepository;
import com.tecverse.app.service.RegistrationService;
import com.tecverse.app.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final TecverseRegistrationRepository registrationRepository;

    @Override
    @Transactional
    public RegistrationResponse register(RegistrationRequest request) {
        String normalizedEmail = request.getEmail().trim().toLowerCase();
        String normalizedPhone = ValidationUtil.normalizeIndianMobile(request.getPhone());

        if (registrationRepository.existsByEmail(normalizedEmail)) {
            throw new DuplicateEmailException("An exhibitor is already registered with this email address.");
        }
        if (registrationRepository.existsByPhone(normalizedPhone)) {
            throw new DuplicatePhoneException("An exhibitor is already registered with this phone number.");
        }

        TecverseRegistration registration = TecverseRegistration.builder()
                .companyName(ValidationUtil.sanitize(request.getCompanyName()))
                .website(request.getWebsite())
                .industry(request.getIndustry())
                .city(request.getCity())
                .country(request.getCountry())
                .gstNumber(ValidationUtil.normalizeGst(request.getGst()))
                .companySize(request.getCompanySize())
                .fullName(ValidationUtil.sanitize(request.getFullName()))
                .email(normalizedEmail)
                .phone(normalizedPhone)
                .designation(request.getDesignation())
                .linkedinProfile(request.getLinkedin())
                .stallPackage(request.getStallPackage())
                .technologyArea(request.getTechnologyArea())
                .powerRequirement(request.getPowerRequirement())
                .productsServices(ValidationUtil.sanitize(request.getProducts()))
                .specialRequirements(ValidationUtil.sanitize(request.getSpecialRequirements()))
                .remarks(ValidationUtil.sanitize(request.getRemarks()))
                .build();

        TecverseRegistration saved = registrationRepository.save(registration);
        log.info("New exhibitor registration saved: id={}, company={}", saved.getId(), saved.getCompanyName());

        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkEmailExists(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        return registrationRepository.existsByEmail(email.trim().toLowerCase());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkPhoneExists(String phone) {
        if (phone == null || phone.isBlank()) {
            return false;
        }
        return registrationRepository.existsByPhone(ValidationUtil.normalizeIndianMobile(phone));
    }

    private RegistrationResponse toResponse(TecverseRegistration entity) {
        return RegistrationResponse.builder()
                .id(entity.getId())
                .companyName(entity.getCompanyName())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .stallPackage(entity.getStallPackage())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
