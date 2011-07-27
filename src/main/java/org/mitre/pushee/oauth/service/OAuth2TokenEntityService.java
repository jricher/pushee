package org.mitre.pushee.oauth.service;

import org.mitre.pushee.oauth.model.OAuth2AccessTokenEntity;
import org.mitre.pushee.oauth.model.OAuth2RefreshTokenEntity;
import org.springframework.security.oauth2.provider.token.OAuth2ProviderTokenServices;

public interface OAuth2TokenEntityService extends OAuth2ProviderTokenServices {

	public OAuth2AccessTokenEntity getAccessToken(String accessTokenValue);
	
	public OAuth2RefreshTokenEntity getRefreshToken(String refreshTokenValue);

	public void revokeRefreshToken(OAuth2RefreshTokenEntity refreshToken);

	public void revokeAccessToken(OAuth2AccessTokenEntity accessToken);
	
}
