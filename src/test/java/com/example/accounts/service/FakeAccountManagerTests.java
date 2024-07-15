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
    public void testAddNewAccount(){
        Long number = ThreadLocalRandom.current().nextLong(1000, 10000);
        Account newAccount = new Account(number, "New Account");
        
        accountManager.save(newAccount);
        
        assertEquals(accountManager.getAll().size(), 5);
    }

}
