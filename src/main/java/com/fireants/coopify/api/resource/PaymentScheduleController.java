/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.resource;

import com.fireants.coopify.api.model.PaymentSchedule;
import com.fireants.coopify.api.service.PaymentScheduleService;
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
@RequestMapping("api/payment-schedules")
public class PaymentScheduleController {
    @Autowired
   private PaymentScheduleService paymentScheduleService;
   
   
   /** Gets all payment schedules
     * @return 
    */
   @GetMapping
   public Flux<PaymentSchedule> getAllPaymentSchedule() {
       return this.paymentScheduleService.findAll();
   }
   
   /**
    * Gets PaymentSchedule by id
     * @param id
     * @return 
    */
   @GetMapping("{id}")
   public Mono<ResponseEntity<PaymentSchedule>> getPaymentScheduleById(@PathVariable String id) {
       return this.paymentScheduleService.findById(id)
               .map(acct -> ResponseEntity.ok().body(acct))
               .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
   }
   
   /**
    * Updates PaymentSchedule by id
     * @param id
     * @param sched
     * @return updated account
    */
   @PutMapping("{id}")
   public Mono<ResponseEntity<PaymentSchedule>> update(@PathVariable String id, @RequestBody PaymentSchedule sched) {
       return this.paymentScheduleService.findById(id)
               .flatMap((PaymentSchedule s) -> {
                   return this.paymentScheduleService.update(s.getId(), sched)
                           .map(ac -> ResponseEntity.ok().body(ac));
               })
               .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
   }
   
   /**
    * Creates new PaymentSchedule
     * @param sched
     * @return account
    */
   @PostMapping
   @ResponseStatus(HttpStatus.CREATED)
   public Mono<PaymentSchedule> createPaymentSchedule(@RequestBody PaymentSchedule sched) {
       return this.paymentScheduleService.save(sched);
   }
   
   /**
    * Deletes All payment schedules
     * @return
    */
   @DeleteMapping
   public Mono<Void> deleteAllPaymentSchedules() {
       return this.paymentScheduleService.deleteAll();
   }
   
   /**
    * Deletes payment schedule by id
     * @param id
     * @return
    */
   @DeleteMapping("{id}")
   public Mono<ResponseEntity<Void>> deletePaymentScheduleById(@PathVariable String id) {
       return this.paymentScheduleService.findById(id)
               .flatMap((PaymentSchedule sched) -> {
                   return this.paymentScheduleService.deleteById(sched.getId())
                        .then(Mono.just(ResponseEntity.ok().<Void>build()));
               })
               .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
   }
}
