package org.mitre.pushee.oauth.web;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mitre.pushee.hub.exception.PermissionDeniedException;
import org.mitre.pushee.oauth.model.ClientDetailsEntity;
import org.mitre.pushee.oauth.model.OAuth2AccessTokenEntity;
import org.mitre.pushee.oauth.model.OAuth2RefreshTokenEntity;
import org.mitre.pushee.oauth.service.OAuth2TokenEntityService;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.AuthorizedClientAuthenticationToken;
import org.springframework.security.oauth2.provider.ClientAuthenticationToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;


/**
 * Unit test for OAuth RevocationEndpoint
 * @author AANGANES
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/spring/application-context.xml", "classpath:test-context.xml"})
public class RevocationEndpointTest {

	private OAuth2TokenEntityService tokenServices;
	private RevocationEndpoint endpoint;
	
	private String clientId;
	private String clientSecret;
	private String accessTokenValue;
	private String refreshTokenValue;
	private ClientDetailsEntity clientDetails;	
	
	@SuppressWarnings("rawtypes")
	private OAuth2Authentication oAuth2Authentication;
	private TestingAuthenticationToken userAuthentication;
	private ClientAuthenticationToken clientAuthentication;
	private Set<String> scope;
	private List<GrantedAuthority> authorities;
	private OAuth2RefreshTokenEntity badRefreshToken;
	private OAuth2AccessTokenEntity accessToken;
	
	private ClientDetailsEntity otherClient;
	private OAuth2RefreshTokenEntity refreshToken;
	private OAuth2AccessTokenEntity badAccessToken;

	@Before
	public void setup() {
		tokenServices = createNiceMock(OAuth2TokenEntityService.class);
		endpoint = new RevocationEndpoint(tokenServices);
		
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
		
		otherClient = ClientDetailsEntity.makeBuilder()
							.setClientId("otherClientId").setClientSecret(clientSecret)
							.setScope(Lists.newArrayList(scope))
							.setAllowRefresh(true)
							.setAuthorizedGrantTypes(authorizedGrantTypes)
							.setWebServerRedirectUri(webServerRedirectUri)
							.setAuthorities(authorities).finish();
		
		badRefreshToken = new OAuth2RefreshTokenEntity();
		badRefreshToken.setValue(refreshTokenValue);
		badRefreshToken.setClient(otherClient);
		badRefreshToken.setScope(scope);
		
		badAccessToken = new OAuth2AccessTokenEntity();
		badAccessToken.setValue(accessTokenValue);
		badAccessToken.setClient(otherClient);
		badAccessToken.setScope(scope);
		badAccessToken.setRefreshToken(badRefreshToken);
		badAccessToken.setTokenType("bearer");
		
		SecurityContextHolder.getContext().setAuthentication(oAuth2Authentication);
	}
	
	@Test
	@Rollback
	public void revoke_validRefreshToken() {
		expect(tokenServices.getRefreshToken(refreshTokenValue)).andReturn(refreshToken).once();
		tokenServices.revokeRefreshToken(refreshToken);
		expectLastCall();
		replay(tokenServices);
		
		ModelAndView mav = new ModelAndView();
		
		ModelAndView result = endpoint.revoke(refreshTokenValue, mav);
		
		verify(tokenServices);
		
		assertThat(result, is(mav));
	}
	
	@Test
	@Rollback
	public void revoke_validAccessToken() {
		expect(tokenServices.getAccessToken(accessTokenValue)).andReturn(accessToken).once();
		tokenServices.revokeAccessToken(accessToken);
		expectLastCall();
		replay(tokenServices);
		
		ModelAndView mav = new ModelAndView();
		
		ModelAndView result = endpoint.revoke(accessTokenValue, mav);
		
		verify(tokenServices);
		
		assertThat(result, is(mav));
	}
	
	@Test(expected = InvalidTokenException.class)
	public void revoke_nullToken() {
		expect(tokenServices.getAccessToken("bad")).andReturn(null).once();
		expect(tokenServices.getRefreshToken("bad")).andReturn(null).once();
		replay(tokenServices);
		
		endpoint.revoke("bad", new ModelAndView());
		
		verify(tokenServices);
	}
	
	@Test(expected = PermissionDeniedException.class)
	public void revoke_invalidUnownedRefreshToken() {
		expect(tokenServices.getRefreshToken(refreshTokenValue)).andReturn(badRefreshToken).once();
		replay(tokenServices);
		
		endpoint.revoke(refreshTokenValue, new ModelAndView());
		
		verify(tokenServices);
	}
	
	@Test(expected = PermissionDeniedException.class)
	public void revoke_invalidUnownedAccessToken() {
		expect(tokenServices.getAccessToken(accessTokenValue)).andReturn(badAccessToken).once();
		replay(tokenServices);
		
		endpoint.revoke(accessTokenValue, new ModelAndView());
		
		verify(tokenServices);
	}
}
