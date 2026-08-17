package com.tecverse.app.repository;

import com.tecverse.app.entity.TecverseRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TecverseRegistrationRepository extends JpaRepository<TecverseRegistration, Long> {

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
}
