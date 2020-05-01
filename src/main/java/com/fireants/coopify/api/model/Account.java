/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.model;

import com.mongodb.lang.NonNull;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author admin
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "accounts")
public class Account {
    @Id    
    private String id;
    @NonNull
    private String name;
    private String address;
    private String contactNumber;
    private LocalDate dateStarted;
    private String payEvery;
    @CreatedDate
    private LocalDate dateCreated;
    
    
    
    @DBRef
    List<User> users;
    
    @DBRef
    List<Member> members;
    
    @DBRef
    List<Borrower> borrowers;
    
    @DBRef
    List<AccountPayment> payments;
}
