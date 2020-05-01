/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.service;

import com.fireants.coopify.api.model.Settings;
import com.fireants.coopify.api.repository.SettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author admin
 */
@Service
public class SetttingsService {
    @Autowired
    private SettingsRepository repo;
    
    /**
     * Gets All settings
     * @return settings
     */
    public Flux<Settings> findAll() {
        return this.repo.findAll();
    }
    
    /**
     * Gets Settings By Id
     * @param id
     * @return Settings
     */
    public Mono<Settings> findById(String id) {
        return this.repo.findById(id);
    }
    
    /**
     * Gets Settings by coopId specified
     * @param coopId
     * @return Settings
     */
    public Mono<Settings> findByCoopId(String coopId) {
        return this.repo.findByCoopId(coopId);
    }
    
    /**
     * Adds Settings
     * @param s
     * @return Settings
     */
    public Mono<Settings> save(Settings s) {
        return this.repo.save(s);
    }
    
    /**
     * Updates existing Settings
     * @param id
     * @param s
     * @return updated Settings
     */
    public Mono<Settings> update(String id, Settings s) {
        s.setId(id);
        return this.repo.save(s);
    }
    
    /**
     * Deletes Settings by id
     * @param id
     * @return 
     */
    public Mono<Void> deleteById(String id) {
        return this.repo.deleteById(id);
    }
    
    /**
     * Deletes all Settings
     * @return 
     */
    public Mono<Void> deleteAll() {
        return this.repo.deleteAll();
    }
}
