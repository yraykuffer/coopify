/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.util;

import com.fireants.coopify.api.constants.InterestMode;
import com.fireants.coopify.api.model.LoanPayment;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin
 */
public class PaymentScheduleBuilder<T> {
    private List<T> list = new ArrayList<>();
    
    public static void main(String[] args) {
        List<LoanPayment> list = PaymentSchedule.builder(5000, LocalDate.now())
                .terms(6)
                .paymentMode("MONTHLY")
                .interestRate(8)
                .interestMode(InterestMode.FIXED)
                .build();
        list.stream().forEach(System.out::println);
    }
}
