/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.builder;

import com.fireants.coopify.api.constants.InterestMode;
import com.fireants.coopify.api.constants.PaymentStatus;
import com.fireants.coopify.api.model.LoanPayment;
import com.fireants.coopify.api.util.PaymentSchedule;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 *
 * @author admin
 */
public class PaymentScheduleBuilder implements Builder<LoanPayment> {

    private int terms;
    private String paymentMode;
    private double amount = 0;
    private double interestRate = .03;
    private LocalDate dateLoaned = LocalDate.now();
    private InterestMode interestMode = InterestMode.FIXED;
    private int multiplier = 1;
    private final List<LoanPayment> paymentSchedules = new ArrayList<>();

    public PaymentScheduleBuilder terms(int terms) {
        this.terms = terms;
        return this;
    }

    public PaymentScheduleBuilder paymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
        return this;
    }

    public PaymentScheduleBuilder interestRate(double interestRate) {
        this.interestRate = interestRate / 100;
        return this;
    }

    public PaymentScheduleBuilder interestMode(InterestMode mode) {
        this.interestMode = mode;
        return this;
    }

    public PaymentScheduleBuilder amount(double amount) {
        this.amount = amount;
        return this;
    }
    public PaymentScheduleBuilder dateLoaned(LocalDate dateLoaned) {
        this.dateLoaned = dateLoaned;
        return this;
    }
    
    public PaymentScheduleBuilder monthly() {
        this.multiplier = 1;
        return this;
    }
    
    public PaymentScheduleBuilder semiMonthly() {
        this.multiplier = 2;
        return this;
    }
    
    public PaymentScheduleBuilder weekly() {
        this.multiplier = 4;
        return this;
    }

    @Override
    public List<LoanPayment> build() {
        return createPaymentSchedules();
    }

    private List<LoanPayment> createPaymentSchedules() {
        this.terms = this.terms * this.multiplier;
        IntStream.range(1, this.terms + 1)
                .forEach(i -> {
                    LocalDate sDate = this.getNextDate();
                    LoanPayment lp = new LoanPayment();
                    lp.setId(""+i);
                    lp.setScheduledDate(sDate);
                    lp.setInterest(amount * interestRate);
                    lp.setAmount(amount / this.terms);
                    lp.setTotal(lp.getInterest() + lp.getAmount());
                    lp.setStatus(PaymentStatus.UNPAID);
                    this.paymentSchedules.add(lp);
                    this.dateLoaned = sDate;

                });
        return this.paymentSchedules;
    }
    
    private LocalDate getNextDate(){
        switch(this.multiplier) {
            case 1: this.dateLoaned = this.dateLoaned.plusMonths(1);
                break;
            case 2: this.dateLoaned = this.dateLoaned.plusDays(15);
                break;
            case 3: this.dateLoaned = this.dateLoaned.plusWeeks(1);
                break;
        }
        return this.dateLoaned;
    }

}
