/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.resource;

import com.fireants.coopify.api.model.Penalty;
import com.fireants.coopify.api.model.Settings;
import com.fireants.coopify.api.service.PenaltyService;
import com.fireants.coopify.api.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author admin
 */
@RestController
@RequestMapping("/api/settings")
public class SetttingsController {
    @Autowired
    private SettingsService settingsService;
    
    /**
     * Gets All Settings
     * @return all Settings
     */
    @GetMapping
    public Flux<Settings> findAll() {
        return this.settingsService.findAll();
    }
    
    /**
     * Gets Settings by id
     * @param id - must not be null
     * @return Settings
     */
    @GetMapping("{id}")
    public Mono<ResponseEntity<Settings>> getById(@PathVariable String id) {
        return this.settingsService.findById(id)
                .map(c -> ResponseEntity.ok(c))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
    
    /**
     * Deletes Settings by id
     * @param id - must not be null
     * @return 
     */
    @DeleteMapping("{id}")
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable String id) {
        return this.settingsService.findById(id)
                .flatMap((Settings s) -> {
                    return this.settingsService.deleteById(s.getId())
                            .then(Mono.just(ResponseEntity.ok().<Void>build()));
                })
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
    
    /**
     * Adds Settings
     * @param s - must not be null
     * @return Loan
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Settings> save(@RequestBody Settings s){
        return this.settingsService.save(s);
    }
    
    /**
     * Updates existing Settings
     * @param settings - must not be null
     * @param id - must not be null
     * @return 
     */
    @PutMapping("{id}")
    public Mono<ResponseEntity<Settings>> update(@RequestBody Settings settings, @PathVariable String id) {
        return this.settingsService.findById(id)
                .flatMap((Settings s) -> {
                    return this.settingsService.update(s.getId(), settings)
                            .map((c) -> ResponseEntity.ok(c));
                })
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
}
