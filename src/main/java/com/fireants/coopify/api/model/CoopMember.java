/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.fireants.coopify.api.views.View;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

/**
 *
 * @author admin
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "coopMembers")
public class CoopMember {
    @Id
    @JsonView(View.Summary.class)
    private String id;
    // number of heads
    @JsonView(View.Summary.class)
    private int nHeads = 1;
    @Transient
    @JsonView(View.Summary.class)
    private double totalContributed;
    // sum of total contribution and total share
    @Transient
    @JsonView(View.Summary.class)
    private double expectedAmount;
    @Transient
    @JsonView(View.Summary.class)
    private double groupShare;
    @Transient
    @JsonView(View.Summary.class)
    private double personalShare;
    // sum of group share and personal share
    @Transient
    @JsonView(View.Summary.class)
    private double totalShare; 
    // total interest under name
    @Transient
    private double totalInterest; 
    // total loans under name
    private double totalLoans;
    @Transient
    @JsonView(View.Summary.class)
    private double totalCollected;
    @Transient
    @JsonView(View.Summary.class)
    private double totalBalance;
    @NonNull
    private String coopId;
    @NonNull
    private String memberId;
    @NonNull
    @JsonView(View.Summary.class)
    @DBRef
    private Member member;
    
    @DBRef
    private List<Contribution> contributions = new ArrayList<>();;
    @DBRef
    private List<Loan> loans = new ArrayList<>();
}
