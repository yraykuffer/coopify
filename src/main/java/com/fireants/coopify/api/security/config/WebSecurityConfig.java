/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.security.config;

import com.fireants.coopify.api.security.AuthenticationManager;
import com.fireants.coopify.api.security.SampleWebFilter;
import com.fireants.coopify.api.security.SecurityContextRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

/**
 *
 * @author admin
 */
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebSecurityConfig {
    
    @Autowired
    private AuthenticationManager authicationMananager;
    @Autowired
    private SecurityContextRepository securityContextRepository;
//    @Autowired
//    private SampleWebFilter webFilter;
    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        http.exceptionHandling()
                .authenticationEntryPoint((swe, e) -> {
                    return Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED));
                })
                .accessDeniedHandler((swe, e) -> {
                    return Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN));
                }).and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().and()
                .authenticationManager(authicationMananager)
                .securityContextRepository(securityContextRepository)
                .authorizeExchange()
//                .pathMatchers("/api/users/**", "/api/accounts/**").hasAuthority("ADMIN")
//                .pathMatchers("/api/users/**", "/api/accounts/**").hasAuthority("SUPER_ADMIN")
                .pathMatchers("/auth", "/signup", "/context", "/logout").permitAll()
                .anyExchange().authenticated();
        
        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
