/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.model;

import com.fireants.coopify.api.constants.ContributionMode;
import com.fireants.coopify.api.constants.InterestMode;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author admin
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "settings")
public class Settings {
    @Id
    private String id;
    private double perHead;
    private double interestRate;
    private int duration; // in months
    private InterestMode interestMode = InterestMode.FIXED; // Fixed or Deminishing
    private Date startDate;
    private Date endDate;
    private ContributionMode mode; // weekly, bimonthly, monthly;
    @Indexed(unique = true)
    private String coopId;
}
