/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.fireants.coopify.api.constants.CoopStatus;
import com.fireants.coopify.api.views.View;
import com.mongodb.lang.NonNull;
import java.util.ArrayList;
import java.util.Date;
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
@Document(collection = "coops")
public class Coop {
    @JsonView(View.Summary.class)
    @Id
    private String id;
    
    @JsonView(View.Summary.class)
    @NonNull
    private String name;
    
    @JsonView(View.Summary.class)
    private String description;
    
    @JsonView(View.Summary.class)
    private String year;
    
    @JsonView(View.Summary.class)
    @Transient
    private double totalCashOnHand;
    
    @JsonView(View.Summary.class)
    @Transient
    private double totalAmountLoaned;
    
    @JsonView(View.Summary.class)
    @Transient
    private double totalInterestEarned;
    
    @JsonView(View.Summary.class)
    @Transient
    private double totalContribution;
    
    @JsonView(View.Summary.class)
    @Transient
    private double totalCollected;
    
    @JsonView(View.Summary.class)
    @Transient
    private double totalBalance;
    
    @JsonView(View.Summary.class)
    @Transient
    private double total;
    
    @JsonView(View.Summary.class)
    @Transient
    private double managementShare;
    @Transient
    private double sharePerHead;
    
    @JsonView(View.Summary.class)
    @Transient
    private double totalGroupShare;
    
    @JsonView(View.Summary.class)
    @Transient
    private double totalPersonalShare;
    
    @JsonView(View.Summary.class)
    @Transient
    private int totalHeads;
    @NonNull
    private String accountId;
    
    @JsonView(View.Summary.class)
    private Date dateCreated = new Date();
    @JsonView(View.Summary.class)
    private CoopStatus status = CoopStatus.ACTIVE;
    
    @DBRef
    private List<CoopMember> members = new ArrayList<>();
    @DBRef
    private Settings settings;
}
