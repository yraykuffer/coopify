/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.resource;

import com.fireants.coopify.api.model.Member;
import com.fireants.coopify.api.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/api/members")
public class MemberController {
    @Autowired
    private MemberService memberService;
    
    /**
     * Gets All members
     * @return all coop members
     */
    @GetMapping
    public Flux<Member> findAll() {
        return this.memberService.findAll();
    }
    
    /**
     * Gets Member by id
     * @param id - must not be null
     * @return Member
     */
    @GetMapping("{id}")
    public Mono<ResponseEntity<Member>> getById(@PathVariable String id) {
        return this.memberService.findById(id)
                .map(c -> ResponseEntity.ok(c))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
    
    /**
     * Deletes Member by id
     * @param id - must not be null
     * @return 
     */
    @DeleteMapping("{id}")
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable String id) {
        return this.memberService.findById(id)
                .flatMap((Member coopM) -> {
                    return this.memberService.deleteById(coopM.getId())
                            .then(Mono.just(ResponseEntity.ok().<Void>build()));
                })
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
    
    /**
     * Adds coop member
     * @param member - must not be null
     * @return Member
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Member> save(@RequestBody Member member){
        return this.memberService.save(member);
    }
    
    /**
     * Updates existing coop member
     * @param member - must not be null
     * @param id - must not be null
     * @return 
     */
    @PutMapping("{id}")
    public Mono<ResponseEntity<Member>> update(@RequestBody Member member, @PathVariable String id) {
        return this.memberService.findById(id)
                .flatMap((Member coopM) -> {
                    return this.memberService.update(coopM.getId(), member)
                            .map((c) -> ResponseEntity.ok(c));
                })
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
}
