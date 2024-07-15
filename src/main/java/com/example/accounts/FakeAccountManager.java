package com.example.accounts;

import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class FakeAccountManager implements AccountManager {

    private List<Account> accounts = new LinkedList<Account>(Arrays.asList(
        new Account(0L, "John Doe"),
        new Account(1L, ""),
        new Account(99L, ""),
        new Account(100L, "")
    ));

    @Override
    public List<Account> getAll() {
        return accounts;
    }

    @Override
    public Account retrieve(long id) {
        Optional<Account> account = accounts.stream().filter(c -> c.getId().equals(id)).findFirst();
        if (account.isPresent())
            return account.get();
        
        return null;
    }

    @Override
    public Account save(Account newAccount) {
        Account retrievedAccount = retrieve(newAccount.getId());
        if(retrievedAccount == null){
            accounts.add(newAccount);
        } else{
            retrievedAccount.setEmail(newAccount.getEmail());
            retrievedAccount.setName(newAccount.getName());
            retrievedAccount.setPassword(newAccount.getPassword());
            retrievedAccount.setStatus(newAccount.getStatus());
        }
        
        return retrieve(newAccount.getId());
    }

    @Override
    public void delete(Account account) {
        Account retrievedAccount = retrieve(account.getId());
        if(retrievedAccount != null){
            accounts.remove(account);
        } else{
            throw new NullPointerException(String.format("Account with id s% not found!", account.getId()));
        }
    }

}
