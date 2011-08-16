package org.mitre.pushee.oauth.web;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mitre.pushee.hub.exception.ClientNotFoundException;
import org.mitre.pushee.oauth.exception.DuplicateClientIdException;
import org.mitre.pushee.oauth.model.ClientDetailsEntity;
import org.mitre.pushee.oauth.service.ClientDetailsEntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.ModelAndView;

/**
 * Unit test for OAuthClientAPI
 * 
 * @author AANGANES
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/spring/application-context.xml", "classpath:test-context.xml"})
public class OAuthClientAPITest {

	private ClientDetailsEntityService clientService;
	private OAuthClientAPI clientAPI;
	
	
	private ClientDetailsEntity client;
	private ArrayList<GrantedAuthority> authorities;
	private static final Logger logger = LoggerFactory.getLogger(OAuthClientAPITest.class);
	
	@Before
	public void setup() {
		clientService = createNiceMock(ClientDetailsEntityService.class);
		clientAPI = new OAuthClientAPI(clientService);
		
		authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new GrantedAuthorityImpl("root"));
		authorities.add(new GrantedAuthorityImpl("42"));
		
		client = new ClientDetailsEntity();
		client.setClientId("1");
		client.setClientSecret("secret");
		client.setClientName("name");
		client.setOwner("");
		client.setClientDescription("description");
		client.setAccessTokenTimeout(0L);
		client.setAllowRefresh(false);
		client.setRefreshTokenTimeout(0L);
		client.setAuthorities(authorities);
		client.setAuthorizedGrantTypes(new ArrayList<String>());
		client.setScope(new ArrayList<String>());
		client.setWebServerRedirectUri("http://redirect.uri.com");
	}
	
	@Test
	@Ignore //The createClient call is not being mocked correctly?
	public void addClient_valid() {
		expect(clientService.loadClientByClientId("1")).andReturn(null).once();
		expect(clientService.createClient("1", "secret", new ArrayList<String>(), new ArrayList<String>(), 
				"http://redirect.uri.com", authorities, 
				"name", "description", false, 0L, 0L, "")).andReturn(client).once();
		replay(clientService);
		
		ModelAndView result = clientAPI.apiAddClient(new ModelAndView(), "1", "secret", "", "", 
				"http://redirect.uri.com", "root 42", 
				"name", "description", false, 0L, 0L, "");
		
		verify(clientService);
		logger.info("Created client = " + (ClientDetailsEntity)result.getModel().get("entity"));
		assertThat((ClientDetailsEntity)result.getModel().get("entity"), is(sameInstance(client)));
		assertThat(result.getViewName(), CoreMatchers.equalTo("jsonOAuthClientView"));
	}
	
	@Test(expected = DuplicateClientIdException.class)
	public void addClient_duplicateClient() {
		expect(clientService.loadClientByClientId("1")).andReturn(client).once();
		replay(clientService);
		clientAPI.apiAddClient(new ModelAndView(), "1", "secret", "", "", 
				"http://redirect.uri.com", "root 42", 
				"name", "description", false, 0L, 0L, "");
		verify(clientService);
	}
	
	@Test
	public void deleteClient_valid() {
		expect(clientService.loadClientByClientId("1")).andReturn(client).once();
		replay(clientService);
		
		ModelAndView result = clientAPI.apiDeleteClient(new ModelAndView(), "1");
		
		verify(clientService);
		assertThat(result.getViewName(), CoreMatchers.equalTo("management/successfullyRemoved"));
	}
	
	@Test(expected = ClientNotFoundException.class)
	public void deleteClient_invalid() {
		expect(clientService.loadClientByClientId("1")).andReturn(null).once();
		replay(clientService);
		
		clientAPI.apiDeleteClient(new ModelAndView(), "1");
		
		verify(clientService);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void getAll() {
		ClientDetailsEntity client1 = new ClientDetailsEntity();
		client1.setClientId("1");
		
		ClientDetailsEntity client2 = new ClientDetailsEntity();
		client2.setClientId("2");
		
		ArrayList<ClientDetailsEntity> allClients = new ArrayList<ClientDetailsEntity>();
		allClients.add(client1);
		allClients.add(client2);
		
		expect(clientService.getAllClients()).andReturn(allClients).once();
		replay(clientService);
		
		ModelAndView result = clientAPI.apiGetAllClients(new ModelAndView());
		
		verify(clientService);
		assertThat((ArrayList<ClientDetailsEntity>)result.getModel().get("entity"), CoreMatchers.equalTo(allClients));
		assertThat(result.getViewName(), CoreMatchers.equalTo("jsonOAuthClientView"));
	}
	
	@Test
	@Rollback
	public void updateClient_valid() {
		ClientDetailsEntity original = new ClientDetailsEntity();
		original.setClientId("1");
		original.setClientName("Original");
		original.setAuthorities(new ArrayList<GrantedAuthority>());
		original.setAllowRefresh(true);
		original.setAccessTokenTimeout(50000L);
		original.setRefreshTokenTimeout(4000000L);
		
		ClientDetailsEntity modified = new ClientDetailsEntity();
		modified.setClientId("1");
		modified.setClientName("Modified");
		modified.setAllowRefresh(false);
		modified.setAuthorities(authorities);
		modified.setAccessTokenTimeout(0L);
		modified.setRefreshTokenTimeout(0L);
		
		expect(clientService.loadClientByClientId("1")).andReturn(original).once();
		expect(clientService.updateClient(original, modified)).andReturn(modified).once();
		replay(clientService);
		
		ModelAndView result = clientAPI.apiUpdateClient(new ModelAndView(), "1", "",
				"", "", "", "root 42", "Modified", "", false, 
				0L, 0L, "");
		
		verify(clientService);
		
		ClientDetailsEntity returned = (ClientDetailsEntity)result.getModel().get("entity");
		
		assertThat(returned.getClientId(), CoreMatchers.equalTo(modified.getClientId()));
		assertThat(returned.getClientName(), CoreMatchers.equalTo(modified.getClientName()));
		assertThat(returned.isAllowRefresh(), CoreMatchers.equalTo(modified.isAllowRefresh()));
		assertThat((ArrayList<GrantedAuthority>)returned.getAuthorities(), CoreMatchers.equalTo(authorities));
		assertThat(returned.getAccessTokenTimeout(), CoreMatchers.equalTo(modified.getAccessTokenTimeout()));
		assertThat(returned.getRefreshTokenTimeout(), CoreMatchers.equalTo(modified.getRefreshTokenTimeout()));
	}
	
	@Test(expected = ClientNotFoundException.class)
	public void updateClient_invalidClient() {
		expect(clientService.loadClientByClientId("1")).andReturn(null).once();
		replay(clientService);
		clientAPI.apiUpdateClient(new ModelAndView(), "1", "",
				"", "", "", "", "Modified", "", false, 
				0L, 0L, "");
		verify(clientService);
	}
	
	@Test
	public void getClientById_valid() {
		expect(clientService.loadClientByClientId("1")).andReturn(client).once();
		replay(clientService);
		
		ModelAndView result = clientAPI.getClientById(new ModelAndView(), "1");
		verify(clientService);
		
		ClientDetailsEntity returned = (ClientDetailsEntity)result.getModel().get("entity");
		
		assertThat(returned.getClientId(), CoreMatchers.equalTo(client.getClientId()));
		assertThat(returned.getClientName(), CoreMatchers.equalTo(client.getClientName()));
		assertThat(returned.getClientSecret(), CoreMatchers.equalTo(client.getClientSecret()));
		assertThat(returned.isAllowRefresh(), CoreMatchers.equalTo(client.isAllowRefresh()));
		assertThat(returned.getAccessTokenTimeout(), CoreMatchers.equalTo(client.getAccessTokenTimeout()));
		assertThat(returned.getRefreshTokenTimeout(), CoreMatchers.equalTo(client.getRefreshTokenTimeout()));
	}
	
	@Test(expected = ClientNotFoundException.class)
	public void getClientById_invalid() {
		expect(clientService.loadClientByClientId("1")).andReturn(null).once();
		replay(clientService);
		
		clientAPI.getClientById(new ModelAndView(), "1");
		verify(clientService);
	}
	
}
