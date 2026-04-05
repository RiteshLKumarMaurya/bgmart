package com.biharigraphic.jilamart.payments.repo;

import com.biharigraphic.jilamart.payments.model.entities.Payment;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);
    Optional<Payment> findByPaymentId(String paymentId);
    Optional<Payment> findByOrder_Id(Long orderId); // DB relation


    @Transactional
    @Modifying
    @Query(value = """
    UPDATE payments
    SET status = 'FAILED'
    WHERE status = 'PENDING'
      AND captured IS NULL
      AND created_at < NOW() - INTERVAL '12 HOURS'
      AND payment_type = 'ONLINE'
""", nativeQuery = true)
    void markStalePaymentsAsFailed();

}