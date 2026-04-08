package com.biharigraphic.jilamart.otp.repo;

import com.biharigraphic.jilamart.otp.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<Otp,Long> {

    public Optional<Otp> findByEmail(String email);
    public Boolean existsByEmail(String email);

    public Optional<Otp> findByEmailAndOtp(String email,String otp);

    Boolean existsByEmailAndOtp(String email,String otp);



}
