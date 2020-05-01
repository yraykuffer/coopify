/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.resource;

import com.fireants.coopify.api.model.Loan;
import com.fireants.coopify.api.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RestController
@RequestMapping("/api/loans")
public class LoanController {
    @Autowired
    private LoanService loanService;
    
    /**
     * Gets All Loan
     * @return all Loan
     */
    @GetMapping
    public Flux<Loan> findAll() {
        return this.loanService.findAll();
    }
    
    @GetMapping("/paged")
    public Mono<Page<Loan>> findAllPaged(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        Pageable p = PageRequest.of(page, size > 100 ? 100 : size);
        return this.loanService.findAllPaged(p);
    }
    
    /**
     * Gets Loan by id
     * @param id - must not be null
     * @return Loan
     */
    @GetMapping("{id}")
    public Mono<ResponseEntity<Loan>> getById(@PathVariable String id) {
        return this.loanService.findById(id)
                .map(c -> ResponseEntity.ok(c))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
    
    /**
     * Deletes Loan by id
     * @param id - must not be null
     * @return 
     */
    @DeleteMapping("{id}")
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable String id) {
        return this.loanService.findById(id)
                .flatMap((Loan loan) -> {
                    return this.loanService.deleteById(loan.getId())
                            .then(Mono.just(ResponseEntity.ok().<Void>build()));
                })
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
    
    /**
     * Adds Loan
     * @param loan - must not be null
     * @return Loan
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Loan> save(@RequestBody Loan loan){
        return this.loanService.save(loan);
    }
    
    /**
     * Updates existing Loan
     * @param loan - must not be null
     * @param id - must not be null
     * @return 
     */
    @PutMapping("{id}")
    public Mono<ResponseEntity<Loan>> update(@RequestBody Loan loan, @PathVariable String id) {
        return this.loanService.findById(id)
                .flatMap((Loan l) -> {
                    return this.loanService.update(l.getId(), loan)
                            .map((c) -> ResponseEntity.ok(c));
                })
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
}
