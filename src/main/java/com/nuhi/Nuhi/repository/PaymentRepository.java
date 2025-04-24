package com.nuhi.Nuhi.repository;

import com.nuhi.Nuhi.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment , Long> {


    Optional<Payment> findByTransactionId(String transactionId);
    List<Payment> findByBooking_ClientId(Long clientId);







}
