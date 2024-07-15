package com.example.accounts.client;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
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
	public void createAccount() {
		String url = "/accounts";
		// use a unique number to avoid conflicts
		Long number = ThreadLocalRandom.current().nextLong(1000, 10000);
		Account account = new Account(number, "John Doe");
		URI newAccountLocation = restTemplate.postForLocation(url, account);
		
		//TODO: assert with account retrieval

		Account[] accounts = restTemplate.getForObject(url, Account[].class);
		assertThat(accounts.length >= 5).isTrue();
		assertThat(accounts[0].getName()).isEqualTo("John Doe");
	}

}
