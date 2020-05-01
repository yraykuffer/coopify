/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.repository;

import com.fireants.coopify.api.model.CoopMember;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author admin
 */
@Repository
public interface CoopMemberRepository extends ReactiveMongoRepository<CoopMember, String>{

    public Flux<CoopMember> findByCoopId(String coopId);
    public Mono<CoopMember> findByCoopIdAndId(String coopId, String coopMemberId);
    
    @DeleteQuery
    public Flux<CoopMember> deleteAllByCoopId(String coopId);
}
