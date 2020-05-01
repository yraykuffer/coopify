/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.model;

import com.fireants.coopify.api.constants.LoanStatus;
import com.mongodb.lang.NonNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author admin
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "loans")
public class Loan {
    @Id 
    private String id;
    private double principalAmount;
    @Transient
    private double totalCollected;
    @Transient
    private double interest;
    @Transient
    private double balance;
    private int terms; // in months
    private LocalDate loanedDate;
    
    @NonNull
    private String coopMemberId;
    private String coopId;
    private String loanedBy;
    private LoanStatus status = LoanStatus.UNPAID;
    
    @DBRef
    private List<LoanPayment> payments = new ArrayList<>();
}
