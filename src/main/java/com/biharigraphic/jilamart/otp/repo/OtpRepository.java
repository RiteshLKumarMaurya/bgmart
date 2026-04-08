package com.biharigraphic.jilamart.otp.repo;

import com.biharigraphic.jilamart.otp.entity.Otp;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<Otp,Long> {

    public Optional<List<Otp>> findByEmail(String email);
    public Boolean existsByEmail(String email);

    public Optional<Otp> findByEmailAndOtp(String email,String otp);

    Boolean existsByEmailAndOtp(String email,String otp);

    @Modifying
    @Query("DELETE FROM Otp o WHERE o.email = :email")
    void deleteByEmail(@Param("email") String email);
}
