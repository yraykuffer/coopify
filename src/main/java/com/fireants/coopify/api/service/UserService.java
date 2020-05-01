/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.service;

import com.fireants.coopify.api.model.User;
import com.fireants.coopify.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author admin
 */
@Service
public class UserService {
    @Autowired
    private UserRepository repo;
    
    /**
     * Gets All Users
     * @return all Users
     */
    public Flux<User> findAll() {
        return this.repo.findAll();
    }
    
    /**
     * Get User By Id
     * @param id
     * @return User by id
     */
    public Mono<User> findById(String id) {
        return this.repo.findById(id);
    }
    
    /**
     * Gets Users by account specified
     * @param accountId
     * @return users by account
     */
    public Flux<User> findByAccountId(String accountId) {
        return this.repo.findByAccountId(accountId);
    }
    
    /**
     * Saves new User
     * @param user
     * @return user
     */
    public Mono<User> save(User user) {
        return this.repo.save(user);
    }
    
    /**
     * Updates existing account
     * @param userId
     * @param user
     * @return updated account
     */
    public Mono<User> update(String userId, User user) {
        user.setId(userId);
        return this.repo.save(user);
    }
    
    /**
     * Deletes User by id
     * @param id
     * @return Void
     */
    public Mono<Void> deleteById(String id) {
        return this.repo.deleteById(id);
    }
    
    /**
     * Deletes all users
     * @return 
     */
    public Mono<Void> deleteAll() {
        return this.repo.deleteAll();
    }

    public Mono<User> findByUsername(String username) {
        return this.repo.findByUsername(username);
    }
    
}
