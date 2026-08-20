package com.tecverse.app.repository;
import com.tecverse.app.entity.TicketRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TicketRegistrationRepository extends JpaRepository<TicketRegistration,Long>{
 boolean existsByEmail(String email);
 boolean existsByPhone(String phone);
 boolean existsByReferenceNumber(String referenceNumber);
}
