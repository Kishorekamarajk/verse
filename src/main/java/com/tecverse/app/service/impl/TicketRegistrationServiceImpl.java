package com.tecverse.app.service.impl;

import com.tecverse.app.dto.TicketRegistrationRequest;
import com.tecverse.app.dto.TicketRegistrationResponse;
import com.tecverse.app.entity.TicketRegistration;
import com.tecverse.app.repository.TicketRegistrationRepository;
import com.tecverse.app.service.TicketRegistrationService;
import com.tecverse.app.service.TicketOtpService;
import com.tecverse.app.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.mail.MessagingException;
import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketRegistrationServiceImpl implements TicketRegistrationService {
    private static final SecureRandom RANDOM = new SecureRandom();
    private final TicketRegistrationRepository repo;
    private final JavaMailSender mailSender;
    private final TicketOtpService otpService;
    private final String fromAddress;

    public TicketRegistrationServiceImpl(TicketRegistrationRepository repo, JavaMailSender mailSender, TicketOtpService otpService,
                                         @Value("${spring.mail.username:}") String fromAddress) {
        this.repo=repo; this.mailSender=mailSender; this.otpService=otpService; this.fromAddress=fromAddress;
    }

    @Transactional
    public TicketRegistrationResponse register(TicketRegistrationRequest r) {
        String email=normEmail(r.getEmail()), phone=ValidationUtil.normalizeIndianMobile(r.getPhone());
        if(!otpService.isEmailVerified(email)) throw new IllegalArgumentException("Please verify your email with OTP before submitting the registration.");
        if(!repo.existsByEmail(email) && !repo.existsByPhone(phone)) {
            String ref=generateReference();
            TicketRegistration e=new TicketRegistration();
            e.setReferenceNumber(ref); e.setOfficialName(clean(r.getOfficialName())); e.setEmail(email); e.setPhone(phone);
            e.setCategory(clean(r.getCategory())); e.setAcademiaType(clean(r.getAcademiaType()));
            e.setCollegeName(clean(r.getCollegeName())); e.setCollegeDistrict(clean(r.getCollegeDistrict())); e.setCollegeState(clean(r.getCollegeState()));
            e.setUniversityName(clean(r.getUniversityName())); e.setAcademiaRole(clean(r.getAcademiaRole())); e.setRegisterNumber(clean(r.getRegisterNumber()));
            e.setCentralMinistry(clean(r.getCentralMinistry())); e.setStateName(clean(r.getStateName())); e.setStateDepartment(clean(r.getStateDepartment()));
            e.setOrganizationName(clean(r.getOrganizationName())); e.setOrganizationLocation(clean(r.getOrganizationLocation()));
            e.setDesignation(clean(r.getDesignation())); e.setIndustryOrStartup(clean(r.getIndustryOrStartup()));
            e.setCitizenshipStatus(clean(r.getCitizenshipStatus())); e.setPassportNumber(clean(r.getPassportNumber()));
            e.setPassportValidUntil(clean(r.getPassportValidUntil())); e.setPassportName(clean(r.getPassportName()));
            e.setAttendanceDays(r.getAttendanceDays()==null?"":r.getAttendanceDays().stream().map(this::clean).collect(Collectors.joining(",")));
            repo.saveAndFlush(e);
            sendConfirmation(e);
            if (otpService instanceof com.tecverse.app.service.impl.TicketOtpServiceImpl impl) impl.consumeEmailVerification(email);
            return new TicketRegistrationResponse(ref,email,"Registration completed successfully.");
        }
        if(repo.existsByEmail(email)) throw new IllegalArgumentException("This email is already registered.");
        throw new IllegalArgumentException("This phone number is already registered.");
    }

    private String generateReference(){
        for(int i=0;i<20;i++){
            String ref=String.format("%012d",Math.abs(RANDOM.nextLong()%1_000_000_000_000L));
            if(!repo.existsByReferenceNumber(ref)) return ref;
        }
        throw new IllegalStateException("Unable to generate reference number.");
    }

    private void sendConfirmation(TicketRegistration e){
        try {
            var msg=mailSender.createMimeMessage();
            var helper=new MimeMessageHelper(msg,true,"UTF-8");
            if(fromAddress!=null&&!fromAddress.isBlank()) helper.setFrom(fromAddress);
            helper.setTo(e.getEmail());
            helper.setSubject("TEC-VERSE 2026 Registration Confirmation - "+e.getReferenceNumber());
            helper.setText("Hello "+e.getOfficialName()+",\n\nYour TEC-VERSE 2026 registration is confirmed.\n\n12-digit Reference Number: "+e.getReferenceNumber()+
                    "\nAttendance: "+e.getAttendanceDays().replace(", ",", ")+"\n\nPlease keep this reference number for entry/help-desk communication. The Event Timeline & Visitor Guidance PDF is attached.\n\nRegards,\nTEC-VERSE 2026 Team");
            ClassPathResource pdf=new ClassPathResource("static/docs/tecverse-event-timeline.pdf");
            if(pdf.exists()) helper.addAttachment("TEC-VERSE-2026-Event-Timeline-and-Visitor-Guidance.pdf",pdf);
            mailSender.send(msg);
        } catch(MessagingException ex) {
            throw new IllegalStateException("Registration was saved, but confirmation email could not be sent. Contact the administrator.",ex);
        }
    }
    private String normEmail(String v){return v==null?"":v.trim().toLowerCase();}
    private String clean(String v){return v==null||v.isBlank()?null:ValidationUtil.sanitize(v.trim());}
}
