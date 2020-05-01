/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.service;

import com.fireants.coopify.api.model.Account;
import com.fireants.coopify.api.model.Borrower;
import com.fireants.coopify.api.model.Coop;
import com.fireants.coopify.api.model.CoopMember;
import com.fireants.coopify.api.model.Member;
import com.fireants.coopify.api.model.User;
import com.fireants.coopify.api.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author admin
 */
@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private ReactiveMongoTemplate template;

    @Autowired
    private MemberService memberService;
    
    @Autowired
    private BorrowerService borrowerService;
    
    @Autowired
    private UserService userService;

    /**
     * Gets All Accounts
     *
     * @return all Accounts
     */
    public Flux<Account> findAll() {
        return this.accountRepo.findAll();
    }

    /**
     * Get Account By Id
     *
     * @param id
     * @return Account by id
     */
    public Mono<Account> findById(String id) {
        return this.accountRepo.findById(id);
    }

    /**
     * Saves new Account
     *
     * @param account
     * @return account
     */
    public Mono<Account> save(Account account) {
        return this.accountRepo.save(account);
    }

    /**
     * Updates existing account
     *
     * @param accountId
     * @param account
     * @return updated account
     */
    public Mono<Account> update(String accountId, Account account) {
        account.setId(accountId);
        return this.accountRepo.save(account);
    }

    /**
     * Deletes account by id
     *
     * @param id
     * @return Void
     */
    public Mono<Void> deleteById(String id) {
        return this.accountRepo.deleteById(id);
    }

    /**
     * Deletes all accounts
     *
     * @return
     */
    public Mono<Void> deleteAll() {
        return this.accountRepo.deleteAll();
    }
    
    /**
     * Returns account with added member
     * @param accountId
     * @param member
     * @return return account;
     */
    public Mono<Account> addMember(String accountId, Member member) {
        member.setAccountId(accountId);
        return this.memberService.save(member)
                .flatMap(m -> {
                    Update update = new Update();
                    update.push("members", m);
                    return template.findAndModify(Query.query(Criteria.where("id").is(accountId)), update, Account.class);
                });
    }
    
    /**
     * Returns account with added borrower
     * @param accountId
     * @param borrower
     * @return return account;
     */
    public Mono<Account> addBorrower(String accountId, Borrower borrower) {
        borrower.setAccountId(accountId);
        return this.borrowerService.save(borrower)
                .flatMap(m -> {
                    Update update = new Update();
                    update.push("members", m);
                    return template.findAndModify(Query.query(Criteria.where("id").is(accountId)), update, Account.class);
                });
    }
    
    /**
     * Returns account with added user
     * @param accountId
     * @param user
     * @return return account;
     */
    public Mono<Account> addUser(String accountId, User user) {
        user.setAccountId(accountId);
        return this.userService.save(user)
                .flatMap(m -> {
                    Update update = new Update();
                    update.push("members", m);
                    return template.findAndModify(Query.query(Criteria.where("id").is(accountId)), update, Account.class);
                });
    }
}
