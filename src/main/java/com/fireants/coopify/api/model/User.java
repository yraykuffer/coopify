/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.model;


import com.fireants.coopify.api.security.model.AuthUser;
import java.util.Collection;
import java.util.Collections;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author admin
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "users")
public class User{
    @Id
    private String id;
    @Indexed(unique = true)
    @NonNull
    private String username;
    @NonNull
    private String password;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @NonNull
    private String emailAddress;
    private String contactNumber;
    @NonNull
    private String accountId;
    private String role;
    
    public UserDetails toAuthUser() {
        return new AuthUser(this);
    }
}
