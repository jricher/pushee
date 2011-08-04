package org.mitre.pushee.oauth.service;

import static org.easymock.EasyMock.createNiceMock;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mitre.pushee.hub.exception.ClientNotFoundException;
import org.mitre.pushee.oauth.model.ClientDetailsEntityFactory;
import org.mitre.pushee.oauth.repository.OAuth2ClientRepository;
import org.mitre.pushee.oauth.repository.OAuth2TokenRepository;
import org.mitre.pushee.oauth.service.impl.DefaultOAuth2ClientDetailsEntityService;

/**
 * Unit test for DefaultOAuth2ClientDetailsEntityService
 * 
 * @author AANGANES
 *
 */
public class ClientDetailsEntityServiceTest {

	private OAuth2ClientRepository clientRepository;
	private OAuth2TokenRepository tokenRepository;
	private ClientDetailsEntityFactory clientFactory;
	private DefaultOAuth2ClientDetailsEntityService entityService;
	
	@Before
	public void setup() {
		clientRepository = createNiceMock(OAuth2ClientRepository.class);
		tokenRepository = createNiceMock(OAuth2TokenRepository.class);
		clientFactory = createNiceMock(ClientDetailsEntityFactory.class);
		entityService = new DefaultOAuth2ClientDetailsEntityService(clientRepository, tokenRepository, clientFactory);
	}
	
	@Test
	@Ignore
	public void loadClientbyId_valid() {
		
	}
	
	@Test(expected = ClientNotFoundException.class)
	@Ignore
	public void loadClientbyId_invalid() {
		
	}
	
	@Test
	@Ignore
	public void createClient_valid() {
		
	}
	
	@Test
	@Ignore
	public void deleteClient_valid() {
		
	}
	
	@Test(expected = ClientNotFoundException.class) 
	@Ignore
	public void deleteClient_invalid() {
		
	}
	
	@Test
	@Ignore
	public void updateClient_valid() {
		
	}
	
	@Test
	@Ignore
	public void updateClient_invalid() {
		
	}
	
	@Test
	@Ignore
	public void getAll() {
		
	}
}
