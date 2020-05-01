/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.service;

import com.fireants.coopify.api.model.Member;
import com.fireants.coopify.api.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author admin
 */
@Service
public class MemberService {
    @Autowired
    private MemberRepository repo;
    
    /**
     * Gets All members
     * @return all Users
     */
    public Flux<Member> findAll() {
        return this.repo.findAll();
    }
    
    /**
     * Get Member By Id
     * @param id
     * @return Member by id
     */
    public Mono<Member> findById(String id) {
        return this.repo.findById(id);
    }
    
    /**
     * Gets Members by account specified
     * @param accountId
     * @return Members by account
     */
    public Flux<Member> findByAccountId(String accountId) {
        return this.repo.findByAccountId(accountId);
    }
    
    /**
     * Saves new Member
     * @param m
     * @return Member
     */
    public Mono<Member> save(Member m) {
        return this.repo.save(m);
    }
    
    /**
     * Updates existing Member
     * @param memberId
     * @param m
     * @return updated account
     */
    public Mono<Member> update(String memberId, Member m) {
        m.setId(memberId);
        return this.repo.save(m);
    }
    
    /**
     * Deletes Member by id
     * @param id
     * @return Void
     */
    public Mono<Void> deleteById(String id) {
        return this.repo.deleteById(id);
    }
    
    /**
     * Deletes all Members
     * @return 
     */
    public Mono<Void> deleteAll() {
        return this.repo.deleteAll();
    }
}
