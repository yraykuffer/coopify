/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.model;

import com.mongodb.lang.NonNull;
import java.time.LocalDate;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author admin
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "contributions")
public class Contribution {
    @Id
    private String id;
    private Double amount;
    private LocalDate dateContributed;
    private LocalDate datePosted = LocalDate.now();
    @NonNull
    private String coopMemberId;
    private String coopId;
}
