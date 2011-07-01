package org.mitre.pushee.oauth.repository;

import org.mitre.pushee.oauth.model.OAuth2AccessTokenEntity;
import org.mitre.pushee.oauth.model.OAuth2RefreshTokenEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

public interface OAuth2TokenRepository {

	public OAuth2AccessTokenEntity saveAccessToken(OAuth2AccessTokenEntity token);

	public OAuth2RefreshTokenEntity getRefreshTokenByValue(String refreshTokenValue);

	public void clearAccessTokensForRefreshToken(OAuth2RefreshTokenEntity refreshToken);

	public void removeRefreshToken(OAuth2RefreshTokenEntity refreshToken);
	
	public OAuth2RefreshTokenEntity saveRefreshToken(OAuth2RefreshTokenEntity refreshToken);

	public OAuth2AccessTokenEntity getAccessTokenByValue(String accessTokenValue);

	public void removeAccessToken(OAuth2AccessTokenEntity accessToken);

}
