/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.repository;

import com.fireants.coopify.api.model.Loan;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author admin
 */
@Repository
public interface LoanRepository extends ReactiveMongoRepository<Loan, String>{

    public Flux<Loan> findByCoopIdAndCoopMemberId(String coopId, String memberId);

    public Flux<Loan> findByLoanedBy(String loanedBy);

    public Mono<Loan>  findByCoopIdAndCoopMemberIdAndId(String coopId, String memberId, String loanId);

    public Flux<Loan> findByCoopId(String coopId);
    
     public Mono<Loan> findByIdAndCoopId(String id, String coopId);
    
}
