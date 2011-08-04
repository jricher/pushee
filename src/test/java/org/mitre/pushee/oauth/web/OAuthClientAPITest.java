package org.mitre.pushee.oauth.web;

import static org.easymock.EasyMock.createNiceMock;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mitre.pushee.hub.exception.ClientNotFoundException;
import org.mitre.pushee.oauth.exception.DuplicateClientIdException;
import org.mitre.pushee.oauth.service.ClientDetailsEntityService;

/**
 * Unit test for OAuthClientAPI
 * 
 * @author AANGANES
 *
 */
public class OAuthClientAPITest {

	private ClientDetailsEntityService clientService;
	private OAuthClientAPI clientAPI;
	
	@Before
	public void setup() {
		clientService = createNiceMock(ClientDetailsEntityService.class);
		clientAPI = new OAuthClientAPI(clientService);
	}
	
	@Test
	@Ignore
	public void addClient_valid() {
		
	}
	
	@Test(expected = DuplicateClientIdException.class)
	@Ignore 
	public void addClient_duplicateClient() {
		
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
	public void getAll() {
		
	}
	
	@Test
	@Ignore
	public void updateClient_valid() {
		
	}
	
	@Test(expected = ClientNotFoundException.class)
	@Ignore
	public void updateClient_invalidClient() {
		
	}
	
	@Test
	@Ignore
	public void getClientById_valid() {
		
	}
	
	@Test(expected = ClientNotFoundException.class)
	@Ignore
	public void getClientById_invalid() {
		
	}
	
}
