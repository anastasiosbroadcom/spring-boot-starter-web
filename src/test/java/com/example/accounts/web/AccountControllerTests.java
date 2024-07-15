package com.example.accounts.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.accounts.Account;
import com.example.accounts.FakeAccountManager;
import com.example.controller.AccountController;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

// TODO implement retrive single account
// TODO implement delete single account with success and failure
// TODO implement update single account

/**
 * A JUnit test case testing the AccountController.
 */
public class AccountControllerTests {

	private AccountController controller;

	@BeforeEach
	public void setUp() throws Exception {
		controller = new AccountController(new FakeAccountManager());
	}

	@Test
	public void accountSummary() {
		List<Account> accounts = controller.accountSummary();
		assertThat(accounts).isNotNull();
		assertThat(accounts.size()).isEqualTo(4);
		assertThat(accounts.get(0).getId()).isEqualTo(Long.valueOf(0));
	}

	@Test
	public void createAccount() {
		Account newAccount = new Account(11223344L, "Test");

		// ServletUriComponentsBuilder expects to find the HttpRequest in the
		// current thread (Spring MVC does this for you). For our test, we need
		// to add a mock request manually
		setupFakeRequest("http://localhost/accounts");

		HttpEntity<?> result = controller.createAccount(newAccount);
		assertThat(result).isNotNull();

		assertThat(result.getHeaders().getLocation().toString()).isEqualTo("http://localhost/accounts/11223344");
		//TODO: include asserts of new created single account retrieval
	}

	/**
	 * Add a mocked up HttpServletRequest to Spring's internal request-context
	 * holder. Normally the DispatcherServlet does this, but we must do it
	 * manually to run our test.
	 *
	 * @param url
	 *            The URL we are creating the fake request for.
	 */
	private void setupFakeRequest(String url) {
		String requestURI = url.substring(16); // Drop "http://localhost"

		// We can use Spring's convenient mock implementation. Defaults to
		// localhost in the URL. Since we only need the URL, we don't need
		// to setup anything else in the request.
		MockHttpServletRequest request = new MockHttpServletRequest("POST", requestURI);

		// Puts the fake request in the current thread for the
		// ServletUriComponentsBuilder to initialize itself from later.
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
	}

}

