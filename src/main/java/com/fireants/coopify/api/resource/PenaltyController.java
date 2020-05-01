/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.resource;

import com.fireants.coopify.api.model.Penalty;
import com.fireants.coopify.api.service.PenaltyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author admin
 */
@RestController
@RequestMapping("/api/penalties")
public class PenaltyController {
    @Autowired
    private PenaltyService penaltyService;
    
    /**
     * Gets All Penalty
     * @return all Penalty
     */
    @GetMapping
    public Flux<Penalty> findAll() {
        return this.penaltyService.findAll();
    }
    
    /**
     * Gets Penalty by id
     * @param id - must not be null
     * @return Penalty
     */
    @GetMapping("{id}")
    public Mono<ResponseEntity<Penalty>> getById(@PathVariable String id) {
        return this.penaltyService.findById(id)
                .map(c -> ResponseEntity.ok(c))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
    
    /**
     * Deletes Penalty by id
     * @param id - must not be null
     * @return 
     */
    @DeleteMapping("{id}")
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable String id) {
        return this.penaltyService.findById(id)
                .flatMap((Penalty p) -> {
                    return this.penaltyService.deleteById(p.getId())
                            .then(Mono.just(ResponseEntity.ok().<Void>build()));
                })
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
    
    /**
     * Adds Penalty
     * @param p - must not be null
     * @return Loan
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Penalty> save(@RequestBody Penalty p){
        return this.penaltyService.save(p);
    }
    
    /**
     * Updates existing Loan
     * @param penalty - must not be null
     * @param id - must not be null
     * @return 
     */
    @PutMapping("{id}")
    public Mono<ResponseEntity<Penalty>> update(@RequestBody Penalty penalty, @PathVariable String id) {
        return this.penaltyService.findById(id)
                .flatMap((Penalty p) -> {
                    return this.penaltyService.update(p.getId(), penalty)
                            .map((c) -> ResponseEntity.ok(c));
                })
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
}
