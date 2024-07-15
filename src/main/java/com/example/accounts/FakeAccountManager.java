package com.example.accounts;

import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;
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
    public Account save(Account newAccount) {
        accounts.add(newAccount);
        return newAccount;
    }


    @Override
    public Account retrieve(long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'retrieve'");
    }


    @Override
    public void delete(Account account) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

}
