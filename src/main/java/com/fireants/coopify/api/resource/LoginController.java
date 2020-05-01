/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.resource;

import com.fireants.coopify.api.model.User;
import com.fireants.coopify.api.security.UserDetailsService;
import com.fireants.coopify.api.security.model.AuthRequest;
import com.fireants.coopify.api.security.model.AuthResponse;
import com.fireants.coopify.api.security.util.JWTUtil;
import com.fireants.coopify.api.service.UserService;
import java.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 *
 * @author admin
 */
@Slf4j
@RestController
public class LoginController {
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private UserDetailsService userDetailSevice;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/auth")
    public Mono<ResponseEntity<?>> login(@RequestBody AuthRequest request) {
        return this.userDetailSevice.findByUsername(request.getUsername())
                .map(user -> {
                    String token = null;
                    if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                        token = jwtUtil.generateToken(request.getUsername(), user);
                        return ResponseEntity.ok(new AuthResponse(token));
                    } else {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
                    }
                })
//                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid credentials.")))
                ;

    }
    
    @PostMapping("/signup")
    public Mono<User> signup(@RequestBody User user) {
        return Mono.just(user).publishOn(Schedulers.parallel())
                .map(u -> {
                    u.setPassword(this.passwordEncoder.encode(u.getPassword()));
                    return user;
                })
                .flatMap(u -> {
                    return this.userService.save(u);
                });
    }
    
    @GetMapping("/logout")
    public Mono<Void> clearContext() {
        return Mono.fromRunnable(() -> ReactiveSecurityContextHolder.clearContext());
    }
}
