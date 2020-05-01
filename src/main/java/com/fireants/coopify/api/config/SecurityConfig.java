/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.UserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.csrf.ServerCsrfTokenRepository;
import org.springframework.security.web.server.csrf.WebSessionServerCsrfTokenRepository;

/**
 *
 * @author admin
 */
//@EnableWebFluxSecurity
//@EnableReactiveMethodSecurity
public class SecurityConfig {
//    @Bean
//    MapReactiveUserDetailsService userDetailService () {
//        UserDetails users = User.builder()
//                .username("admin")
//                .password("admin")
//                .passwordEncoder(pass -> passwordEncoder().encode(pass))
//                .authorities("ADMIN")
//                .roles("ADMIN")
//                .build();
//        return new MapReactiveUserDetailsService(users);
//    }
    
//    @Bean
//    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
//        http.authorizeExchange()
//                .pathMatchers("/api/**").hasRole("ADMIN")
//                .pathMatchers("/api/**").permitAll()
//                .anyExchange().permitAll()
//                .access((mono, context) -> mono.map(auth -> auth.getAuthorities().stream()
//                        .filter(e -> e.getAuthority().equals("ROLE_ADMIN"))
//                        .count() > 0)
//                        .map(AuthorizationDecision::new))
//                .authenticated()
//                .and()
//                    .httpBasic()
//                .and()
//                    .formLogin()
//                .and()
//                .csrf()
//                    .csrfTokenRepository(csrfTokenRepository())
//                .and()
//                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
//                .cors().disable()
                ;
//        return http.build();
//    }
//    @Bean
//    PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
    
//    @Bean
//    public ServerCsrfTokenRepository csrfTokenRepository() {
//        WebSessionServerCsrfTokenRepository repository = new WebSessionServerCsrfTokenRepository();
//        repository.setHeaderName("X-CSRF-Token");
//        return repository;
//    }
}
