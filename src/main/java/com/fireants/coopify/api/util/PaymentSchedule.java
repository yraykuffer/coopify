/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.util;

import com.fireants.coopify.api.constants.InterestMode;
import com.fireants.coopify.api.constants.PaymentStatus;
import com.fireants.coopify.api.model.LoanPayment;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 *
 * @author admin
 */
public class PaymentSchedule {
//    private int terms = 1;
//    private String paymentMode = "MONTHLY";
//    private double amount = 0.0;
//    private double interestRate = .03;
//    private LocalDate dateLoaned;
//    private PaymentSchedule(Builder builder) {
//        this.terms = builder.terms;
//        this.amount = builder.amount;
//        this.interestRate = builder.interestRate;
//        this.paymentMode = builder.paymentMode;
//        this.dateLoaned = builder.dateLoaned;
//    }
    
    private PaymentSchedule(){}
    
    public static Builder builder(double amount, LocalDate dateLoaned) {
        return new Builder(amount, dateLoaned);
    }
    
    
    public static class Builder {
        private int terms;
        private String paymentMode;
        private double amount;
        private double interestRate = .03;
        private LocalDate dateLoaned;
        private InterestMode interestMode = InterestMode.FIXED;
        private List<LoanPayment> paymentSchedules = new ArrayList<>();
        
        private Builder(){}
        private Builder(double amount, LocalDate dateLoaned) {
            this.amount = amount;
            this.dateLoaned = dateLoaned;
        }
        
        public Builder terms(int terms) {
            this.terms = terms;
            return this;
        }
        
        public Builder paymentMode(String paymentMode) {
            this.paymentMode = paymentMode;
            return this;
        }
        
        public Builder interestRate(double interestRate) {
            this.interestRate = interestRate / 100;
            return this;
        }
        
         public Builder interestMode(InterestMode mode) {
            this.interestMode = mode;
            return this;
        }
        
        public List<LoanPayment> build() {
            return createPaymentSchedules();
        }
        
        private List<LoanPayment> createPaymentSchedules() {
            
            IntStream.range(1, this.terms + 1)
                .forEach(i -> {
                    LocalDate sDate = this.dateLoaned.plusMonths(i);
                    LoanPayment lp = new LoanPayment();
                    lp.setScheduledDate(sDate);
                    lp.setInterest(amount * interestRate);
                    lp.setAmount(amount / this.terms);
                    lp.setTotal(lp.getInterest() + lp.getAmount());
                    lp.setStatus(PaymentStatus.UNPAID);
                    this.paymentSchedules.add(lp);
                    
                    
                });
            return this.paymentSchedules;
        }
    }
    
    public static void main(String[] args) {
        List<LoanPayment> list = PaymentSchedule.builder(5000, LocalDate.now())
                .terms(6)
                .paymentMode("MONTHLY")
                .interestRate(8)
                .build();
        list.stream().forEach(System.out::println);
    }
}
