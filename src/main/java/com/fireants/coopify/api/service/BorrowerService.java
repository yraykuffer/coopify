/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.service;

import com.fireants.coopify.api.model.Borrower;
import com.fireants.coopify.api.model.User;
import com.fireants.coopify.api.repository.BorrowerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author admin
 */
@Service
public class BorrowerService {
    @Autowired
    private BorrowerRepository repo;
    
    /**
     * Gets All Borrowers
     * @return all Borrowers
     */
    public Flux<Borrower> findAll() {
        return this.repo.findAll();
    }
    
    /**
     * Get Borrower By Id
     * @param id
     * @return Borrower by id
     */
    public Mono<Borrower> findById(String id) {
        return this.repo.findById(id);
    }
    
    /**
     * Saves new Borrower
     * @param borrower
     * @return Borrower
     */
    public Mono<Borrower> save(Borrower borrower) {
        return this.repo.save(borrower);
    }
    
    /**
     * Updates existing borrower
     * @param borrowerId
     * @param borrower
     * @return updated borrower
     */
    public Mono<Borrower> update(String borrowerId, Borrower borrower) {
        borrower.setId(borrowerId);
        return this.repo.save(borrower);
    }
    
    /**
     * Deletes borrower by id
     * @param id
     * @return Void
     */
    public Mono<Void> deleteById(String id) {
        return this.repo.deleteById(id);
    }
    
    /**
     * Deletes all borrowers
     * @return 
     */
    public Mono<Void> deleteAll() {
        return this.repo.deleteAll();
    }

    public Flux<Borrower> findByAccountId(String accountId) {
        return this.repo.findByAccountId(accountId);
    }
}
