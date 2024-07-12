package com.example.controller;

import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.accounts.Account;
import com.example.accounts.AccountManager;


@RestController
public class AccountController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private AccountManager accountManager;

	/**
	 * Creates a new AccountController with a given account manager.
	 */
	@Autowired
	public AccountController(AccountManager accountManager) {
		this.accountManager = accountManager;
	}

    /**
	 * Provide a list of all accounts.
	 */
	@GetMapping(value = "/accounts")
	public List<Account> accountSummary() {
		return accountManager.getAll();
	}

	/**
	 * Provide the details of an account with the given id.
	 */
	@GetMapping(value = "/accounts/{id}")
	public Account userDetails(@PathVariable int id) {
		return accountManager.retrieve(id);
	}

	/**
	 * Creates a new Account, setting its URL as the Location header on the
	 * response.
	 */
	@PostMapping(value = "/accounts")
	public ResponseEntity<Void> createAccount(@RequestBody Account newAccount) {
		Account account = accountManager.save(newAccount);
		return entityWithLocation(account.getId());
    }


    /**
	 * Creates a new Account, setting its URL as the Location header on the
	 * response.
	 */
	@DeleteMapping(value = "/accounts/id")
	public void deleteAccount(@PathVariable int id) {
		Account account = accountManager.retrieve(id);
		accountManager.delete(account);

    }

	/**
	 * Return a response with the location of the new resource.
	 *
	 * Suppose we have just received an incoming URL of, say,
	 * http://localhost:8080/accounts and resourceId
	 * is "12345". Then the URL of the new resource will be
	 * http://localhost:8080/accounts/12345.
	 */
	private ResponseEntity<Void> entityWithLocation(Object resourceId) {

		// Determines URL of child resource based on the full URL of the given
		// request, appending the path info with the given resource Identifier
		// URI location = ServletUriComponentsBuilder
		// 		.fromCurrentRequestUri()
		// 		.path("/{resourceId}")
		// 		.buildAndExpand(resourceId)
		// 		.toUri();

		// Return an HttpEntity object - it will be used to build the
		// HttpServletResponse
		return ResponseEntity.created(URI.create("null")).build();
	}

}
