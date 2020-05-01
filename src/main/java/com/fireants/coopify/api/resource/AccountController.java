/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.resource;

import com.fireants.coopify.api.model.Account;
import com.fireants.coopify.api.model.Borrower;
import com.fireants.coopify.api.model.Coop;
import com.fireants.coopify.api.model.Member;
import com.fireants.coopify.api.model.User;
import com.fireants.coopify.api.service.AccountService;
import com.fireants.coopify.api.service.BorrowerService;
import com.fireants.coopify.api.service.CoopService;
import com.fireants.coopify.api.service.MemberService;
import com.fireants.coopify.api.service.UserService;
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
@RequestMapping("/api/accounts")
public class AccountController {
   @Autowired
   private AccountService acctService;
   
   @Autowired
   private CoopService coopService;
   
   @Autowired
   private MemberService memberService;
   
   @Autowired
   private UserService userService;
   
   @Autowired
   private BorrowerService borrowerService;
   
   /** Gets all accounts
     * @return 
    */
   @GetMapping
   public Flux<Account> getALlAccounts() {
       return this.acctService.findAll();
   }
   
   /**
    * Gets Account by id
     * @param id
     * @return 
    */
   @GetMapping("{id}")
   public Mono<ResponseEntity<Account>> getAccountById(@PathVariable String id) {
       return this.acctService.findById(id)
               .map(acct -> ResponseEntity.ok().body(acct))
               .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
   }
   
   /**
    * Updates Account by id
     * @param id
     * @param acct
     * @return updated account
    */
   @PutMapping("{id}")
   public Mono<ResponseEntity<Account>> update(@PathVariable String id, @RequestBody Account acct) {
       return this.acctService.findById(id)
               .flatMap((Account a) -> {
                   return this.acctService.update(a.getId(), acct)
                           .map(ac -> ResponseEntity.ok().body(ac));
               })
               .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
   }
   
   /**
    * Creates new account
     * @param acct
     * @return account
    */
   @PostMapping
   @ResponseStatus(HttpStatus.CREATED)
   public Mono<Account> createAccount(@RequestBody Account acct) {
       return this.acctService.save(acct);
   }
   
   /**
    * Deletes All accounts
     * @return
    */
   @DeleteMapping
   public Mono<Void> deleteAllAccounts() {
       return this.acctService.deleteAll();
   }
   
   /**
    * Deletes account by id
     * @param id
     * @return
    */
   @DeleteMapping("{id}")
   public Mono<ResponseEntity<Void>> deleteAccountById(@PathVariable String id) {
       return this.acctService.findById(id)
               .flatMap((Account acct) -> {
                   return this.acctService.deleteById(acct.getId())
                        .then(Mono.just(ResponseEntity.ok().<Void>build()));
               })
               .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
   }
   
   /**
     * Gets list of coops by account 
     * @param accountId - Account Id
     * @return list of coops
    */
   @GetMapping("/{accountId}/coops")
   public Flux<Coop> getCoopsByAccount(@PathVariable String accountId) {
       return coopService.findByAccountId(accountId);
   }
   
   /**
     * Creates new coop
     * @param accountId
     * @param coop
     * @return 
     */
    @PostMapping("/{accountId}/coops")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Coop> saveCoop(@PathVariable String accountId, @RequestBody Coop coop) {
        coop.setAccountId(accountId);
        return this.coopService.save(coop);
    }
    
    /**
     * Gets list of members by account
     * @param accountId - must not be null
     * @return list of members
    */
   @GetMapping("/{accountId}/members")
   public Flux<Member> getMembers(@PathVariable String accountId) {
       return memberService.findByAccountId(accountId);
   }
   
   /**
     * Creates new member
     * @param accountId
     * @param member
     * @return 
     */
    @PostMapping("/{accountId}/members")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Account> addMember(@PathVariable String accountId, @RequestBody Member member) {
        return this.acctService.addMember(accountId, member);
    }
   
   /**
     * Creates new user
     * @param accountId
     * @param user
     * @return 
     */
    @PostMapping("/{accountId}/users")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Account> addUser(@PathVariable String accountId, @RequestBody User user) {
        return this.acctService.addUser(accountId, user);
    }
    
    /**
     * Gets list of users by account
     * @param accountId - must not be null
     * @return list of users
    */
   @GetMapping("/{accountId}/users")
   public Flux<User> getUsers(@PathVariable String accountId) {
       return this.userService.findByAccountId(accountId);
   }
   
   
   /**
     * Creates new borrower
     * @param accountId
     * @param borrower
     * @return 
     */
    @PostMapping("/{accountId}/borrowers")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Account> addBorrower(@PathVariable String accountId, @RequestBody Borrower borrower) {
        return this.acctService.addBorrower(accountId, borrower);
    }
    
    /**
     * Gets list of borrowers by account
     * @param accountId - must not be null
     * @return list of borrowers
    */
   @GetMapping("/{accountId}/borrowers")
   public Flux<Borrower> getBorrowers(@PathVariable String accountId) {
       return this.borrowerService.findByAccountId(accountId);
   }
   
}
