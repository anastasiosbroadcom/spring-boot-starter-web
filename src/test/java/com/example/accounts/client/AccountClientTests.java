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
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AccountClientTests {

	@Autowired
	private TestRestTemplate restTemplate;

	private Random random = new Random();
	
	@Test 
	public void listAccounts() {
		String url = "/accounts";
		// we have to use Account[] instead of List<Account>, or Jackson won't know what type to unmarshal to
		Account[] accounts = restTemplate.getForObject(url, Account[].class);
		assertThat(accounts.length >= 21).isTrue();
		assertThat(accounts[0].getName()).isEqualTo("Keith and Keri Donald");
	}
	
	@Test
	public void getAccount() {
		String url = "/accounts/{accountId}";
		Account account = restTemplate.getForObject(url, Account.class, 0); 
		assertThat(account.getName()).isEqualTo("Keith and Keri Donald");
	}
	
	@Test
	public void createAccount() {
		String url = "/accounts";
		// use a unique number to avoid conflicts
		Long number = ThreadLocalRandom.current().nextLong(10000);
		Account account = new Account(number, "John Doe");
		URI newAccountLocation = restTemplate.postForLocation(url, account);
		
		Account retrievedAccount = restTemplate.getForObject(newAccountLocation, Account.class);
		assertThat(retrievedAccount.getId()).isNotNull();
	}

	@Test
	public void addAndDeleteBeneficiary() {

		String addUrl = "/accounts/{accountId}/beneficiaries";
		URI newBeneficiaryLocation = restTemplate.postForLocation(addUrl, "David", 1);
		Beneficiary newBeneficiary = restTemplate.getForObject(newBeneficiaryLocation, Beneficiary.class);
		assertThat(newBeneficiary.getName()).isEqualTo("David");

		restTemplate.delete(newBeneficiaryLocation);

		ResponseEntity<Beneficiary> response =
				restTemplate.getForEntity(newBeneficiaryLocation, Beneficiary.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

}
