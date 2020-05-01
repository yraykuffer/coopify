/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.builder;

import com.fireants.coopify.api.model.Contribution;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin
 */
public class ContributionScheduleBuilder implements Builder<Contribution> {

    private int heads = 1;
    private double perHead;
    private LocalDate startDate;
    private LocalDate endDate;
    private DayOfWeek day;
    private boolean isWeekly;
    private boolean isMonthly;
    private boolean isSemiMonthly;
    private int day1;
    private int day2;
    private final List<Contribution> contriList = new ArrayList<>();

    public ContributionScheduleBuilder heads(int heads) {
        this.heads = heads;
        return this;
    }

    public ContributionScheduleBuilder perHead(double perHead) {
        this.perHead = perHead;
        return this;
    }

    public ContributionScheduleBuilder startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public ContributionScheduleBuilder endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public ContributionScheduleBuilder weekly() {
        this.day = DayOfWeek.SATURDAY;
        this.isWeekly = true;
        return this;
    }

    public ContributionScheduleBuilder weekly(DayOfWeek day) {
        this.day = day;
        this.isWeekly = true;
        return this;
    }

    public ContributionScheduleBuilder semiMontly() {
        this.isSemiMonthly = true;
        this.day1 = 5;
        this.day2 = 20;
        return this;
    }

    public ContributionScheduleBuilder semiMontly(int day1, int day2) {
        this.isSemiMonthly = true;
        this.day1 = day1;
        this.day2 = day2;
        return this;
    }

    public ContributionScheduleBuilder monthly() {
        this.isMonthly = true;
        return this;
    }

    @Override
    public List<Contribution> build() {
        return this.createSchedules();
    }

    private List<Contribution> createSchedules() {
        if (this.isWeekly) {
            this.startDate = this.startDate.withDayOfMonth(1).with(this.day);
        }
        int i = 1;
        while (this.startDate.isBefore(this.endDate)) {
            Contribution c = new Contribution();
            c.setId(i + "");
            c.setDateContributed(this.startDate);
            c.setAmount(this.perHead * this.heads);
            contriList.add(c);
            if (this.isWeekly) {
                this.startDate = this.startDate.plusWeeks(1);
            } else if (this.isMonthly) {
                this.startDate = this.startDate.plusMonths(1);
            } else if (this.isSemiMonthly) {
                this.startDate = this.startDate.plusDays(15);
            }
            i++;

        }
        return this.contriList;
    }

}
