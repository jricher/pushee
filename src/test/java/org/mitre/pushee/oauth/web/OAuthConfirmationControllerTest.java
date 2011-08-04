package org.mitre.pushee.oauth.web;

import static org.easymock.EasyMock.createNiceMock;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mitre.pushee.hub.exception.ClientNotFoundException;
import org.mitre.pushee.oauth.service.ClientDetailsEntityService;
import org.springframework.security.oauth2.provider.verification.ClientAuthenticationCache;

public class OAuthConfirmationControllerTest {

	private ClientDetailsEntityService clientService;
	private ClientAuthenticationCache authenticationCache;
	private OAuthConfirmationController controller;
	
	@Before
	public void setup() {
		clientService = createNiceMock(ClientDetailsEntityService.class);
		authenticationCache = createNiceMock(ClientAuthenticationCache.class);
		
		controller = new OAuthConfirmationController(clientService, authenticationCache);
	}
	
	@Test
	@Ignore
	public void confirmAccess_valid() {
		
	}
	
	@Test(expected = ClientNotFoundException.class)
	@Ignore
	public void confirmAccess_invalidClient() {
		
	}
	
	@Test(expected = IllegalStateException.class)
	@Ignore
	public void confirmAccess_invalidAuth() {
		
	}
	
}
