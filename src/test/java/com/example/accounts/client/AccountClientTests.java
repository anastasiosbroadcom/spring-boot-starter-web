package com.example.accounts.client;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.accounts.Account;

import java.net.URI;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AccountClientTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test 
	public void listAccounts() {
		String url = "/accounts";
		// we have to use Account[] instead of List<Account>, or Jackson won't know what type to unmarshal to
		Account[] accounts = restTemplate.getForObject(url, Account[].class);
		assertThat(accounts.length >= 4).isTrue();
		assertThat(accounts[0].getName()).isEqualTo("John Doe");
	}
	
	@Test
	public void getAccount() {
		String url = "/accounts/{id}";
		Account account = restTemplate.getForObject(url, Account.class, 0); 
		assertThat(account.getName()).isEqualTo("John Doe");
	}
	
	@Test
	public void createAccount() {
		String url = "/accounts";
		// use a unique number to avoid conflicts
		Long number = ThreadLocalRandom.current().nextLong(1000, 10000);
		Account account = new Account(number, "John Doe");
		URI newAccountLocation = restTemplate.postForLocation(url, account);
		
		Account retrievedAccount = restTemplate.getForObject(newAccountLocation, Account.class);
		assertThat(retrievedAccount.getId()).isNotNull();
	}

	@Test
	public void deleteAccount() {

		String addUrl = "/accounts";
		Account account = new Account(10001L, "David");
		URI newAccountLocation = restTemplate.postForLocation(addUrl, account);
		Account newAccount = restTemplate.getForObject(newAccountLocation, Account.class);
		assertThat(newAccount.getName()).isEqualTo("David");

		restTemplate.delete(newAccountLocation);

		ResponseEntity<Account> response =
				restTemplate.getForEntity(newAccountLocation, Account.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

}
