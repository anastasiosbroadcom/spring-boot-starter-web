package com.example.accounts;

import java.util.List;

public interface AccountManager {

    List<Account> getAll();

    Account retrieve(int id);

    Account save(Account newAccount);

    void delete(Account account);

}
