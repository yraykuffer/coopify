/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.repository;

import com.fireants.coopify.api.model.Account;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 *
 * @author admin
 */
public interface AccountRepository extends ReactiveMongoRepository<Account, String>{
    
}
