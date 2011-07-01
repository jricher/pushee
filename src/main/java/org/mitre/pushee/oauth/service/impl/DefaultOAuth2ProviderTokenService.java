/**
 * 
 */
package org.mitre.pushee.oauth.service.impl;

import java.util.Date;
import java.util.UUID;

import org.mitre.pushee.oauth.model.ClientDetailsEntity;
import org.mitre.pushee.oauth.model.OAuth2AccessTokenEntity;
import org.mitre.pushee.oauth.model.OAuth2RefreshTokenEntity;
import org.mitre.pushee.oauth.repository.OAuth2TokenRepository;
import org.mitre.pushee.oauth.service.ClientDetailsEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth.provider.token.ExpiredOAuthTokenException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.ClientAuthenticationToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.OAuth2ProviderTokenServices;
import org.springframework.stereotype.Service;


/**
 * @author jricher
 * 
 */
@Service
public class DefaultOAuth2ProviderTokenService implements OAuth2ProviderTokenServices {

	@Autowired
	private OAuth2TokenRepository tokenRepository;
	
	@Autowired
	private ClientDetailsEntityService clientDetailsService;
	
	@Override
    public OAuth2AccessToken createAccessToken(OAuth2Authentication authentication) throws AuthenticationException {
		if (authentication.getClientAuthentication() != null && authentication.getClientAuthentication() instanceof ClientAuthenticationToken) {
			// look up our client
			ClientAuthenticationToken clientAuth = (ClientAuthenticationToken) authentication.getClientAuthentication();
			ClientDetailsEntity client = clientDetailsService.loadClientByClientId(clientAuth.getClientId());
		
			// create our token container
			OAuth2AccessTokenEntity token = new OAuth2AccessTokenEntity();
			
			// set a random value (TODO: support JWT)
		    String tokenValue = UUID.randomUUID().toString();
		    token.setValue(tokenValue);
		    
		    // attach the client
	    	token.setClient(client);
	    	
		    // inherit the scope from the auth
		    if (client.isScoped()) {
		    	token.setScope(clientAuth.getScope());
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
	    		OAuth2RefreshTokenEntity refreshToken = new OAuth2RefreshTokenEntity();
	    		
	    		// set a random value for the refresh
	    		String refreshTokenValue = UUID.randomUUID().toString();
	    		refreshToken.setValue(refreshTokenValue);
	    		
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
    public OAuth2AccessToken refreshAccessToken(String refreshTokenValue) throws AuthenticationException {
		
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

		OAuth2AccessTokenEntity token = new OAuth2AccessTokenEntity();
	    String tokenValue = UUID.randomUUID().toString();
	    token.setValue(tokenValue);
	    
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

	
	
}
