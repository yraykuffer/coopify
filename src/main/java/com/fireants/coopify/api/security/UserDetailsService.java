/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.security;

import com.fireants.coopify.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsPasswordService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 *
 * @author admin
 */
@Service
public class UserDetailsService implements ReactiveUserDetailsService, ReactiveUserDetailsPasswordService{
    @Autowired
    private UserRepository repo;
    
    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return this.repo.findByUsername(username).map(u -> u.toAuthUser());
                
    }

    @Override
    public Mono<UserDetails> updatePassword(UserDetails user, String newPassword) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
