/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.builder;

import com.fireants.coopify.api.model.Contribution;
import com.fireants.coopify.api.model.LoanPayment;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author admin
 */
public class BuilderTest {

    public static void main(String[] args) {
        List<LoanPayment> list = Schedules.<PaymentScheduleBuilder>builder(Builder.Type.PAYMENT)
                .amount(25000)
                .dateLoaned(LocalDate.now())
                .interestRate(8)
                .terms(4)
                .monthly()
                .build();
        list.stream().forEach(System.out::println);

        List<Contribution> list2 = Schedules.<ContributionScheduleBuilder>builder(Builder.Type.CONTRI)
                .heads(5)
                .perHead(500)
                .startDate(LocalDate.of(2019,1,1))
                .endDate(LocalDate.of(2019,6,30))
                .semiMontly()
                .build();
        list.stream().forEach(System.out::println);
        list2.stream().forEach(System.out::println);
    }
}
