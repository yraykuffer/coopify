/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.security;

import com.fireants.coopify.api.security.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 *
 * @author admin
 */
@Slf4j
@Component
public class AuthenticationManager implements ReactiveAuthenticationManager{
    @Autowired
    private JWTUtil jwtUtil;
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        try {
            String token = authentication.getName();
            if (token != null && this.jwtUtil.isTokenValid(token)) {
                Collection<? extends GrantedAuthority> roles = 
                        Collections.singletonList(
                                new SimpleGrantedAuthority(this.jwtUtil.getClaims(token).get("role").toString()));
                Authentication auth = new UsernamePasswordAuthenticationToken(this.jwtUtil.getUsername(token), null, roles);
                return Mono.just(auth);
            } else {
                return Mono.empty();
            }
        } catch(Exception e) {
//            log.info("error: {}", e);
//            return Mono.error(new AccessDeniedException("Expired token."));
            return Mono.empty();
        }
        
    }
    
}
