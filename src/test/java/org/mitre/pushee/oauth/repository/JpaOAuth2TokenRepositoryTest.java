package org.mitre.pushee.oauth.repository;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mitre.pushee.oauth.model.ClientDetailsEntity;
import org.mitre.pushee.oauth.model.OAuth2AccessTokenEntity;
import org.mitre.pushee.oauth.model.OAuth2RefreshTokenEntity;
import org.mitre.pushee.oauth.repository.impl.JpaOAuth2TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

/**
 * Unit test for JpaOauth2TokenRepository
 * 
 * @author AANGANES
 *
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/spring/application-context.xml", "classpath:test-context.xml"})
public class JpaOAuth2TokenRepositoryTest {
	
	//Inject the same entity manager that is injected into the repository
	@PersistenceContext
	private EntityManager manager;
	
	
	@Autowired
	private JpaOAuth2TokenRepository repository;
	
	@Test
	public void getAccessTokenByValue_valid() {
		//There are two tokens in the test database already
		OAuth2AccessTokenEntity token = repository.getAccessTokenByValue("1asdf");
		assertThat(token.getTokenType(), CoreMatchers.equalTo("bearer"));
		assertThat(token.getRefreshToken().getValue(), CoreMatchers.equalTo("1"));
		assertThat(token.getClient().getClientId(), CoreMatchers.equalTo("1"));
		
		OAuth2AccessTokenEntity token2 = repository.getAccessTokenByValue("jklop2");
		assertThat(token2.getTokenType(), CoreMatchers.equalTo("bearer"));
		assertThat(token2.getRefreshToken().getValue(), CoreMatchers.equalTo("5"));
		assertThat(token2.getClient().getClientId(), CoreMatchers.equalTo("2"));
	}
	
	@Test
	public void getAccessTokenByValue_invalid() {
		OAuth2AccessTokenEntity token = repository.getAccessTokenByValue("789");
		assertThat(token, is(nullValue()));
	}
	
	@Test
	public void saveAccessToken_valid() {
		OAuth2AccessTokenEntity token = new OAuth2AccessTokenEntity();
		token.setTokenType("bearer");
		token.setValue("jabberwocky");
		
		OAuth2AccessTokenEntity saved = repository.saveAccessToken(token);
		manager.flush();
		
		assertThat(saved, equalTo(token));
	}
	
	@Test
	@Rollback
	public void removeAccessToken_valid() {
		OAuth2AccessTokenEntity token = repository.getAccessTokenByValue("1asdf");
		assertThat(token, is(not(nullValue())));
		
		repository.removeAccessToken(token);
		manager.flush();
		
		OAuth2AccessTokenEntity removedToken = repository.getAccessTokenByValue("1asdf");
		assertThat(removedToken, is(nullValue()));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void removeAccessToken_invalid() {
		OAuth2AccessTokenEntity unmanaged = new OAuth2AccessTokenEntity();
		unmanaged.setValue("badToken");
		
		repository.removeAccessToken(unmanaged);
		manager.flush();
	}
	
	@Test
	@Rollback
	public void clearAccessTokensForRefreshToken_valid() {
		OAuth2AccessTokenEntity accessToken = repository.getAccessTokenByValue("1asdf");
		assertThat(accessToken, is(not(nullValue())));
		
		OAuth2RefreshTokenEntity refreshToken = repository.getRefreshTokenByValue("1");
		assertThat(refreshToken, is(not(nullValue())));
		
		repository.clearAccessTokensForRefreshToken(refreshToken);
		
		OAuth2AccessTokenEntity shouldBeNull = repository.getAccessTokenByValue("1asdf");
		assertThat(shouldBeNull, is(nullValue()));
	}
	
	@Test
	public void getRefreshTokenByValue_valid() {
		OAuth2RefreshTokenEntity token = repository.getRefreshTokenByValue("1");
		assertThat(token, is(not(nullValue())));
		assertThat(token.getClient().getClientId(), CoreMatchers.equalTo("1"));
		
		OAuth2RefreshTokenEntity token2 = repository.getRefreshTokenByValue("5");
		assertThat(token2, is(not(nullValue())));
		assertThat(token2.getClient().getClientId(), CoreMatchers.equalTo("2"));
	}
	
	@Test 
	public void getRefreshTokenByValue_invalid() {
		OAuth2RefreshTokenEntity token = repository.getRefreshTokenByValue("notAToken");
		assertThat(token, is(nullValue()));
	}
	
	@Test
	public void saveRefreshToken_valid() {
		OAuth2RefreshTokenEntity token = new OAuth2RefreshTokenEntity();
		token.setValue("value");
		
		repository.saveRefreshToken(token);
		manager.flush();
		
		OAuth2RefreshTokenEntity saved = repository.getRefreshTokenByValue("value");
		assertThat(saved, is(not(nullValue())));
		assertThat(saved, equalTo(token));
	}
	
	@Test
	@Rollback
	public void removeRefreshToken_valid() {
		OAuth2RefreshTokenEntity token = repository.getRefreshTokenByValue("1");
		assertThat(token, is(not(nullValue())));
		
		repository.removeRefreshToken(token);
		manager.flush();
		
		OAuth2RefreshTokenEntity removed = repository.getRefreshTokenByValue("1");
		assertThat(removed, is(nullValue()));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void removeRefreshToken_invalid() {
		OAuth2RefreshTokenEntity unmanaged = new OAuth2RefreshTokenEntity();
		unmanaged.setValue("BadToken");
		
		repository.removeRefreshToken(unmanaged);
		manager.flush();
	}
	
	@Test
	@Rollback
	public void clearTokensForClient_valid() {
		ClientDetailsEntity client1 = new ClientDetailsEntity();
		client1.setClientId("1");
		
		//assert: access token linked to client 1
		OAuth2AccessTokenEntity accessToken = repository.getAccessTokenByValue("1asdf");
		assertThat(accessToken.getClient(), equalTo(client1));
		//assert: refresh token linked to client 1
		OAuth2RefreshTokenEntity refreshToken = repository.getRefreshTokenByValue("1");
		assertThat(refreshToken.getClient(), equalTo(client1));
		
		//clear tokens
		repository.clearTokensForClient(client1);
		manager.flush();
		
		//assert: no access token linked to client1
		OAuth2AccessTokenEntity removedAccessToken = repository.getAccessTokenByValue("1asdf");
		assertThat(removedAccessToken, is(nullValue()));
		//assert: no refresh token linked to client1
		OAuth2RefreshTokenEntity removedRefreshToken = repository.getRefreshTokenByValue("1");
		assertThat(removedRefreshToken, is(nullValue()));
	}
	
	@Test
	public void getAccessTokensForClient_valid() {
		ClientDetailsEntity client1 = new ClientDetailsEntity();
		client1.setClientId("1");
		
		OAuth2AccessTokenEntity accessToken = repository.getAccessTokenByValue("1asdf");
		List<OAuth2AccessTokenEntity> expected = Lists.newArrayList(accessToken);
		
		List<OAuth2AccessTokenEntity> tokens = repository.getAccessTokensForClient(client1);
		
		assertThat(expected, equalTo(tokens));
	}
	
	@Test
	public void getAccessTokensForClient_invalid() {
		ClientDetailsEntity client1 = new ClientDetailsEntity();
		client1.setClientId("noTokens");
		
		List<OAuth2AccessTokenEntity> tokens = repository.getAccessTokensForClient(client1);
		
		assertThat(tokens.size(), equalTo(0));
	}
	
	@Test
	public void getRefreshTokensForClient_valid() {
		ClientDetailsEntity client1 = new ClientDetailsEntity();
		client1.setClientId("1");
		
		OAuth2RefreshTokenEntity refreshToken = repository.getRefreshTokenByValue("1");
		List<OAuth2RefreshTokenEntity> expected = Lists.newArrayList(refreshToken);
		
		List<OAuth2RefreshTokenEntity> tokens = repository.getRefreshTokensForClient(client1);
		
		assertThat(expected, equalTo(tokens));
	}
	
	@Test
	public void getRefreshTokensForClient_invalid() {
		ClientDetailsEntity client1 = new ClientDetailsEntity();
		client1.setClientId("noTokens");
		
		List<OAuth2RefreshTokenEntity> tokens = repository.getRefreshTokensForClient(client1);
		
		assertThat(tokens.size(), equalTo(0));
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void getExpiredAccessTokens() {
		OAuth2AccessTokenEntity accessToken = repository.getAccessTokenByValue("1asdf");
		Date yesterday = new Date();
		yesterday.setHours(yesterday.getHours() - 24);
		accessToken.setExpiration(yesterday);
		
		List<OAuth2AccessTokenEntity> expected = Lists.newArrayList(accessToken);
		
		List<OAuth2AccessTokenEntity> tokens = repository.getExpiredAccessTokens();
		
		assertThat(tokens, equalTo(expected));
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void getExpiredRefreshTokens() {
		OAuth2RefreshTokenEntity accessToken = repository.getRefreshTokenByValue("1");
		Date yesterday = new Date();
		yesterday.setHours(yesterday.getHours() - 24);
		accessToken.setExpiration(yesterday);
		
		List<OAuth2RefreshTokenEntity> expected = Lists.newArrayList(accessToken);
		
		List<OAuth2RefreshTokenEntity> tokens = repository.getExpiredRefreshTokens();
		
		assertThat(tokens, equalTo(expected));
	}
		
}
