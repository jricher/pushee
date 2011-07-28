/**
 * 
 */
package org.mitre.pushee.oauth.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.mitre.pushee.oauth.model.ClientDetailsEntity;
import org.mitre.pushee.oauth.model.OAuth2AccessTokenEntity;
import org.mitre.pushee.oauth.model.OAuth2AccessTokenEntityFactory;
import org.mitre.pushee.oauth.model.OAuth2RefreshTokenEntity;
import org.mitre.pushee.oauth.model.OAuth2RefreshTokenEntityFactory;
import org.mitre.pushee.oauth.repository.OAuth2TokenRepository;
import org.mitre.pushee.oauth.service.ClientDetailsEntityService;
import org.mitre.pushee.oauth.service.OAuth2TokenEntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth.provider.token.ExpiredOAuthTokenException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.ClientAuthenticationToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.OAuth2ProviderTokenServices;
import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;


/**
 * @author jricher
 * 
 */
@Service
public class DefaultOAuth2ProviderTokenService implements OAuth2TokenEntityService {
	
	private static Logger logger = LoggerFactory.getLogger(DefaultOAuth2ProviderTokenService.class);

	@Autowired
	private OAuth2TokenRepository tokenRepository;
	
	@Autowired
	private ClientDetailsEntityService clientDetailsService;
	
	@Autowired
	private OAuth2AccessTokenEntityFactory accessTokenFactory;
	
	@Autowired
	private OAuth2RefreshTokenEntityFactory refreshTokenFactory;
	
	@Override
    public OAuth2AccessTokenEntity createAccessToken(OAuth2Authentication authentication) throws AuthenticationException {
		if (authentication != null && 
				authentication.getClientAuthentication() != null && 
				authentication.getClientAuthentication() instanceof ClientAuthenticationToken) {
			// look up our client
			ClientAuthenticationToken clientAuth = (ClientAuthenticationToken) authentication.getClientAuthentication();
			ClientDetailsEntity client = clientDetailsService.loadClientByClientId(clientAuth.getClientId());
		
			if (client == null) {
				throw new InvalidClientException("Client not found: " + clientAuth.getClientId());
			}
			
			OAuth2AccessTokenEntity token = accessTokenFactory.createNewAccessToken();
		    
		    // attach the client
	    	token.setClient(client);
	    	
		    // inherit the scope from the auth
	    	// this lets us match which scope is requested 
		    if (client.isScoped()) {
		    	
		    	// restrict granted scopes to a valid subset of those 
		    	Set<String> validScopes = Sets.newHashSet();
		    	
		    	for (String requested : clientAuth.getScope()) {
	                if (client.getScope().contains(requested)) {
	                	validScopes.add(requested);
	                } else {
	                	logger.warn("Client " + client.getClientId() + " requested out of permission scope: " + requested);
	                }
                }
		    	
		    	token.setScope(validScopes);
		    }

		    // make it expire if necessary
	    	if (client.getAccessTokenTimeout() != null) {
	    		Date expiration = new Date(System.currentTimeMillis() + (client.getAccessTokenTimeout() * 1000L));
	    		token.setExpiration(expiration);
	    	}
		    
	    	// attach the authorization so that we can look it up later
	    	token.setAuthentication(authentication);
	    	
	    	// attach a refresh token, if this client is allowed to request them
	    	if (client.isAllowRefresh()) {
	    		OAuth2RefreshTokenEntity refreshToken = refreshTokenFactory.createNewRefreshToken();
	    		
	    		// make it expire if necessary
	    		if (client.getRefreshTokenTimeout() != null) {
		    		Date expiration = new Date(System.currentTimeMillis() + (client.getRefreshTokenTimeout() * 1000L));
		    		refreshToken.setExpiration(expiration);
	    		}
	    		
	    		// save our scopes so that we can reuse them later for more auth tokens
	    		// TODO: save the auth instead of the just the scope?
			    if (client.isScoped()) {
			    	refreshToken.setScope(clientAuth.getScope());
			    }
			
			    tokenRepository.saveRefreshToken(refreshToken);
			    
	    		token.setRefreshToken(refreshToken);
	    	}
	    	
		    tokenRepository.saveAccessToken(token);		    
		    
		    return token;
		}
		
	    return null;
    }

	@Override
    public OAuth2AccessTokenEntity refreshAccessToken(String refreshTokenValue) throws AuthenticationException {
		
		OAuth2RefreshTokenEntity refreshToken = tokenRepository.getRefreshTokenByValue(refreshTokenValue);
		
		if (refreshToken == null) {
			throw new InvalidTokenException("Invalid refresh token: " + refreshTokenValue);
		}
		
		ClientDetailsEntity client = refreshToken.getClient();		
		// should we check client.isAllowRefresh() here? Sanity check?
		
		// clear out any access tokens
		// TODO: make this a configurable option
		tokenRepository.clearAccessTokensForRefreshToken(refreshToken);
		
		if (refreshToken.isExpired()) {
			tokenRepository.removeRefreshToken(refreshToken);
			throw new ExpiredOAuthTokenException("Expired refresh token: " + refreshTokenValue);			
		}
		
		// TODO: have the option to recycle the refresh token here, too
		// for now, we just reuse it as long as it's valid, which is the original intent

		OAuth2AccessTokenEntity token = accessTokenFactory.createNewAccessToken();
	    
	    // TODO: we should get the scope of this request passed in so that we can re-scope the access token
	    // else pass through to the old scope
	    if (client.isScoped()) {
	    	token.setScope(refreshToken.getScope());
	    }
	    
    	token.setClient(client);
    	
    	if (client.getAccessTokenTimeout() != null) {
    		Date expiration = new Date(System.currentTimeMillis() + (client.getAccessTokenTimeout() * 1000L));
    		token.setExpiration(expiration);
    	}
    	
    	token.setRefreshToken(refreshToken);
    	
    	tokenRepository.saveAccessToken(token);
    	
    	return token;
		
    }

	@Override
    public OAuth2Authentication loadAuthentication(String accessTokenValue) throws AuthenticationException {
		
		OAuth2AccessTokenEntity accessToken = tokenRepository.getAccessTokenByValue(accessTokenValue);
		
		if (accessToken == null) {
			throw new InvalidTokenException("Invalid access token: " + accessTokenValue);
		}
		
		if (accessToken.isExpired()) {
			tokenRepository.removeAccessToken(accessToken);
			throw new ExpiredOAuthTokenException("Expired access token: " + accessTokenValue);
		}
		
	    return accessToken.getAuthentication();
    }


	@Override
    public OAuth2AccessTokenEntity getAccessToken(String accessTokenValue) {
		OAuth2AccessTokenEntity accessToken = tokenRepository.getAccessTokenByValue(accessTokenValue);
		
		return accessToken;		
    }

	@Override
    public OAuth2RefreshTokenEntity getRefreshToken(String refreshTokenValue) {
		OAuth2RefreshTokenEntity refreshToken = tokenRepository.getRefreshTokenByValue(refreshTokenValue);
		
		return refreshToken;
    }
	
	@Override
    public void revokeRefreshToken(OAuth2RefreshTokenEntity refreshToken) {
	    tokenRepository.clearAccessTokensForRefreshToken(refreshToken);
	    tokenRepository.removeRefreshToken(refreshToken);	    
    }

	@Override
    public void revokeAccessToken(OAuth2AccessTokenEntity accessToken) {
		tokenRepository.removeAccessToken(accessToken);	    
    }
	

	/* (non-Javadoc)
     * @see org.mitre.pushee.oauth.service.OAuth2TokenEntityService#getAccessTokensForClient(org.mitre.pushee.oauth.model.ClientDetailsEntity)
     */
    @Override
    public List<OAuth2AccessTokenEntity> getAccessTokensForClient(ClientDetailsEntity client) {
	    return tokenRepository.getAccessTokensForClient(client);
    }

	/* (non-Javadoc)
     * @see org.mitre.pushee.oauth.service.OAuth2TokenEntityService#getRefreshTokensForClient(org.mitre.pushee.oauth.model.ClientDetailsEntity)
     */
    @Override
    public List<OAuth2RefreshTokenEntity> getRefreshTokensForClient(ClientDetailsEntity client) {
    	return tokenRepository.getRefreshTokensForClient(client);
    }

	/**
	 * Get a builder object for this class (for tests)
	 * @return
	 */
	public static DefaultOAuth2ProviderTokenServicesBuilder makeBuilder() {
		return new DefaultOAuth2ProviderTokenServicesBuilder();
	}
	
	/**
	 * Builder class for test harnesses.
	 */
	public static class DefaultOAuth2ProviderTokenServicesBuilder {
		private DefaultOAuth2ProviderTokenService instance;
		
		private DefaultOAuth2ProviderTokenServicesBuilder() {
			instance = new DefaultOAuth2ProviderTokenService();
		}
		
		public DefaultOAuth2ProviderTokenServicesBuilder setTokenRepository(OAuth2TokenRepository tokenRepository) {
			instance.tokenRepository = tokenRepository;
			return this;
		}
		
		public DefaultOAuth2ProviderTokenServicesBuilder setClientDetailsService(ClientDetailsEntityService clientDetailsService) {
			instance.clientDetailsService = clientDetailsService;
			return this;
		}
		
		public DefaultOAuth2ProviderTokenServicesBuilder setAccessTokenFactory(OAuth2AccessTokenEntityFactory accessTokenFactory) {
			instance.accessTokenFactory = accessTokenFactory;
			return this;
		}
		
		public DefaultOAuth2ProviderTokenServicesBuilder setRefreshTokenFactory(OAuth2RefreshTokenEntityFactory refreshTokenFactory) {
			instance.refreshTokenFactory = refreshTokenFactory;
			return this;
		}
		
		public DefaultOAuth2ProviderTokenService finish() {
			return instance;
		}
	}

}
