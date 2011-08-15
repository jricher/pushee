package org.mitre.pushee.oauth.service;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mitre.pushee.oauth.model.ClientDetailsEntity;
import org.mitre.pushee.oauth.model.ClientDetailsEntityFactory;
import org.mitre.pushee.oauth.repository.OAuth2ClientRepository;
import org.mitre.pushee.oauth.repository.OAuth2TokenRepository;
import org.mitre.pushee.oauth.service.impl.DefaultOAuth2ClientDetailsEntityService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Unit test for DefaultOAuth2ClientDetailsEntityService
 * 
 * @author AANGANES
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/spring/application-context.xml", "classpath:test-context.xml"})
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
	public void loadClientbyId_valid() {
		ClientDetailsEntity client1 = new ClientDetailsEntity();
		client1.setClientId("1");
		
		expect(clientRepository.getClientById("1")).andReturn(client1).once();
		replay(clientRepository);
		
		ClientDetailsEntity client = entityService.loadClientByClientId("1");
		//TODO: if equality test includes more than id, add other attributes
		
		verify(clientRepository);
		
		assertThat(client, CoreMatchers.equalTo(client));
	}
	
	@Test(expected = InvalidClientException.class)
	public void loadClientbyId_invalidId() {
		expect(clientRepository.getClientById("badId")).andReturn(null).once();
		replay(clientRepository);
		
		entityService.loadClientByClientId("badId");
		
		verify(clientRepository);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void loadClientbyId_nullId() {
		entityService.loadClientByClientId(null);
	}
	
	@Test
	public void createClient_valid() {
		
		ClientDetailsEntity client = new ClientDetailsEntity();
		client.setClientId("5");
		client.setClientSecret("secret");
		
		expect(clientFactory.createClient("5", "secret")).andReturn(client).once();
		replay(clientFactory);
		
		ClientDetailsEntity newClient = entityService.createClient("5", "secret", new ArrayList<String>(), new ArrayList<String>(), "http://redirect.uri", new ArrayList<GrantedAuthority>(), "name", "desc", false, 1000000000L, 5000000000L, "owner");
		
		verify(clientFactory);
		
		assertThat(newClient.getClientId(), CoreMatchers.equalTo("5"));
	}
	
	@Test
	@Rollback
	public void deleteClient_valid() {
		ClientDetailsEntity client = new ClientDetailsEntity();
		client.setClientId("1");
		
		expect(clientRepository.getClientById("1"))
			.andReturn(client).once()
			.andReturn(null).once();
		replay(clientRepository);
		
		entityService.deleteClient(client);
		ClientDetailsEntity deleted = null;
		
		Boolean failed = false;
		try {
			deleted = entityService.loadClientByClientId("1");
		} catch (InvalidClientException e) {
			failed = true;
			assertThat(e.getMessage(), CoreMatchers.equalTo("Client with id 1 was not found"));
		} finally {
			if (!failed) {
				fail("Loading the client that was deleted should have thrown an exception");
			}
		}
		
		verify(clientRepository);
		assertThat(deleted, is(nullValue()));
	}
	
	@Test(expected = InvalidClientException.class) 
	public void deleteClient_invalid() {
		
		ClientDetailsEntity client = new ClientDetailsEntity();
		client.setClientId("badId");
		
		expect(clientRepository.getClientById("badId")).andReturn(null).once();
		replay(clientRepository);
		
		entityService.deleteClient(client);
		
		verify(clientRepository);
	}
	
	@Test
	@Rollback
	public void updateClient_valid() {
		ClientDetailsEntity original = new ClientDetailsEntity();
		original.setClientId("1");
		original.setClientName("Original");
		original.setClientSecret("originalSecret");
		
		ClientDetailsEntity modified = new ClientDetailsEntity();
		modified.setClientId("1");
		modified.setClientName("Modified");
		modified.setClientSecret("newSecret");
		
		expect(clientRepository.updateClient("1", modified)).andReturn(modified).once();
		replay(clientRepository);
		
		ClientDetailsEntity retrieved = entityService.updateClient(original, modified);
		
		verify(clientRepository);
		
		assertThat(retrieved, is(sameInstance(modified)));
		
	}
	
	@Test(expected = IllegalArgumentException.class)	
	public void updateClient_invalid() {
		entityService.updateClient(null, null);
	}
	
	@Test
	public void getAll() {
		ClientDetailsEntity client1 = new ClientDetailsEntity();
		client1.setClientId("1");
		
		ClientDetailsEntity client2 = new ClientDetailsEntity();
		client2.setClientId("2");
		
		ArrayList<ClientDetailsEntity> allClients = new ArrayList<ClientDetailsEntity>();
		allClients.add(client1);
		allClients.add(client2);
		
		expect(clientRepository.getAllClients()).andReturn(allClients).once();
		replay(clientRepository);
		
		ArrayList<ClientDetailsEntity> retrieved = (ArrayList<ClientDetailsEntity>) entityService.getAllClients();
		
		verify(clientRepository);
		
		assertThat(allClients, is(sameInstance(retrieved)));
	}
}
