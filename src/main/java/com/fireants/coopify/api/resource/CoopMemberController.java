/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.resource;

import com.fireants.coopify.api.model.CoopMember;
import com.fireants.coopify.api.service.CoopMemberService;
import com.fireants.coopify.api.service.CoopService;
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
@RequestMapping("/api/coop-members")
public class CoopMemberController {
    @Autowired
    private CoopMemberService coopMemberService;
    @Autowired
    private CoopService coopService;
    
    /**
     * Gets All members
     * @return all coop members
     */
    @GetMapping
    public Flux<CoopMember> findAll() {
        return this.coopMemberService.findAll();
    }
    
    /**
     * Gets CoopMember by id
     * @param id - must not be null
     * @return CoopMember
     */
    @GetMapping("{id}")
    public Mono<ResponseEntity<CoopMember>> getById(@PathVariable String id) {
        return this.coopMemberService.findById(id)
                .map(c -> ResponseEntity.ok(c))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
    
    /**
     * Deletes CoopMember by id
     * @param id - must not be null
     * @return 
     */
    @DeleteMapping("{id}")
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable String id) {
        return this.coopMemberService.findById(id)
                .flatMap((CoopMember coopM) -> {
                    return this.coopMemberService.deleteById(coopM.getMemberId())
                            .then(Mono.just(ResponseEntity.ok().<Void>build()));
                })
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
    
    /**
     * Adds coop member
     * @param member - must not be null
     * @return CoopMember
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CoopMember> save(@RequestBody CoopMember member){
        return this.coopMemberService.save(member);
    }
    
    /**
     * Updates existing coop member
     * @param member - must not be null
     * @param id - must not be null
     * @return 
     */
    @PutMapping("{id}")
    public Mono<ResponseEntity<CoopMember>> update(@RequestBody CoopMember member, @PathVariable String id) {
        return this.coopMemberService.findById(id)
                .flatMap((CoopMember coopM) -> {
                    return this.coopMemberService.update(coopM.getId(), member)
                            .map((c) -> ResponseEntity.ok(c));
                })
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
}
