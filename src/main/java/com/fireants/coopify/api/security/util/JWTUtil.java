/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.security.util;

import com.fireants.coopify.api.model.User;
import com.fireants.coopify.api.security.model.AuthUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 *
 * @author admin
 */
@Component
@Slf4j
public class JWTUtil {
    @Value("${coopify.security.secret}")
    private String secretKey;
    
    @Value("${coopify.security.expiration}")
    private String expire;
    
    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(Base64.getEncoder().encodeToString(secretKey.getBytes()))
                .parseClaimsJws(token)
                .getBody();
    }
    
    public String getUsername(String token) {
        return this.getClaims(token).getSubject();
    }
    
    private Boolean isTokenExpired(String token) {
        return new Date().before(this.getClaims(token).getExpiration());
    }
    
    public Boolean isTokenValid(String token) {
        return this.isTokenExpired(token);
    }
    
    public String generateToken(String username, UserDetails user) {
        final Date expireDate = new Date();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date( expireDate.getTime() + Long.parseLong(expire)))
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(secretKey.getBytes()))
                .claim("role", user.getAuthorities().iterator().next().getAuthority())
                .compact();
    }
}
