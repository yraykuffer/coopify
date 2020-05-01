/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.repository;

import com.fireants.coopify.api.model.PaymentSchedule;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

/**
 *
 * @author admin
 */
public interface PaymentScheduleRepository extends ReactiveMongoRepository<PaymentSchedule, String>{

    public Flux<PaymentSchedule> findByLoanId(String loanId);
    
}
