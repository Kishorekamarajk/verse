package com.tecverse.app.service.impl;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.tecverse.app.dto.TicketOtpSendRequest;
import com.tecverse.app.service.TicketOtpService;
import com.tecverse.app.repository.TicketRegistrationRepository;
import com.tecverse.app.util.ValidationUtil;

@Service
public class TicketOtpServiceImpl implements TicketOtpService {

    private static final Logger log = LoggerFactory.getLogger(TicketOtpServiceImpl.class);
    private static final Duration OTP_VALIDITY = Duration.ofMinutes(5);
    private static final SecureRandom RANDOM = new SecureRandom();

    private final JavaMailSender mailSender;
    private final TicketRegistrationRepository registrationRepository;
    private final Map<String, OtpEntry> otpStore = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> verifiedEmails = new ConcurrentHashMap<>();
    private final String fromAddress;
    private final boolean consoleFallbackEnabled;

    public TicketOtpServiceImpl(JavaMailSender mailSender, TicketRegistrationRepository registrationRepository,
                                @Value("${spring.mail.username:}") String fromAddress,
                                @Value("${tecverse.ticket.otp.console-fallback:false}") boolean consoleFallbackEnabled) {
        this.mailSender = mailSender;
        this.registrationRepository = registrationRepository;
        this.fromAddress = fromAddress;
        this.consoleFallbackEnabled = consoleFallbackEnabled;
    }

    @Override
    public void sendOtp(TicketOtpSendRequest request) {
        String email = normalizeEmail(request.getEmail());
        if (registrationRepository.existsByEmail(email)) throw new IllegalArgumentException("This email is already registered.");
        String normalizedPhone = ValidationUtil.normalizeIndianMobile(request.getPhone());
        if (registrationRepository.existsByPhone(normalizedPhone)) throw new IllegalArgumentException("This phone number is already registered.");
        String otp = String.format("%06d", RANDOM.nextInt(1_000_000));
        otpStore.put(email, new OtpEntry(otp, LocalDateTime.now().plus(OTP_VALIDITY)));

        SimpleMailMessage message = new SimpleMailMessage();
        if (fromAddress != null && !fromAddress.isBlank()) {
            message.setFrom(fromAddress);
        }
        message.setTo(email);
        message.setSubject("TEC-VERSE ticket verification OTP");
        message.setText(buildMessage(request, otp));
        try {
            mailSender.send(message);
        } catch (MailException ex) {
            if (consoleFallbackEnabled) {
                log.warn("OTP email could not be sent to {}. Console fallback is enabled. OTP: {}", email, otp, ex);
                return;
            }
            otpStore.remove(email);
            throw new IllegalStateException("Unable to send OTP email right now. SMTP error: " + rootMessage(ex), ex);
        }
    }

    @Override
    public void verifyOtp(String email, String otp) {
        String normalizedEmail = normalizeEmail(email);
        OtpEntry entry = otpStore.get(normalizedEmail);
        if (entry == null) {
            throw new IllegalArgumentException("Please request a new OTP.");
        }
        if (LocalDateTime.now().isAfter(entry.expiresAt())) {
            otpStore.remove(normalizedEmail);
            throw new IllegalArgumentException("OTP has expired. Please request a new OTP.");
        }
        if (!entry.otp().equals(otp)) {
            throw new IllegalArgumentException("Incorrect OTP. Please try again.");
        }
        otpStore.remove(normalizedEmail);
        verifiedEmails.put(normalizedEmail, LocalDateTime.now().plus(Duration.ofMinutes(30)));
    }

    @Override
    public boolean isEmailVerified(String email) {
        String key=normalizeEmail(email); LocalDateTime until=verifiedEmails.get(key);
        if(until==null) return false;
        if(LocalDateTime.now().isAfter(until)){verifiedEmails.remove(key); return false;}
        return true;
    }

    public void consumeEmailVerification(String email) { verifiedEmails.remove(normalizeEmail(email)); }

    private String buildMessage(TicketOtpSendRequest request, String otp) {
        String name = ValidationUtil.sanitize(request.getFullName());
        String passType = request.getPassType() == null || request.getPassType().isBlank()
                ? "ticket"
                : ValidationUtil.sanitize(request.getPassType());

        return "Hello " + name + ",\n\n"
                + "Your TEC-VERSE 2026 OTP for " + passType + " booking is " + otp + ".\n"
                + "This OTP is valid for 5 minutes.\n\n"
                + "Regards,\nTEC-VERSE Team";
    }

    private static String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }

    private static String rootMessage(Throwable throwable) {
        Throwable root = throwable;
        while (root.getCause() != null) {
            root = root.getCause();
        }
        return root.getMessage() == null ? throwable.getMessage() : root.getMessage();
    }

    private record OtpEntry(String otp, LocalDateTime expiresAt) {
    }
}
