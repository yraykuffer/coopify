/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.service;

import com.fireants.coopify.api.constants.InterestMode;
import com.fireants.coopify.api.constants.LoanStatus;
import com.fireants.coopify.api.constants.PaymentStatus;
import com.fireants.coopify.api.model.CoopMember;
import com.fireants.coopify.api.model.Loan;
import com.fireants.coopify.api.model.LoanPayment;
import com.fireants.coopify.api.util.PaymentSchedule;
import com.fireants.coopify.api.repository.LoanRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.format.number.money.MonetaryAmountFormatter;
import org.springframework.stereotype.Service;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author admin
 */
@Service
public class LoanService {
    @Autowired
    private LoanRepository repo;
    
    @Autowired
    private LoanPaymentService loanPaymentService;
    
    @Autowired
    private PaymentScheduleService paymentScheduleService;
    
    @Autowired
    private CoopMemberService coopMemberService;
    
    @Autowired
    private ReactiveMongoTemplate template;
    
    /**
     * Gets All loans
     * @return loans
     */
    public Flux<Loan> findAll() {
        return this.repo.findAll();
    }
    
    /**
     * Gets Loan By Id
     * @param id
     * @return loan
     */
    public Mono<Loan> findById(String id) {
        return this.repo.findById(id);
    }
    
    public Mono<Page<Loan>> findAllPaged(Pageable p) {
        Query q = new Query();
        q.addCriteria(Criteria.where("id"));
        return this.template.count(new Query(), Loan.class)
                .flatMap(loanCount -> {
                   return this.template.find(new Query().with(p), Loan.class)
                           .collectList()
                           .map(list -> new PageImpl<>(list, p, loanCount));
                });
    }
    
    /**
     * Gets loans by memberId specified
     * @param coopId
     * @param memberId
     * @return loans by member
     */
    public Flux<Loan> findByCoopIdAndCoopMemberId(String coopId, String memberId) {
        return this.repo.findByCoopIdAndCoopMemberId(coopId, memberId)
                .flatMap(loan -> {
                    return this.processLoan(Mono.just(loan));
                });
    }
    
    /**
     * Gets loans by borrower specified
     * @param loanedBy
     * @return loans by borrower
     */
    public Flux<Loan> findByLoanedBy(String loanedBy) {
        return this.repo.findByLoanedBy(loanedBy);
    }
    
    /**
     * Adds Loan
     * @param loan
     * @return loan
     */
    public Mono<Loan> save(Loan loan) {
        if (loan.getTerms() > 0) {
            List<LoanPayment> paymentSchedules = PaymentSchedule.builder(loan.getPrincipalAmount(), loan.getLoanedDate())
                    .interestMode(InterestMode.FIXED)
                    .interestRate(10)
                    .paymentMode("MONTHLY")
                    .terms(loan.getTerms())
                    .build();
            return this.loanPaymentService.saveAll(paymentSchedules)
                .collectList()
                .flatMap(payments -> {
                    loan.setPayments(payments);
                    return this.repo.save(loan)
                        .flatMap(l -> this.updateCoopMember(l).then(Mono.just(l)));
                }); 
        } else {
            return this.repo.save(loan)
                    .flatMap(l -> this.updateCoopMember(l).then(Mono.just(l)));
        }
        
    }
    
    /**
     * Updates existing Loan
     * @param loanId
     * @param loan
     * @return updated loan
     */
    public Mono<Loan> update(String loanId, Loan loan) {
        loan.setId(loanId);
        return this.repo.save(loan);
    }
    
    /**
     * Deletes Loan by id
     * @param id
     * @return 
     */
    public Mono<Void> deleteById(String id) {
        return this.repo.deleteById(id);
    }
    
    /**
     * Deletes all loans
     * @return 
     */
    public Mono<Void> deleteAll() {
        return this.repo.deleteAll();
    }
    
    /**
     * Gets loan by coop id, memberId and loan id
     * @param coopId
     * @param memberId
     * @param loanId
     * @return Loan
     */
    public Mono<Loan> findByCoopIdAndCoopMemberIdAndId(String coopId, String memberId, String loanId) {
        return this.processLoan(this.repo.findByCoopIdAndCoopMemberIdAndId(coopId, memberId, loanId));
                
    }
    
    /**
     * Gets loans by coop id
     * @param coopId
     * @return Loans 
     */
    public Flux<Loan> findByCoopId(String coopId) {
        return this.repo.findByCoopId(coopId)
                .flatMap(loan -> this.processLoan(Mono.just(loan)));
    }
    
    /**
     * Gets Loan by id and coop
     * @param id
     * @param coopId
     * @return 
     */
    public Mono<Loan> findByIdAndCoopId(String id, String coopId) {
        return this.repo.findByIdAndCoopId(id, coopId);
                
    }
    
    private Mono<Loan> processLoan(Mono<Loan> loanMono) {
        return loanMono.map(loan -> {
                    if (loan.getPayments()!= null) {
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
                            return loan;
                        });
                    }
                    return loan;
                });
        
    }
    
    private Mono<CoopMember> updateCoopMember(Loan loan) {
        return Mono.just(loan).flatMap(l -> {
            return this.coopMemberService.findByCoopIdAndCoopMemberId(l.getCoopId(), l.getCoopMemberId())
                .flatMap((CoopMember coopMember) -> {
                        coopMember.getLoans().add(l);
                        return this.coopMemberService.update(coopMember.getId(), coopMember);
                });
            });
    }
}
