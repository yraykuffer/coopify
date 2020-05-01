/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.service;

import com.fireants.coopify.api.model.LoanPayment;
import com.fireants.coopify.api.repository.LoanPaymentRepository;
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
public class LoanPaymentService {
    @Autowired
    private LoanPaymentRepository repo;
    
    /**
     * Gets All payments
     * @return payments
     */
    public Flux<LoanPayment> findAll() {
        return this.repo.findAll();
    }
    
    /**
     * Gets payment By Id
     * @param id
     * @return payment
     */
    public Mono<LoanPayment> findById(String id) {
        return this.repo.findById(id);
    }
    
    /**
     * Gets payments by loanId specified
     * @param loanId
     * @return payments by loan id
     */
    public Flux<LoanPayment> findByLoanId(String loanId) {
        return this.repo.findByLoanId(loanId);
    }
    
    /**
     * Adds loan payment
     * @param payment
     * @return payment
     */
    public Mono<LoanPayment> save(LoanPayment payment) {
        return this.repo.save(payment);
    }
    
    /**
     * Adds multiple payments
     * @param payment
     * @return payment
     */
    public Flux<LoanPayment> saveAll(List<LoanPayment> payments) {
        return this.repo.saveAll(payments);
    }
    
    /**
     * Updates existing payment
     * @param paymentId
     * @param payment
     * @return updated payment
     */
    public Mono<LoanPayment> update(String paymentId, LoanPayment payment) {
        payment.setId(paymentId);
        return this.repo.save(payment);
    }
    
    /**
     * Deletes payment by id
     * @param id
     * @return 
     */
    public Mono<Void> deleteById(String id) {
        return this.repo.deleteById(id);
    }
    
    /**
     * Deletes all payments
     * @return 
     */
    public Mono<Void> deleteAll() {
        return this.repo.deleteAll();
    }
}
