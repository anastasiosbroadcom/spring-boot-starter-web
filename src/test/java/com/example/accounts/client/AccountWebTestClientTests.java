package com.example.accounts.client;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.example.accounts.Account;

import reactor.core.publisher.Mono;

import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.*;

// TODO implement retrive single account
// TODO implement delete single account with success and failure
// TODO implement update single account
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
                
                //TODO: replace with single account retrieval
                 webTestClient.get().uri(url)
                     .accept(MediaType.APPLICATION_JSON)
                     .exchange()
                     .expectStatus().isOk()
                     .expectHeader().contentType(MediaType.APPLICATION_JSON)
                     .expectBody(Account[].class)
                     .consumeWith(response -> {
                         Account[] accounts = response.getResponseBody();
                         assertThat(accounts.length >= 5).isTrue();
                     });
            
        });

    }


}
