/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.resource;

import com.fireants.coopify.api.model.User;
import com.fireants.coopify.api.service.UserService;
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
@PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN')")
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;
    
    /**
     * Gets all users
     * @return all Flux
     */
    @GetMapping
    public Flux<User> list() {        
        return userService.findAll();
    }
    
    /**
     * Get user by id
     * @param Id
     * @return 
     */
    @GetMapping("{Id}")
    public Mono<ResponseEntity<User>> getById(@PathVariable String Id) {
        return this.userService.findById(Id)
                .map(u -> ResponseEntity.ok(u))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
    /**
     * Deletes User by id
     * @param id - must not be null
     * @return 
     */
    @DeleteMapping("{id}")
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable String id) {
        return this.userService.findById(id)
                .flatMap((User u) -> {
                    return this.userService.deleteById(u.getId())
                            .then(Mono.just(ResponseEntity.ok().<Void>build()));
                })
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
    
    /**
     * Adds user
     * @param u - must not be null
     * @return Member
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<User> save(@RequestBody User u){
        return this.userService.save(u);
    }
    
    /**
     * Updates existing user
     * @param user - must not be null
     * @param id - must not be null
     * @return 
     */
    @PutMapping("{id}")
    public Mono<ResponseEntity<User>> update(@RequestBody User user, @PathVariable String id) {
        return this.userService.findById(id)
                .flatMap((User u) -> {
                    user.setId(u.getId());
                    return this.userService.save(user)
                            .map((c) -> ResponseEntity.ok(c));
                })
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
    
}
