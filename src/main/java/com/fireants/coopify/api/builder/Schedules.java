/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.builder;

/**
 *
 * @author admin
 */
public class Schedules {
    private Schedules(){}
    public static <T extends Builder> T builder(Builder.Type type) {
        if (type == Builder.Type.PAYMENT) {
            return (T) new PaymentScheduleBuilder();
        } else {
            return (T) new ContributionScheduleBuilder();
        }
        
    }
}
