package org.mitre.pushee.oauth.web;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mitre.pushee.hub.exception.PermissionDeniedException;
import org.mitre.pushee.oauth.model.ClientDetailsEntity;
import org.mitre.pushee.oauth.model.OAuth2AccessTokenEntity;
import org.mitre.pushee.oauth.model.OAuth2RefreshTokenEntity;
import org.mitre.pushee.oauth.service.ClientDetailsEntityService;
import org.mitre.pushee.oauth.service.OAuth2TokenEntityService;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.AuthorizedClientAuthenticationToken;
import org.springframework.security.oauth2.provider.ClientAuthenticationToken;
import org.springframework.security.oauth2.provider.DefaultOAuth2GrantManager;
import org.springframework.security.oauth2.provider.DefaultOAuth2GrantManager.GrantType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * Unit test for OauthClientController
 * 
 * @author AANGANES
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/spring/application-context.xml", "classpath:test-context.xml"})
public class OAuthClientControllerTest {

	private ClientDetailsEntityService clientService;
	private OAuth2TokenEntityService tokenService;
	private OAuthClientController controller;
	
	private OAuth2TokenEntityService tokenServices;
	private RevocationEndpoint endpoint;
	
	private String clientId;
	private String clientSecret;
	private String accessTokenValue;
	private String refreshTokenValue;
	private ClientDetailsEntity clientDetails;	
	
	@SuppressWarnings("rawtypes")
	private OAuth2Authentication oAuth2Authentication;
	@SuppressWarnings("rawtypes")
	private OAuth2Authentication badAuth;
	private TestingAuthenticationToken userAuthentication;
	private ClientAuthenticationToken clientAuthentication;
	private TestingAuthenticationToken badUserAuth;
	private ClientAuthenticationToken badClientAuth;
	private Set<String> scope;
	private List<GrantedAuthority> authorities;
	private List<GrantedAuthority> noAuthority;
	private OAuth2AccessTokenEntity accessToken;
	private OAuth2RefreshTokenEntity refreshToken;

	@Before
	public void setup() {
		clientService = createNiceMock(ClientDetailsEntityService.class);
		tokenService = createNiceMock(OAuth2TokenEntityService.class);
		controller = new OAuthClientController(clientService, tokenService);
		
		clientId = "clientid";
		clientSecret = "clientsecret";
		accessTokenValue = "ACCESS-TOKEN-VALUE";
		refreshTokenValue = "REFRESH-TOKEN-VALUE";
		
		scope = Sets.newHashSet("test");
		authorities = Lists.newArrayList();
		authorities.add(new GrantedAuthorityImpl("ROLE_ADMIN")); // for some reason the guava constructor doesn't work here

		List<String> authorizedGrantTypes = Lists.newArrayList("authorization_code", "refresh_token");
		String webServerRedirectUri = "http://localhost/return";
		
		clientDetails = ClientDetailsEntity.makeBuilder()
							.setClientId(clientId).setClientSecret(clientSecret)
							.setScope(Lists.newArrayList(scope))
							.setAllowRefresh(true)
							.setAuthorizedGrantTypes(authorizedGrantTypes)
							.setWebServerRedirectUri(webServerRedirectUri)
							.setAuthorities(authorities).finish();
		
		// we fake the auth for now, the underlying bits mostly just care about the client auth
		userAuthentication = new TestingAuthenticationToken(clientId, clientSecret);
		clientAuthentication = new AuthorizedClientAuthenticationToken(clientId, clientSecret, scope, authorities);
		oAuth2Authentication = new OAuth2Authentication<Authentication, Authentication>(clientAuthentication, userAuthentication);
		
		//Create a bad auth for testing that it cannot access the protected URLs
		noAuthority = Lists.newArrayList();
		noAuthority.add(new GrantedAuthorityImpl("NO_AUTH"));
		badUserAuth = new TestingAuthenticationToken("id", "secret");
		badClientAuth = new AuthorizedClientAuthenticationToken("id", "secret", scope, noAuthority);
		badAuth = new OAuth2Authentication<Authentication, Authentication>(badClientAuth, badUserAuth);
		
		refreshToken = new OAuth2RefreshTokenEntity();
		refreshToken.setValue(refreshTokenValue);
		refreshToken.setClient(clientDetails);
		refreshToken.setScope(scope);
		
		accessToken = new OAuth2AccessTokenEntity();
		accessToken.setValue(accessTokenValue);
		accessToken.setClient(clientDetails);
		accessToken.setAuthentication(oAuth2Authentication);
		accessToken.setScope(scope);
		accessToken.setRefreshToken(refreshToken);
		accessToken.setTokenType("bearer");
		
		SecurityContextHolder.getContext().setAuthentication(oAuth2Authentication);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void viewAll() {
		ClientDetailsEntity client1 = new ClientDetailsEntity();
		client1.setClientId("1");
		
		ClientDetailsEntity client2 = new ClientDetailsEntity();
		client2.setClientId("2");
		
		ArrayList<ClientDetailsEntity> allClients = new ArrayList<ClientDetailsEntity>();
		allClients.add(client1);
		allClients.add(client2);
		
		expect(clientService.getAllClients()).andReturn(allClients).once();
		replay(clientService);
		
		ModelAndView result = controller.viewAllClients(new ModelAndView());
		
		verify(clientService);
		
		assertThat((ArrayList<ClientDetailsEntity>)result.getModel().get("clients"), CoreMatchers.equalTo(allClients));
		assertThat(result.getViewName(), CoreMatchers.equalTo("/management/oauth/clientIndex"));
	}
	
	/*
	 * The following test should fail - why isn't it?
	 */
	@Test(expected = PermissionDeniedException.class)
	@Rollback
	@Ignore
	public void viewAll_noAuth() {
		SecurityContextHolder.getContext().setAuthentication(badAuth);
		controller.viewAllClients(new ModelAndView());
	}
	
	@Test
	public void addClient() {
		List<GrantedAuthority> auth = Lists.newArrayList();
		auth.add(new GrantedAuthorityImpl("ROLE_CLIENT"));
		
		ClientDetailsEntity client = ClientDetailsEntity.makeBuilder()
				.setScope(Lists.newArrayList("scope"))
				.setAuthorities(auth) // why do we have to pull this into a separate list?
				.setAuthorizedGrantTypes(Lists.newArrayList(DefaultOAuth2GrantManager.GrantType.authorization_code.toString()))
				.finish();
		
		ModelAndView result = controller.addClientPage(new ModelAndView());
		
		GrantType[] grantTypes = DefaultOAuth2GrantManager.GrantType.values();
		
		assertThat((GrantType[])result.getModel().get("availableGrantTypes"), CoreMatchers.equalTo(grantTypes));
		assertThat((ClientDetailsEntity)result.getModel().get("client"), CoreMatchers.equalTo(client));
		assertThat(result.getViewName(), CoreMatchers.equalTo("/management/oauth/editClient"));
	}
	
	@Test
	public void deleteClient_valid() {
		expect(clientService.loadClientByClientId(clientId)).andReturn(clientDetails).once();
		replay(clientService);
		
		ModelAndView result = controller.deleteClientConfirmation(new ModelAndView(), clientId);
		
		verify(clientService);
		
		assertThat((ClientDetailsEntity)result.getModel().get("client"), CoreMatchers.equalTo(clientDetails));
		assertThat(result.getViewName(), CoreMatchers.equalTo("/management/oauth/deleteClientConfirm"));
	}
	
	@Test(expected = PermissionDeniedException.class)
	@Rollback
	@Ignore
	public void deleteClient_invalid() {
		SecurityContextHolder.getContext().setAuthentication(null);
		controller.deleteClientConfirmation(new ModelAndView(), clientId);
	}
	
	@Test
	@Ignore
	public void editClient_valid() {
		
	}
	
	@Test(expected = PermissionDeniedException.class)
	@Rollback
	@Ignore
	public void editClient_invalid() {
		expect(clientService.loadClientByClientId(clientId)).andReturn(clientDetails).once();
		replay(clientService);
		SecurityContextHolder.getContext().setAuthentication(null);
		controller.editClientPage(new ModelAndView(), clientId);
		verify(clientService);
		
	}
	
	@Test
	@Ignore
	public void viewDetails_valid() {
		
	}
	
	@Test(expected = PermissionDeniedException.class)
	@Rollback
	@Ignore
	public void viewDetails_invalid() {
		expect(clientService.loadClientByClientId(clientId)).andReturn(clientDetails).once();
		replay(clientService);
		SecurityContextHolder.getContext().setAuthentication(null);
		controller.viewClientDetails(new ModelAndView(), clientId);
		verify(clientService);
		
	}
	
}
