package com.oss.ossv1.data.repository;

import com.oss.ossv1.data.entity.PayPalPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayPalPaymentRepository extends JpaRepository<PayPalPayment, Integer> {}
