/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.resource;

import com.fasterxml.jackson.annotation.JsonView;
import com.fireants.coopify.api.model.Contribution;
import com.fireants.coopify.api.model.Coop;
import com.fireants.coopify.api.model.CoopMember;
import com.fireants.coopify.api.model.Loan;
import com.fireants.coopify.api.model.LoanPayment;
import com.fireants.coopify.api.model.Settings;
import com.fireants.coopify.api.service.ContributionService;
import com.fireants.coopify.api.service.CoopMemberService;
import com.fireants.coopify.api.service.CoopService;
import com.fireants.coopify.api.service.LoanPaymentService;
import com.fireants.coopify.api.service.LoanService;
import com.fireants.coopify.api.service.SettingsService;
import com.fireants.coopify.api.views.View;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author admin
 */
@Slf4j
@RestController
//@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/api/coops")
public class CoopController {
    @Autowired
    private CoopService coopService;
    
    @Autowired
    private CoopMemberService coopMemberService;
    
    @Autowired
    private ContributionService contriService;
    
    @Autowired
    private LoanService loanService;
    
    @Autowired
    private LoanPaymentService loanPaymentService;
    
    @Autowired
    private SettingsService settingsService;
    
    /**
     * Gets All coops
     * @return 
     */
    @JsonView(View.Summary.class)
    @GetMapping
    public Flux<Coop> getAllCoops() {
        return this.coopService.findAll();
    }
    
    @GetMapping("/paged")
    public Mono<Page<Coop>> getAllCoopsPaged(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable p = PageRequest.of(page, size);
        return this.coopService.findAllPaged(p);
    }
    
    /**
     * Gets coop by id
     * @param id - coop id
     * @return coop
     */
    @GetMapping("{id}")
    public Mono<ResponseEntity<Coop>> getCoopById(@PathVariable String id) {
        return this.coopService.findById(id)
                .map(coop -> ResponseEntity.ok().body(coop))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
    
    /**
     * Gets coop by id
     * @param id
     * @return 
     */
    @DeleteMapping("{id}")
    public Mono<ResponseEntity<Void>> deleteCoopById(@PathVariable String id) {
        return this.coopService.findById(id)
                .flatMap((Coop c) -> {
                   return this.coopService.deleteById(c.getId())
                           .then(Mono.just(ResponseEntity.ok().<Void>build()));
                })
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
    
    /**
     * Creates new coop
     * @param coop
     * @return 
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Coop> addCoop(@RequestBody Coop coop) {
        return this.coopService.save(coop);
    }
    
    /**
     * Updates existing coop
     * @param coop
     * @param id
     * @return updated coop
     */
    @PutMapping("{id}")
    public Mono<ResponseEntity<Coop>> updateCoop(@RequestBody Coop coop, @PathVariable String id) {
        return this.coopService.findById(id)
                .flatMap((Coop c) -> {
                    return this.coopService.update(c.getId(), coop)
                            .map((Coop c2) -> ResponseEntity.ok().body(c2));
                })
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
    
    
    /**
     * Deletes all coops
     * @return 
     */
    @DeleteMapping
    public Mono<Void> deleteAllCoops() {
        return this.coopService.deleteAll();
    }
    
    /**
     * Adds member to specific coop
     * @param coopId - must not be null
     * @param coop - must not be null
     * @return 
     */
    @PostMapping("/{coopId}/members")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Coop> addMember(@PathVariable String coopId, @RequestBody CoopMember coop) {
        coop.setCoopId(coopId);
        return this.coopMemberService.save(coop)
                .flatMap(cm -> {
                    return this.coopService.addMember(coopId, cm);
                });
    }
    
    /**
     * Gets all members from specific coop
     * @param coopId - must not be null
     * @return 
     */
//    @JsonView(View.Summary.class)
    @GetMapping("/{coopId}/members")
    public Flux<CoopMember> getAllMembers(@PathVariable String coopId) {
        return this.coopMemberService.findByCoopId(coopId);
    }
    
    /**
     * Deletes all members from specific coop
     * @param coopId - must not be null
     * @return 
     */
    @DeleteMapping("/{coopId}/members")
    public Mono<Void> deleteAllMembers(@PathVariable String coopId) {
        return this.coopMemberService.deleteAllByCoopId(coopId);
    }
    
    /**
     * Gets member
     * @param coopId - must not be null
     * @param memberId
     * @return 
     */
    @GetMapping("/{coopId}/members/{memberId}")
    public Mono<ResponseEntity<CoopMember>> getCoopMember(@PathVariable String coopId, @PathVariable String memberId) {
        return this.coopMemberService.findByCoopIdAndCoopMemberId(coopId, memberId)
                .map((CoopMember cm) -> ResponseEntity.ok(cm))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
    
    /**
     * Gets member
     * @param coopId - must not be null
     * @param memberId
     * @param coopMember
     * @return 
     */
    @PutMapping("/{coopId}/members/{memberId}")
    public Mono<ResponseEntity<CoopMember>> updateCoopMember(@PathVariable String coopId, @PathVariable String memberId,
            @Valid @RequestBody CoopMember coopMember) {
        return this.coopMemberService.findByCoopIdAndCoopMemberId(coopId, memberId)
                .flatMap(coopM -> {
                    coopMember.setCoopId(coopM.getCoopId());
                    coopMember.setId(coopM.getMemberId());
                    return this.coopMemberService.save(coopMember)
                            .map((CoopMember cm) -> ResponseEntity.ok(cm));
                })
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
    
    /**
     * Gets member's contributions
     * @param coopId - must not be null
     * @param memberId
     * @return 
     */
    @GetMapping("/{coopId}/members/{memberId}/contributions")
    public Flux<Contribution> getMemberContributions(@PathVariable String coopId, @PathVariable String memberId) {
        return this.contriService.findByCoopMemberId(memberId);
    }
    
    /** Adds contribution
     * @param coopId
     * @param memberId
     * @param contri
     * @return 
     */
    @PostMapping("/{coopId}/members/{memberId}/contributions")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Contribution> addContribution(@PathVariable String coopId, @PathVariable String memberId,
            @RequestBody Contribution contri) {
        contri.setCoopId(coopId);
        contri.setCoopMemberId(memberId);
        return this.coopMemberService.addContribution(memberId, contri);
    }
    
    /**
     * Gets coop contributions
     * @param coopId - must not be null
     * @return 
     */
    @GetMapping("/{coopId}/contributions")
    public Flux<Contribution> getContributions(@PathVariable String coopId) {
        return this.contriService.findByCoopId(coopId);
    }
    
    /**
     * Gets member's loans
     * @param coopId - must not be null
     * @param memberId
     * @return 
     */
    @GetMapping("/{coopId}/members/{memberId}/loans")
    public Flux<Loan> getMemberLoans(@PathVariable String coopId, @PathVariable String memberId) {
        return this.loanService.findByCoopIdAndCoopMemberId(coopId, memberId);
    }
    
    /**
     * Gets loan by id
     * @param coopId - must not be null
     * @param memberId - must not be null
     * @param loanId - must not be null
     * @return 
     */
    @GetMapping("/{coopId}/members/{memberId}/loans/{loanId}")
    public Mono<ResponseEntity<Loan>> getLoanById(@PathVariable String coopId, @PathVariable String memberId,
            @PathVariable String loanId) {
        return this.loanService.findByCoopIdAndCoopMemberIdAndId(coopId, memberId, loanId)
                .map(loan -> ResponseEntity.ok(loan))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
    
    /** Adds loan
     * @param coopId
     * @param memberId
     * @param loan
     * @return 
     */
    @PostMapping("/{coopId}/members/{memberId}/loans")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Loan> applyLoan(@PathVariable String coopId, @PathVariable String memberId,
            @RequestBody Loan loan) {
        loan.setCoopId(coopId);
        loan.setCoopMemberId(memberId);
        return this.coopMemberService.addLoan(coopId, loan);
    }
    
    /**
     * Gets loan by coopId
     * @param coopId - must not be null
     * @return 
     */
    @GetMapping("/{coopId}/loans")
    public Flux<Loan> getLoansByCoopId(@PathVariable String coopId) {
        return this.loanService.findByCoopId(coopId);
    }
    
    /**
     * Gets loan by coopId
     * @param coopId - must not be null
     * @param loanId
     * @return 
     */
    @GetMapping("/{coopId}/loans/{loanId}")
    public Mono<ResponseEntity<Loan>> getLoansByCoopIdAndLoanId(@PathVariable String coopId, @PathVariable String loanId) {
        return this.loanService.findByIdAndCoopId(loanId, coopId)
                .map(loan -> ResponseEntity.ok(loan))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
    
    /**
     * Deletes loan by coopId and loanId
     * @param coopId - must not be null
     * @param loanId
     * @return 
     */
    @DeleteMapping("/{coopId}/loans/{loanId}")
    public Mono<ResponseEntity<String>> deleteLoanByCoopIdAndLoanId(@PathVariable String coopId, @PathVariable String loanId) {
        return this.loanService.findByIdAndCoopId(loanId, coopId)
                .flatMap(loan -> {
                    return this.loanService.deleteById(loan.getId())
                            .then(Mono.just(ResponseEntity.ok("Loan successfully deleted.")));
                })
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Loan not found.")));
    }
    
    /**
     * Updates loan by coopId and loanId
     * @param coopId - must not be null
     * @param loanId
     * @param loan
     * @return 
     */
    @PutMapping("/{coopId}/loans/{loanId}")
    public Mono<ResponseEntity<Loan>> updateLoanByCoopIdAndLoanId(@PathVariable String coopId, @PathVariable String loanId,
            @RequestBody Loan loan) {
        return this.loanService.findByIdAndCoopId(loanId, coopId)
                .flatMap(l -> {
                    loan.setCoopId(coopId);
                    return this.loanService.update(l.getId(), loan)
                            .map(val -> ResponseEntity.ok(val));
                })
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
    
    // START LOAN PAYMENTS
    /**
     * Gets loan payments
     * @param coopId - must not be null
     * @param memberId
     * @param loanId
     * @return 
     */
    @GetMapping("/{coopId}/members/{memberId}/loans/{loanId}/payments")
    public Flux<LoanPayment> getMemmberLoanPayments(@PathVariable String coopId, @PathVariable String memberId,
            @PathVariable String loanId) {
        return this.loanService.findByCoopIdAndCoopMemberIdAndId(coopId, memberId, loanId)
                .flatMapMany(loan -> {
                    return this.loanPaymentService.findByLoanId(loan.getId());
                });
    }
    
    /**
     * Gets loan payment by id
     * @param coopId - must not be null
     * @param memberId - must not be null
     * @param loanId - must not be null
     * @param paymentId
     * @return 
     */
    @GetMapping("/{coopId}/members/{memberId}/loans/{loanId}/payments/{paymentId}")
    public Mono<ResponseEntity<LoanPayment>> getMemmberLoanPaymentById(@PathVariable String coopId, @PathVariable String memberId,
            @PathVariable String loanId, @PathVariable String paymentId) {
        return this.loanService.findByCoopIdAndCoopMemberIdAndId(coopId, memberId, loanId)
                .flatMap(loan -> {
                    return this.loanPaymentService.findById(paymentId)
                            .map(payment -> ResponseEntity.ok(payment));
                })
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
    
    /** Adds loan payment
     * @param coopId
     * @param memberId
     * @param payment
     * @param loanId
     * @return 
     */
    @PostMapping("/{coopId}/members/{memberId}/loans/{loanId}/payments")
    public Mono<ResponseEntity<LoanPayment>> addLoanPyament(@PathVariable String coopId, @PathVariable String memberId,
            @PathVariable String loanId, @RequestBody LoanPayment payment) {
        return this.loanService.findByCoopIdAndCoopMemberIdAndId(coopId, memberId, loanId)
                .flatMap(loan -> {
                    payment.setLoanId(loan.getId());
                    return this.loanPaymentService.save(payment)
                            .map(p -> ResponseEntity.status(HttpStatus.CREATED).body(p));
                })
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
    
    /** Updates loan payment
     * @param coopId
     * @param memberId
     * @param payment
     * @param paymentId
     * @param loanId
     * @return 
     */
    @PutMapping("/{coopId}/members/{memberId}/loans/{loanId}/payments/{paymentId}")
    public Mono<ResponseEntity<LoanPayment>> updateLoanPayment(@PathVariable String coopId, @PathVariable String memberId,
            @PathVariable String loanId, @PathVariable String paymentId, @RequestBody LoanPayment payment) {
        return this.loanService.findByCoopIdAndCoopMemberIdAndId(coopId, memberId, loanId)
                .flatMap(loan -> {
                    payment.setLoanId(loan.getId());
                    return this.loanPaymentService.update(paymentId, payment)
                            .map(p -> ResponseEntity.ok(p));
                })
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
    
    /** Deletes loan payment
     * @param coopId
     * @param memberId
     * @param payment
     * @param paymentId
     * @param loanId
     * @return 
     */
    @DeleteMapping("/{coopId}/members/{memberId}/loans/{loanId}/payments/{paymentId}")
    public Mono<ResponseEntity<Void>> deletePayment(@PathVariable String coopId, @PathVariable String memberId,
            @PathVariable String loanId, @PathVariable String paymentId, @RequestBody LoanPayment payment) {
        return this.loanService.findByCoopIdAndCoopMemberIdAndId(coopId, memberId, loanId)
                .flatMap(loan -> {
                    return this.loanPaymentService.findById(paymentId)
                            .flatMap(p -> {
                                return this.loanPaymentService.deleteById(p.getId())
                                        .then(Mono.just(ResponseEntity.ok().<Void>build()));
                            })
                            .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
                })
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
    // END LOAN PAYMENTS
    
    // START SETTINGS
    /**
     * Adds settings to coop
     * @param coopId - must not be null
     * @param settings
     * @return 
     */
    @PostMapping("/{coopId}/settings")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Settings> addSettings(@PathVariable String coopId, @RequestBody Settings settings) {
        settings.setCoopId(coopId);
        return this.settingsService.save(settings);
    }
    
    /**
     * Gets settings
     * @param coopId - must not be null
     * @return 
     */
    @GetMapping("/{coopId}/settings")
    public Mono<ResponseEntity<Settings>> getSettings(@PathVariable String coopId) {
        return this.settingsService.findByCoopId(coopId)
                .map(settings -> ResponseEntity.ok(settings))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
    
    /**
     * Updates settings
     * @param coopId - must not be null
     * @param settings
     * @return 
     */
    @PutMapping("/{coopId}/settings")
    public Mono<ResponseEntity<Settings>> updateSettings(@PathVariable String coopId, @RequestBody Settings settings) {
        return this.settingsService.findByCoopId(coopId)
                .flatMap(s -> {
                    settings.setCoopId(coopId);
                    return this.settingsService.update(s.getId(), settings);
                })
                .map(s -> ResponseEntity.ok(s))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
    //END SETTINGS
    
    /**
     * Deletes settings
     * @param coopId - must not be null
     * @return 
     */
    @DeleteMapping("/{coopId}/settings")
    public Mono<ResponseEntity<Void>> deleteSettings(@PathVariable String coopId) {
        return this.settingsService.findByCoopId(coopId)
                .flatMap(s -> {
                    return this.settingsService.deleteById(s.getId())
                            .then(Mono.just(ResponseEntity.ok().<Void>build()));
                })
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
    //END SETTINGS
}
