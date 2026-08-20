package com.tecverse.app.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tecverse.app.dto.RegistrationRequest;
import com.tecverse.app.dto.RegistrationResponse;
import com.tecverse.app.entity.TecverseRegistration;
import com.tecverse.app.exception.DuplicateEmailException;
import com.tecverse.app.exception.DuplicatePhoneException;
import com.tecverse.app.repository.TecverseRegistrationRepository;
import com.tecverse.app.service.RegistrationService;
import com.tecverse.app.util.ValidationUtil;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private static final Logger log = LoggerFactory.getLogger(RegistrationServiceImpl.class);

    private final TecverseRegistrationRepository registrationRepository;
    private final MessageSource messageSource;

    public RegistrationServiceImpl(TecverseRegistrationRepository registrationRepository, MessageSource messageSource) {
        this.registrationRepository = registrationRepository;
        this.messageSource = messageSource;
    }

    @Override
    @Transactional
    public RegistrationResponse register(RegistrationRequest request) {
        String normalizedEmail = request.getEmail().trim().toLowerCase();
        String normalizedPhone = ValidationUtil.normalizeIndianMobile(request.getPhone());

        if (registrationRepository.existsByEmail(normalizedEmail)) {
            throw new DuplicateEmailException(message("api.exhibitor.email.registered"));
        }
        if (registrationRepository.existsByPhone(normalizedPhone)) {
            throw new DuplicatePhoneException(message("api.exhibitor.phone.registered"));
        }

        TecverseRegistration registration = TecverseRegistration.builder()
                .companyName(ValidationUtil.sanitize(request.getCompanyName()))
                .website(blankToNull(request.getWebsite()))
                .industry(ValidationUtil.sanitize(request.getIndustry()))
                .city(ValidationUtil.sanitize(request.getCity()))
                .country(ValidationUtil.sanitize(request.getCountry()))
                .gstNumber(ValidationUtil.normalizeGst(request.getGst()))
                .companySize(ValidationUtil.sanitize(request.getCompanySize()))
                .fullName(ValidationUtil.sanitize(request.getFullName()))
                .email(normalizedEmail)
                .phone(normalizedPhone)
                .designation(ValidationUtil.sanitize(request.getDesignation()))
                .linkedinProfile(blankToNull(request.getLinkedin()))
                .stallPackage(ValidationUtil.sanitize(request.getStallPackage()))
                .technologyArea(ValidationUtil.sanitize(request.getTechnologyArea()))
                .powerRequirement(ValidationUtil.sanitize(request.getPowerRequirement()))
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

    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private String message(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }
}
