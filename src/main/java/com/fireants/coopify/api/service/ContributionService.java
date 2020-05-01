/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.service;

import com.fireants.coopify.api.model.Contribution;
import com.fireants.coopify.api.model.CoopMember;
import com.fireants.coopify.api.model.Loan;
import com.fireants.coopify.api.repository.ContributionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author admin
 */
@Service
public class ContributionService {
    @Autowired
    private ContributionRepository contriRepo;
    
    @Autowired
    private CoopMemberService coopMemberService;
    
    /**
     * Gets All Contributions
     * @return all Contributions
     */
    public Flux<Contribution> findAll() {
        return this.contriRepo.findAll();
    }
    
    /**
     * Get Contribution By Id
     * @param id
     * @return Contribution
     */
    public Mono<Contribution> findById(String id) {
        return this.contriRepo.findById(id);
    }
    
    public Flux<Contribution> findByCoopMemberId(String memberId) {
        return this.contriRepo.findByCoopMemberId(memberId);
    }
    
    /**
     * Saves new Contribution
     * @param contri
     * @return Contribution
     */
    public Mono<Contribution> save(Contribution contri) {
        return this.contriRepo.save(contri)
                .flatMap(c -> this.updateCoopMember(c).then(Mono.just(c)));
    }
    
    /**
     * Updates existing Contribution
     * @param id
     * @param contri
     * @return updated Contribution
     */
    public Mono<Contribution> update(String id, Contribution contri) {
        contri.setId(id);
        return this.contriRepo.save(contri);
    }
    
    /**
     * Deletes contribution by id
     * @param id
     * @return Void
     */
    public Mono<Void> deleteById(String id) {
        return this.contriRepo.deleteById(id);
    }
    
    /**
     * Deletes all contributions
     * @return 
     */
    public Mono<Void> deleteAll() {
        return this.contriRepo.deleteAll();
    }

    /**
     * Gets all coop contributions
     * @param coopId
     * @return 
     */
    public Flux<Contribution> findByCoopId(String coopId) {
        return this.contriRepo.findByCoopId(coopId);
    }
    
    /**
     * Updates Coop Member
     */
    private Mono<CoopMember> updateCoopMember(Contribution contri) {
        return Mono.just(contri).flatMap(c -> {
            return this.coopMemberService.findByCoopIdAndCoopMemberId(c.getCoopId(), c.getCoopMemberId())
                .flatMap((CoopMember coopMember) -> {
                        coopMember.getContributions().add(c);
                        return this.coopMemberService.update(coopMember.getId(), coopMember);
                });
            });
    }
}
