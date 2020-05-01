/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.repository;

import com.fireants.coopify.api.model.Borrower;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

/**
 *
 * @author admin
 */
public interface BorrowerRepository extends ReactiveMongoRepository<Borrower, String>{

    public Flux<Borrower> findByAccountId(String accountId);
    public Flux<Borrower> findByFirstNameRegex(String name);
    
}
