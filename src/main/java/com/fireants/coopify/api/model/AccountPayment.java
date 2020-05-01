/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.model;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author admin
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "accountPayments")
public class AccountPayment {
    @Id
    private String id;
    private double amount;
    private LocalDate datePaid;
    private String paidBy;
    private String verifiedBy;
    private String status;
    private String accountId;
    private String refNo;
    @CreatedDate
    private LocalDate createdDate;
}
