/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.repository;

import com.fireants.coopify.api.model.Reservation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

/**
 *
 * @author admin
 */
public interface ReservationRepository extends ReactiveMongoRepository<Reservation, String> {

    public Flux<Reservation> findByMemberCoopId(String memberCoopId);

}
