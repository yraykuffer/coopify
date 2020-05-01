/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.service;

import com.fireants.coopify.api.constants.LoanStatus;
import com.fireants.coopify.api.model.Contribution;
import com.fireants.coopify.api.model.CoopMember;
import com.fireants.coopify.api.model.Loan;
import com.fireants.coopify.api.model.LoanPayment;
import com.fireants.coopify.api.repository.CoopMemberRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author Fernando Yray
 */
@Service
public class CoopMemberService {
    @Autowired
    private CoopMemberRepository repo;
    
    @Autowired
    private ContributionService contriService;
    
    @Autowired
    private LoanService loanService;
    
    @Autowired
    private CoopService coopService;
    
    @Autowired
    private ReactiveMongoTemplate template;
    
    /**
     * Gets All members
     * @return all members
     */
    public Flux<CoopMember> findAll() {
        return this.repo.findAll();
    }
    
    /**
     * Gets CoopMember By Id
     * @param id
     * @return member
     */
    public Mono<CoopMember> findById(String id) {
        return this.repo.findById(id);
    }
    
    /**
     * Gets members by coopId specified
     * @param coopId
     * @return members by coopId
     */
    public Flux<CoopMember> findByCoopId(String coopId) {
        return this.repo.findByCoopId(coopId)
                .flatMap(coopMember -> this.updateCoopMember(coopMember)); // lacking group share
    }
    
    /**
     * Adds member to coop
     * @param coop
     * @return member
     */
    public Mono<CoopMember> save(CoopMember coop) {
        return this.repo.save(coop);
    }
    
    /**
     * Updates existing coop member
     * @param coopMemberId
     * @param coop
     * @return updated coop member
     */
    public Mono<CoopMember> update(String coopMemberId, CoopMember coop) {
        coop.setId(coopMemberId);
        return this.repo.save(coop);
    }
    
    /**
     * Deletes CoopMember by id
     * @param id
     * @return 
     */
    public Mono<Void> deleteById(String id) {
        return this.repo.deleteById(id);
    }
    
    /**
     * Deletes all coop members
     * @return 
     */
    public Mono<Void> deleteAll() {
        return this.repo.deleteAll();
    }
    
    /**
     * Deletes all coop members by coopId
     * @return 
     */
    public Mono<Void> deleteAllByCoopId(String coopId) {
        return this.repo.deleteAllByCoopId(coopId)
                .then(Mono.empty());
    }
    
    /**
     * Add contribution to member
     * @param coopMemberId
     * @param contri
     * @return 
     */
    public Mono<Contribution> addContribution(String coopMemberId, Contribution contri) {
        return this.contriService.save(contri)
                .flatMap(c -> {
                    Update update = new Update();
                    update.push("contributions", c);
                    return this.template.findAndModify(Query.query(Criteria.where("id").is(coopMemberId)), update, CoopMember.class)
                            .then(Mono.just(c));
                });
    }
    
    /**
     * Add loan to member
     * @param coopMemberId
     * @param loan
     * @return 
     */
    public Mono<Loan> addLoan(String coopMemberId, Loan loan) {
        return this.loanService.save(loan)
                .flatMap(l -> {
                    Update update = new Update();
                    update.push("loans", l);
                    return this.template.findAndModify(Query.query(Criteria.where("id").is(coopMemberId)), update, CoopMember.class)
                            .then(Mono.just(l));
                });
    }
    
    /**
     * Gets coop member by coopId and id
     * @param coopId
     * @param coopMemberId
     * @return 
     */
    public Mono<CoopMember> findByCoopIdAndCoopMemberId(String coopId, String coopMemberId) {
        return this.repo.findByCoopIdAndId(coopId, coopMemberId)
                .flatMap(coopMember -> this.updateCoopMember(coopMember)); // lacking group share
    }
    

    
    private Mono<CoopMember> updateCoopMember(CoopMember coopM) {
        return Mono.just(coopM)
                .map(coopMember -> {
                    Stream<Loan> loanStream = coopMember.getLoans().stream()
                            .map(loan -> {
                                Stream<LoanPayment> sched = loan.getPayments().stream();
                                sched.map(p -> {
                                        Map<String, Double> map = new HashMap<>();
                                        map.put("totalCollected", p.getTotal());
                                        map.put("totalInterest", p.getInterest());
                                        map.put("totalAmount", p.getAmount());
                                        return map;
                                    })
                                    .reduce((a, b) -> {
                                        a.put("totalCollected", a.get("totalCollected") + b.get("totalCollected"));
                                        a.put("totalInterest", a.get("totalInterest") + b.get("totalInterest"));
                                        a.put("totalAmount", a.get("totalAmount") + b.get("totalAmount"));
                                        return a;
                                    })
                                    .map(map -> {
                                        loan.setTotalCollected(map.get("totalCollected"));
                                        loan.setInterest(map.get("totalInterest"));
                                        loan.setBalance(loan.getPrincipalAmount() - map.get("totalAmount"));
                                        if (map.get("totalAmount") == 0) {
                                            loan.setStatus(LoanStatus.UNPAID);
                                        }else {
                                            if (map.get("totalAmount") >= loan.getPrincipalAmount()) {
                                                loan.setStatus(LoanStatus.PAID);
                                            } else {
                                                loan.setStatus(LoanStatus.PARTIAL);
                                            }
                                        }
                                        return map;
                                    });
                                return loan;
                            });
                    coopMember.setLoans(loanStream.collect(Collectors.toList()));
                    Stream<Contribution> contriStream = coopMember.getContributions().stream();
                    double totalContri = contriStream.mapToDouble(contri -> contri.getAmount()).sum();
                    double totalLoans = coopMember.getLoans().stream()
                            .mapToDouble(loan -> loan.getPrincipalAmount()).sum();
                    coopMember.setTotalContributed(totalContri);
                    coopMember.setTotalLoans(totalLoans);
                    coopMember.setTotalInterest(totalLoans * 0.1);
                    coopMember.setPersonalShare(coopMember.getTotalInterest() * 0.1);
                    coopMember.setTotalShare(coopMember.getPersonalShare() + coopMember.getGroupShare());
                    coopMember.setExpectedAmount(coopMember.getTotalContributed() + coopMember.getTotalShare());
                    return coopMember;
                });
    }
    
    
}
