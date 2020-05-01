/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.repository;

import com.fireants.coopify.api.model.Contribution;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 *
 * @author admin
 */
@Repository
public interface ContributionRepository extends ReactiveMongoRepository<Contribution, String>{

    public Flux<Contribution> findByCoopMemberId(String memberId);

    public Flux<Contribution> findByCoopId(String coopId);
    
}
