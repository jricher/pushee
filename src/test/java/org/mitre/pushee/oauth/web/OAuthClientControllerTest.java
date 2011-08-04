package org.mitre.pushee.oauth.web;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mitre.pushee.oauth.service.ClientDetailsEntityService;
import org.mitre.pushee.oauth.service.OAuth2TokenEntityService;
import org.mitre.pushee.oauth.web.OAuthClientController;

/**
 * Unit test for OauthClientController
 * 
 * @author AANGANES
 *
 */
public class OAuthClientControllerTest {

	private ClientDetailsEntityService clientService;
	private OAuth2TokenEntityService tokenService;
	private OAuthClientController controller;
	
	@Before
	public void setup() {
		clientService = createNiceMock(ClientDetailsEntityService.class);
		tokenService = createNiceMock(OAuth2TokenEntityService.class);
		controller = new OAuthClientController(clientService, tokenService);
	}
	
	@Test
	@Ignore
	public void viewAll() {
		
	}
	
	@Test
	@Ignore
	public void addClient() {
		
	}
	
	@Test
	@Ignore
	public void deleteClient_valid() {
		
	}
	
	@Test
	@Ignore
	public void deleteClient_invalid() {
		
	}
	
	@Test
	@Ignore
	public void editClient_valid() {
		
	}
	
	@Test
	@Ignore
	public void editClient_invalid() {
		
	}
	
	@Test
	@Ignore
	public void viewDetails_valid() {
		
	}
	
	@Test
	@Ignore
	public void viewDetails_invalid() {
		
	}
	
}
