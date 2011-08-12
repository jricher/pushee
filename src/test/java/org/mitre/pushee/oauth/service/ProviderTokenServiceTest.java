package org.mitre.pushee.oauth.service;

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
import org.mitre.pushee.oauth.model.ClientDetailsEntity;
import org.mitre.pushee.oauth.model.OAuth2AccessTokenEntity;
import org.mitre.pushee.oauth.model.OAuth2AccessTokenEntityFactory;
import org.mitre.pushee.oauth.model.OAuth2RefreshTokenEntity;
import org.mitre.pushee.oauth.model.OAuth2RefreshTokenEntityFactory;
import org.mitre.pushee.oauth.repository.OAuth2TokenRepository;
import org.mitre.pushee.oauth.service.impl.DefaultOAuth2ProviderTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.AuthorizedClientAuthenticationToken;
import org.springframework.security.oauth2.provider.ClientAuthenticationToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.OAuth2ProviderTokenServices;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * 
 * @author jricher
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/spring/application-context.xml", "classpath:test-context.xml"})
public class ProviderTokenServiceTest {

	private Logger logger;
	
	private OAuth2ProviderTokenServices tokenServices;
	
	private OAuth2TokenRepository tokenRepository;
	private ClientDetailsEntityService clientDetailsService;
	private OAuth2AccessTokenEntityFactory accessTokenFactory;
	private OAuth2RefreshTokenEntityFactory refreshTokenFactory;
	
	private String clientId;
	private String clientSecret;
	private String accessTokenValue;
	private String refreshTokenValue;
	private ClientDetailsEntity clientDetails;	
	
	private OAuth2Authentication oAuth2Authentication;
	private TestingAuthenticationToken userAuthentication;
	private ClientAuthenticationToken clientAuthentication;
	private Set<String> scope;
	private List<GrantedAuthority> authorities;
	
	private OAuth2AccessTokenEntity accessToken;
	private OAuth2RefreshTokenEntity refreshToken;
	
	@Before
	public void setup() {
		logger = LoggerFactory.getLogger(this.getClass());
		logger.info("Setting up ProviderTokenServiceTest");
		
		tokenRepository = createNiceMock(OAuth2TokenRepository.class);
		clientDetailsService = createNiceMock(ClientDetailsEntityService.class);
		accessTokenFactory = createNiceMock(OAuth2AccessTokenEntityFactory.class);
		refreshTokenFactory = createNiceMock(OAuth2RefreshTokenEntityFactory.class);
		
		tokenServices = DefaultOAuth2ProviderTokenService.makeBuilder()
							.setTokenRepository(tokenRepository)
							.setClientDetailsService(clientDetailsService)
							.setAccessTokenFactory(accessTokenFactory)
							.setRefreshTokenFactory(refreshTokenFactory)
							.finish();

		clientId = "clientid";
		clientSecret = "clientsecret";
		accessTokenValue = "ACCESS-TOKEN-VALUE";
		refreshTokenValue = "REFRESH-TOKEN-VALUE";
		
		scope = Sets.newHashSet("test");
		authorities = Lists.newArrayList();
		authorities.add(new GrantedAuthorityImpl("RIGHT_TEST")); // for some reason the guava constructor doesn't work here

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
		
		logger.info("Setup finished");
	}
	
	@Test
	public void createAccessToken_validAuth() {
		logger.info("Starting createAccessToken test with valid auth object");
		
		expect(clientDetailsService.loadClientByClientId(clientId)).andReturn(clientDetails).once();		
		replay(clientDetailsService);
		
		expect(accessTokenFactory.createNewAccessToken()).andReturn(accessToken).once();
		replay(accessTokenFactory);
		
		expect(refreshTokenFactory.createNewRefreshToken()).andReturn(refreshToken).once();
		replay(refreshTokenFactory);
		
		expect(tokenRepository.saveRefreshToken(refreshToken)).andReturn(refreshToken).once();
		expect(tokenRepository.saveAccessToken(accessToken)).andReturn(accessToken).once();
		replay(tokenRepository);
		
		OAuth2AccessTokenEntity token = (OAuth2AccessTokenEntity) tokenServices.createAccessToken(oAuth2Authentication);
		
		verify(tokenRepository);
		verify(clientDetailsService);
		verify(accessTokenFactory);
		verify(refreshTokenFactory);
		
		assertThat(token, is(accessToken));
	}
	
	@Test(expected=AuthenticationCredentialsNotFoundException.class)
	public void createAccessToken_nullAuth() {
		OAuth2AccessToken token = tokenServices.createAccessToken(null);
	}
	
	@Test
	public void refreshAccessToken_validToken() {
		logger.info("Starting refreshAccessToken with valid token");
		
		expect(tokenRepository.getRefreshTokenByValue(refreshTokenValue)).andReturn(refreshToken).once();
		tokenRepository.clearAccessTokensForRefreshToken(refreshToken);
		expectLastCall();
		replay(tokenRepository);
		
		expect(accessTokenFactory.createNewAccessToken()).andReturn(accessToken).once();
		replay(accessTokenFactory);
		
		OAuth2AccessTokenEntity token = (OAuth2AccessTokenEntity) tokenServices.refreshAccessToken(refreshTokenValue);
		
		verify(tokenRepository);
		verify(accessTokenFactory);
		
		assertThat(token, is(accessToken));
	}
	
	@Test(expected = InvalidTokenException.class)
	public void refreshAccessToken_invalidToken() {
		logger.info("Starting refreshAccesStoken with invalid token");
		
		expect(tokenRepository.getRefreshTokenByValue(refreshTokenValue)).andReturn(null);
		replay(tokenRepository);
		
		tokenServices.refreshAccessToken(refreshTokenValue);
		
		verify(tokenRepository);
	}
	
	@Test
	public void loadAuthentication_validToken() {
		logger.info("Starting loadAuthentication with a valid token");
		expect(tokenRepository.getAccessTokenByValue(accessTokenValue)).andReturn(accessToken);
		
		replay(tokenRepository);
		
		OAuth2Authentication auth = tokenServices.loadAuthentication(accessTokenValue);
		
		assertThat(auth, is(oAuth2Authentication));
	}
	
}
