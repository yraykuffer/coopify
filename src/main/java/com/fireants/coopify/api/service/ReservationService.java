/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.service;

import com.fireants.coopify.api.model.Reservation;
import com.fireants.coopify.api.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author admin
 */
@Service
public class ReservationService {
    @Autowired
    private ReservationRepository repo;
    
    /**
     * Gets All reservations
     * @return reservations
     */
    public Flux<Reservation> findAll() {
        return this.repo.findAll();
    }
    
    /**
     * Gets reservation By Id
     * @param id
     * @return reservation
     */
    public Mono<Reservation> findById(String id) {
        return this.repo.findById(id);
    }
    
    /**
     * Gets Reservations by memberCoopId specified
     * @param memberCoopId
     * @return Reservations
     */
    public Flux<Reservation> findByMemberCoopId(String memberCoopId) {
        return this.repo.findByMemberCoopId(memberCoopId);
    }
    
    /**
     * Adds Reservation
     * @param r
     * @return Reservation
     */
    public Mono<Reservation> save(Reservation r) {
        return this.repo.save(r);
    }
    
    /**
     * Updates existing Reservation
     * @param id
     * @param r
     * @return updated Reservation
     */
    public Mono<Reservation> update(String id, Reservation r) {
        r.setId(id);
        return this.repo.save(r);
    }
    
    /**
     * Deletes Reservation by id
     * @param id
     * @return 
     */
    public Mono<Void> deleteById(String id) {
        return this.repo.deleteById(id);
    }
    
    /**
     * Deletes all Reservations
     * @return 
     */
    public Mono<Void> deleteAll() {
        return this.repo.deleteAll();
    }
}
