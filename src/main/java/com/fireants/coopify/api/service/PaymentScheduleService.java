/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.service;

import com.fireants.coopify.api.model.LoanPayment;
import com.fireants.coopify.api.model.PaymentSchedule;
import com.fireants.coopify.api.repository.LoanPaymentRepository;
import com.fireants.coopify.api.repository.PaymentScheduleRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author admin
 */
@Service
public class PaymentScheduleService {
    @Autowired
    private PaymentScheduleRepository repo;
    
    /**
     * Gets All PaymentSchedules
     * @return PaymentSchedule
     */
    public Flux<PaymentSchedule> findAll() {
        return this.repo.findAll();
    }
    
    /**
     * Gets PaymentSchedule By Id
     * @param id
     * @return PaymentSchedule
     */
    public Mono<PaymentSchedule> findById(String id) {
        return this.repo.findById(id);
    }
    
    /**
     * Gets PaymentSchedules by loanId specified
     * @param loanId
     * @return payments by loan id
     */
    public Flux<PaymentSchedule> findByLoanId(String loanId) {
        return this.repo.findByLoanId(loanId);
    }
    
    /**
     * Adds PaymentSchedule
     * @param payment
     * @return PaymentSchedule
     */
    public Mono<PaymentSchedule> save(PaymentSchedule payment) {
        return this.repo.save(payment);
    }
    
    /**
     * Adds PaymentSchedule
     * @param payments
     * @return PaymentSchedule
     */
    public Flux<PaymentSchedule> saveAll(List<PaymentSchedule> payments) {
        return this.repo.saveAll(payments);
    }
    
    /**
     * Updates existing PaymentSchedule
     * @param paymentId
     * @param payment
     * @return updated PaymentSchedule
     */
    public Mono<PaymentSchedule> update(String paymentId, PaymentSchedule payment) {
        payment.setId(paymentId);
        return this.repo.save(payment);
    }
    
    /**
     * Deletes PaymentSchedule by id
     * @param id
     * @return 
     */
    public Mono<Void> deleteById(String id) {
        return this.repo.deleteById(id);
    }
    
    /**
     * Deletes all PaymentSchedules
     * @return 
     */
    public Mono<Void> deleteAll() {
        return this.repo.deleteAll();
    }
}
