package com.example.accounts.service;

import org.junit.jupiter.api.Test;

import com.example.accounts.Account;
import com.example.accounts.FakeAccountManager;

import io.netty.util.internal.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

public class FakeAccountManagerTests {

    private FakeAccountManager accountManager;


    @BeforeEach
    public void SetUp(){
        accountManager = new FakeAccountManager();
    }

    @Test
    public void testRetrieveAllAccounts(){
            var accounts = accountManager.getAll();
            assertEquals(accounts.size(), 4);
    }


    @Test
    public void testRetrievAccount(){
        Long accountId = 0L;
        var account = accountManager.retrieve(accountId);
        
        assertNotNull(account);
        assertEquals(account.getName(), "John Doe");

    }

    @Test
    public void testAddNewAccount(){
        Long number = ThreadLocalRandom.current().nextLong(1000, 10000);
        Account newAccount = new Account(number, "New Account");
        
        accountManager.save(newAccount);
        
        assertEquals(accountManager.getAll().size(), 5);
        assertNotNull(accountManager.retrieve(number));
        assertEquals(accountManager.retrieve(number).getName(), "New Account");
    }


    @Test
    public void testDeleteAccount(){
        Long accountId = 0L;
        var account = accountManager.retrieve(accountId);

        accountManager.delete(account);
        
        assertEquals(accountManager.getAll().size(), 3);
    }

    @Test
    public void testUpdateAccount(){
        Long accountId = 0L;
        var account = accountManager.retrieve(accountId);
        account.setEmail("johndoe@email.com");

        accountManager.save(account);
        

        var updatedAccount = accountManager.retrieve(accountId);
        assertEquals(updatedAccount.getEmail(), "johndoe@email.com");
    }



}
