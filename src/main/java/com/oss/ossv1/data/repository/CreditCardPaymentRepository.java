package com.oss.ossv1.data.repository;

import com.oss.ossv1.data.entity.CreditCardPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditCardPaymentRepository extends JpaRepository<CreditCardPayment, Integer> {}
