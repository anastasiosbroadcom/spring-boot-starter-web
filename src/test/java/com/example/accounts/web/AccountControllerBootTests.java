package com.example.accounts.web;

import com.example.accounts.Account;
import com.example.accounts.AccountManager;
import com.example.controller.AccountController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * These tests run the AccountController using the MockMVC framework.
 * The server does not need to be running.
 */
@WebMvcTest(AccountController.class) // WebMvcTest = MockMvc, @MockBean
public class AccountControllerBootTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountManager accountManager;

    @Test
    public void accountDetails() throws Exception {

        // arrange
        given(accountManager.retrieve(anyLong()))
				.willReturn(new Account(111L, "Test Doe"));

        // act and assert
        mockMvc.perform(get("/accounts/111"))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("name").value("Test Doe"))
               .andExpect(jsonPath("id").value("111"));

        // verify
        verify(accountManager).retrieve(anyLong());

    }

    @Test
    public void accountDetailsFail() throws Exception {

        given(accountManager.retrieve(any(Long.class)))
                .willThrow(new IllegalArgumentException("No such account with id " + 0L));

        mockMvc.perform(get("/accounts/9999"))
               .andExpect(status().isNotFound());

        verify(accountManager).retrieve(any(Long.class));

    }

    @Test
    public void accountSummary() throws Exception {

        List<Account> testAccounts = Arrays.asList(new Account(1234567890L, "John Doe"));
        given(accountManager.getAll())
				.willReturn(testAccounts);

        mockMvc.perform(get("/accounts"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$..id").value(1234567890))
               .andExpect(jsonPath("$..name").value("John Doe"));

        verify(accountManager).getAll();

    }

    @Test
    public void createAccount() throws Exception {

        Account testAccount = new Account(21L, "Mary Jones");
        
        given(accountManager.save(any(Account.class)))
				.willReturn(testAccount);

        mockMvc.perform(post("/accounts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(testAccount)))
               .andExpect(status().isCreated())
               .andExpect(header().string("Location", "http://localhost/accounts/21"));

        verify(accountManager).save(any(Account.class));

    }

    @Test
    public void deleteAccount() throws Exception {

        Account account = new Account(1234567890L, "John Doe");
        given(accountManager.retrieve(anyLong())).willReturn(account);

        mockMvc.perform(delete("/accounts/{id}", 1234567890L))
               .andExpect(status().isNoContent());

        verify(accountManager).retrieve(anyLong());

    }

    @Test
    public void deleteAccountFail() throws Exception {
        given(accountManager.retrieve(anyLong())).willReturn(null);

        mockMvc.perform(delete("/accounts/{id}", 150L))
               .andExpect(status().isNotFound());

        verify(accountManager).retrieve(anyLong());
    }

    protected static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
