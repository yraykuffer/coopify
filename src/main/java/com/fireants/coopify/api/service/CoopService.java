/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.service;

import com.fireants.coopify.api.constants.LoanStatus;
import com.fireants.coopify.api.constants.PaymentStatus;
import com.fireants.coopify.api.model.Contribution;
import com.fireants.coopify.api.model.Coop;
import com.fireants.coopify.api.model.CoopMember;
import com.fireants.coopify.api.model.Loan;
import com.fireants.coopify.api.model.LoanPayment;
import com.fireants.coopify.api.repository.CoopRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author admin
 */
@Service
public class CoopService {
    @Autowired
    private CoopRepository repo;
    
    @Autowired
    private LoanService loanService;
    
    @Autowired
    private ContributionService contriService;
    
    @Autowired
    ReactiveMongoTemplate template;
    
    /**
     * Gets All Coops
     * @return all Coops
     */
    public Flux<Coop> findAll() {
        return this.repo.findAll()
            .map(coop -> this.processCoop(coop));
    }
    
    public Mono<Page<Coop>> findAllPaged(Pageable page) {
        return this.template.count(new Query(), Coop.class)
                .flatMap(coopCount -> {
                    return this.template.find(new Query().with(page), Coop.class)
                            .collectList()
                            .map(list -> new PageImpl<Coop>(list, page, coopCount));
                });
    }
    
    /**
     * Gets Coop By Id
     * @param id
     * @return Coop
     */
    public Mono<Coop> findById(String id) {
        return this.repo.findById(id)
                .map(coop -> this.processCoop(coop));
    }
    
    /**
     * Gets Coops by account specified
     * @param accountId
     * @return Coops by account
     */
    public Flux<Coop> findByAccountId(String accountId) {
        return this.repo.findByAccountId(accountId)
                .map(coop -> this.processCoop(coop));
    }
    
    /**
     * Saves new Coop
     * @param coop
     * @return coop
     */
    public Mono<Coop> save(Coop coop) {
        return this.repo.save(coop);
    }
    
    /**
     * Updates existing coop
     * @param coopId
     * @param coop
     * @return updated account
     */
    public Mono<Coop> update(String coopId, Coop coop) {
        coop.setId(coopId);
        return this.repo.save(coop);
    }
    
    /**
     * Deletes Coop by id
     * @param id
     * @return 
     */
    public Mono<Void> deleteById(String id) {
        return this.repo.deleteById(id);
    }
    
    /**
     * Deletes all coops
     * @return 
     */
    public Mono<Void> deleteAll() {
        return this.repo.deleteAll();
    }
    
    /** Enroll member to coop
     * @param coopId
     * @param member
     * @return 
     */
    public Mono<Coop> addMember(String coopId, CoopMember member) {
        Update update = new Update();
        member.setCoopId(coopId);
        update.push("members", member);
        return template.findAndModify(Query.query(Criteria.where("id").is(coopId)), update, Coop.class);
    }
    
    /**
     * Gets updated coop
     */
    private Coop processCoop(Coop coop) {
        Stream<CoopMember> coopStream = coop.getMembers().stream();
        coopStream = coopStream.map(coopM -> {
            Stream<Loan> loanStream = coopM.getLoans().stream()
                .map(loan -> {
                    double totalCollect = loan.getPayments().stream()
                            .filter(l->l.getStatus() == PaymentStatus.PAID).mapToDouble(l -> l.getTotal()).sum();
                    double totalInterest = loan.getPayments().stream()
                            .filter(l->l.getStatus() == PaymentStatus.PAID).mapToDouble(l -> l.getInterest()).sum();
                    double totalAmount = loan.getPayments().stream()
                            .filter(l->l.getStatus() == PaymentStatus.PAID).mapToDouble(l -> l.getAmount()).sum();
                    loan.setTotalCollected(totalCollect);
                    loan.setInterest(totalInterest);
                    loan.setBalance(loan.getPrincipalAmount() - totalAmount);
                    if (totalAmount == 0) {
                        loan.setStatus(LoanStatus.UNPAID);
                    } else {
                        if (totalAmount >= loan.getPrincipalAmount()) {
                            loan.setStatus(LoanStatus.PAID);
                        } else {
                            loan.setStatus(LoanStatus.PARTIAL);
                        }
                    }
                    return loan;
                });
            coopM.setLoans(loanStream.collect(Collectors.toList()));
            Stream<Contribution> contriStream = coopM.getContributions().stream();
            double totalContri = contriStream.mapToDouble(contri -> contri.getAmount()).sum();
            double totalLoans = coopM.getLoans().stream()
                .mapToDouble(loan -> loan.getPrincipalAmount()).sum();
            double totalCollected = coopM.getLoans().stream()
                .mapToDouble(loan -> loan.getTotalCollected()).sum();
            double totalBalance = coopM.getLoans().stream()
                .mapToDouble(loan -> loan.getBalance()).sum();
            double interestRate = 0;
            if (coop.getSettings() != null) {
                interestRate = coop.getSettings().getInterestRate() / 100;
            }
            coopM.setTotalContributed(totalContri);
            coopM.setTotalLoans(totalLoans);
            coopM.setTotalInterest(totalLoans * interestRate);
            coopM.setPersonalShare(coopM.getTotalInterest() * interestRate);
            coopM.setExpectedAmount(coopM.getTotalContributed() + coopM.getTotalShare());
            coopM.setTotalCollected(totalCollected);
            coopM.setTotalBalance(totalBalance);
            return coopM;
        });
        coop.setMembers(coopStream.collect(Collectors.toList()));
        double totalInterestEarned = coop.getMembers().stream().mapToDouble(m -> m.getTotalInterest()).sum();
        double totalContribution = coop.getMembers().stream().mapToDouble(m -> m.getTotalContributed()).sum();
        double totalLoans = coop.getMembers().stream().mapToDouble(m -> m.getTotalLoans()).sum();
        double totalCollection = coop.getMembers().stream().mapToDouble(m -> m.getTotalCollected()).sum();
        double totalBalance = coop.getMembers().stream().mapToDouble(m -> m.getTotalBalance()).sum();
        int totalHeads = coop.getMembers().stream().mapToInt(m -> m.getNHeads()).sum();
        coop.setTotalInterestEarned(totalInterestEarned);
        coop.setTotalContribution(totalContribution);
        coop.setTotalAmountLoaned(totalLoans);
        coop.setTotalCollected(totalCollection);
        coop.setTotalBalance(totalBalance);
        coop.setTotal(coop.getTotalContribution() + coop.getTotalInterestEarned());
        coop.setTotalHeads(totalHeads);
        // fines not yet included
        double cashOnHand = (coop.getTotalContribution() - coop.getTotalAmountLoaned())
            + coop.getTotalCollected();
        double interestRate = 0;
        if (coop.getSettings() != null) {
            interestRate = coop.getSettings().getInterestRate() / 100;
        }
        coop.setTotalCashOnHand(cashOnHand);
        coop.setManagementShare(coop.getTotalInterestEarned() * interestRate);
        coop.setTotalPersonalShare( coop.getTotalInterestEarned() * interestRate);
        coop.setTotalGroupShare(coop.getTotalInterestEarned() - 
            (coop.getTotalPersonalShare() + coop.getManagementShare()));
        coop.setSharePerHead(coop.getTotalGroupShare() / coop.getTotalHeads()); // to be changed
        Stream<CoopMember> coopMemberStream = coop.getMembers().stream()
            .map(cm -> {
                cm.setGroupShare(coop.getSharePerHead() * cm.getNHeads());
                cm.setTotalShare(cm.getPersonalShare() + cm.getGroupShare());
                return cm;
            });
        coop.setMembers(coopMemberStream.collect(Collectors.toList()));
        return coop;           
    }
}
