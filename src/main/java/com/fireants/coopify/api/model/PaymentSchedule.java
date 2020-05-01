/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.model;

import com.fireants.coopify.api.constants.PaymentStatus;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author admin
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "paymentSchedules")
public class PaymentSchedule {
    @Id
    private String id;
    private double amount;
    private double interest;
    @Transient
    private double total;
    private String loanId;
    private Date paymentDate;
    private Date scheduledDate;
    private String notes;
    private PaymentStatus status = PaymentStatus.UNPAID;
    
}
