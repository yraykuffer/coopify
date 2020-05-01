/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.model;

import com.fireants.coopify.api.constants.PaymentStatus;
import com.mongodb.lang.NonNull;
import java.time.LocalDate;
import java.util.Date;
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
@Document(collection = "payments")
public class LoanPayment {
    @Id
    private String id;
    @NonNull
    private double amount;
    private double interest;
    @Transient
    private double total; // amount + interest
    private LocalDate paymentDate;
    private LocalDate scheduledDate;
    private String notes;
    @NonNull
    private String loanId;
    private PaymentStatus status = PaymentStatus.UNPAID;
}
