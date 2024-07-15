package com.example.accounts.client;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.example.accounts.Account;

import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AccountWebTestClientTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void listAccounts_WebTestClient() {
        String url = "/accounts";

        webTestClient.get().uri(url)
                     .accept(MediaType.APPLICATION_JSON)
                     .exchange()
                     .expectStatus().isOk()
                     .expectHeader().contentType(MediaType.APPLICATION_JSON)
                     .expectBody(Account[].class)
                     .consumeWith(response -> {
                         Account[] accounts = response.getResponseBody();
                         assertThat(accounts.length >= 4).isTrue();
                         assertThat(accounts[0].getName()).isEqualTo("John Doe");
                     });
    }

    @Test
    public void getAccount_WebTestClient() {
        String url = "/accounts/{Id}";

        webTestClient.get()
                     .uri(url, 0)
                     .exchange()
                     .expectStatus().isOk()
                     .expectBody(Account.class)
                     .consumeWith(response -> {
                         Account account = response.getResponseBody();
                         assertThat(account.getName()).isEqualTo("John Doe");
                     });

    }

    @Test
    public void createAccount_WebTestClient() {
        String url = "/accounts";
        // use a unique number to avoid conflicts
        Long number = ThreadLocalRandom.current().nextLong(1000, 10000);
        Account account = new Account(number, "John Doe");
        
        webTestClient.post()
                     .uri(url)
                     .contentType(MediaType.APPLICATION_JSON)
                     .accept(MediaType.APPLICATION_JSON)
                     .body(Mono.just(account), Account.class)
                     .exchange()
                     .expectStatus().isCreated()
                     .expectHeader().value("Location", location -> {
            try {
                URI newAccountLocation = new URI(location);
                webTestClient.get()
                             .uri(newAccountLocation)
                             .exchange()
                             .expectStatus().isOk()
                             .expectBody(Account.class)
                             .consumeWith(response -> {
                                 Account retrievedAccount = response.getResponseBody();
                                 assertThat(retrievedAccount.getId()).isEqualTo(account.getId());
                                 assertThat(retrievedAccount.getName()).isEqualTo(account.getName());
                             });
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        });

    }

	@Test
	public void deleteAccount_WebTestClient() {

		String addUrl = "/accounts";

		webTestClient.post().uri(addUrl, 1)
					 .contentType(MediaType.APPLICATION_JSON)
					 .accept(MediaType.APPLICATION_JSON)
					 .body(Mono.just("{ \"id\": 999, \"name\": \"David\" }"), String.class)
					 .exchange()
					 .expectStatus().isCreated()
					 .expectHeader().value("Location", location -> {
			try {
				URI newBeneficiaryLocation = new URI(location);
				
				webTestClient.delete()
							 .uri(newBeneficiaryLocation)
							 .exchange()
							 .expectStatus().isNoContent();

			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		});

	}

}
