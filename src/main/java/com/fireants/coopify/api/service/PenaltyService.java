/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.service;

import com.fireants.coopify.api.model.Penalty;
import com.fireants.coopify.api.repository.PenaltyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author admin
 */
@Service
public class PenaltyService {
    @Autowired
    private PenaltyRepository repo;
    
    /**
     * Gets All penalties
     * @return penalties
     */
    public Flux<Penalty> findAll() {
        return this.repo.findAll();
    }
    
    /**
     * Gets Penalty By Id
     * @param id
     * @return Penalty
     */
    public Mono<Penalty> findById(String id) {
        return this.repo.findById(id);
    }
    
    /**
     * Gets Penalties by memerId id specified
     * @param memberId
     * @return Penalties by loan id
     */
    public Flux<Penalty> findByCoopMemberId(String memberId) {
        return this.repo.findByCoopMemberId(memberId);
    }
    
    /**
     * Adds penalty
     * @param penalty
     * @return penalty
     */
    public Mono<Penalty> save(Penalty penalty) {
        return this.repo.save(penalty);
    }
    
    /**
     * Updates existing penalty
     * @param id
     * @param penalty
     * @return updated penalty
     */
    public Mono<Penalty> update(String id, Penalty penalty) {
        penalty.setId(id);
        return this.repo.save(penalty);
    }
    
    /**
     * Deletes penalty by id
     * @param id
     * @return 
     */
    public Mono<Void> deleteById(String id) {
        return this.repo.deleteById(id);
    }
    
    /**
     * Deletes all penalties
     * @return 
     */
    public Mono<Void> deleteAll() {
        return this.repo.deleteAll();
    }
}
