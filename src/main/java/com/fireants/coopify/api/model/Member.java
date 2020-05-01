/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.model;

import com.mongodb.lang.NonNull;
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
@Document(collection = "members")
public class Member {
    @Id
    private String id;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    private String address;
    private String emailAddress;
    @NonNull
    private String contactNumber;
    @NonNull
    private String accountId;
}
