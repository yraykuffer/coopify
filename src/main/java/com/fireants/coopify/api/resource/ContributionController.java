/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.resource;

import com.fireants.coopify.api.model.Contribution;
import com.fireants.coopify.api.service.ContributionService;
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
@RequestMapping("/api/contributions")
public class ContributionController {
    @Autowired
    private ContributionService contriService;
    
    
    /**
     * Gets All Contributions
     * @return Contributions
     */
    @GetMapping
    public Flux<Contribution> findAll() {
        return this.contriService.findAll();
    }
    
    /**
     * Gets Contribution by id
     * @param id - must not be null
     * @return Contribution
     */
    @GetMapping("{id}")
    public Mono<ResponseEntity<Contribution>> getById(@PathVariable String id) {
        return this.contriService.findById(id)
                .map(c -> ResponseEntity.ok(c))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
    
    /**
     * Deletes Contribution by id
     * @param id - must not be null
     * @return 
     */
    @DeleteMapping("{id}")
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable String id) {
        return this.contriService.findById(id)
                .flatMap((Contribution c) -> {
                    return this.contriService.deleteById(c.getId())
                            .then(Mono.just(ResponseEntity.ok().<Void>build()));
                })
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
    
    /**
     * Adds Contribution
     * @param contri - must not be null
     * @return CoopMember
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Contribution> save(@RequestBody Contribution contri){
        return this.contriService.save(contri);
    }
    
    /**
     * Updates existing Contribution
     * @param contri - must not be null
     * @param id - must not be null
     * @return 
     */
    @PutMapping("{id}")
    public Mono<ResponseEntity<Contribution>> update(@RequestBody Contribution contri, @PathVariable String id) {
        return this.contriService.findById(id)
                .flatMap((Contribution c) -> {
                    return this.contriService.update(c.getId(), contri)
                            .map((r) -> ResponseEntity.ok(r));
                })
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
}
