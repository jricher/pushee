package org.mitre.pushee.oauth.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.mitre.pushee.hub.repository.util.JpaUtil;
import org.mitre.pushee.oauth.model.ClientDetailsEntity;
import org.mitre.pushee.oauth.model.OAuth2AccessTokenEntity;
import org.mitre.pushee.oauth.model.OAuth2RefreshTokenEntity;
import org.mitre.pushee.oauth.repository.OAuth2TokenRepository;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.consumer.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class JpaOAuth2TokenRepository implements OAuth2TokenRepository {

	@PersistenceContext
	private EntityManager manager;

	@Override
	public OAuth2AccessTokenEntity getAccessTokenByValue(String accessTokenValue) {
		return manager.find(OAuth2AccessTokenEntity.class, accessTokenValue);
	}
	
	@Override
	@Transactional
	public OAuth2AccessTokenEntity saveAccessToken(OAuth2AccessTokenEntity token) {
		return JpaUtil.saveOrUpdate(token.getValue(), manager, token);
	}
	
	@Override
	@Transactional
	public void removeAccessToken(OAuth2AccessTokenEntity accessToken) {
		OAuth2AccessTokenEntity found = getAccessTokenByValue(accessToken.getValue());
		if (found != null) {
			manager.remove(found);
		} else {
			throw new IllegalArgumentException("Access token not found: " + accessToken);
		}
	}
	
	@Override
	@Transactional
    public void clearAccessTokensForRefreshToken(OAuth2RefreshTokenEntity refreshToken) {
		TypedQuery<OAuth2AccessTokenEntity> query = manager.createNamedQuery("OAuth2AccessTokenEntity.getByRefreshToken", OAuth2AccessTokenEntity.class);
		query.setParameter("refreshToken", refreshToken);
	    List<OAuth2AccessTokenEntity> accessTokens = query.getResultList();
	    for (OAuth2AccessTokenEntity accessToken : accessTokens) {
	        removeAccessToken(accessToken);
        }
    }

	@Override
	public OAuth2RefreshTokenEntity getRefreshTokenByValue(String refreshTokenValue) {
		return manager.find(OAuth2RefreshTokenEntity.class, refreshTokenValue);
	}
	
	@Override
	@Transactional
	public OAuth2RefreshTokenEntity saveRefreshToken(OAuth2RefreshTokenEntity refreshToken) {
		return JpaUtil.saveOrUpdate(refreshToken.getValue(), manager, refreshToken);
	}
	
	@Override
	@Transactional
    public void removeRefreshToken(OAuth2RefreshTokenEntity refreshToken) {
		OAuth2RefreshTokenEntity found = getRefreshTokenByValue(refreshToken.getValue());
		if (found != null) {
			manager.remove(found);
		} else {
			throw new IllegalArgumentException("Refresh token not found: " + refreshToken);
		}	    
    }

	@Override
	@Transactional
    public void clearTokensForClient(ClientDetailsEntity client) {
		TypedQuery<OAuth2AccessTokenEntity> queryA = manager.createNamedQuery("OAuth2AccessTokenEntity.getByClient", OAuth2AccessTokenEntity.class);
		queryA.setParameter("client", client);
	    List<OAuth2AccessTokenEntity> accessTokens = queryA.getResultList();
	    for (OAuth2AccessTokenEntity accessToken : accessTokens) {
	        removeAccessToken(accessToken);
        }
		TypedQuery<OAuth2RefreshTokenEntity> queryR = manager.createNamedQuery("OAuth2RefreshTokenEntity.getByClient", OAuth2RefreshTokenEntity.class);
		queryR.setParameter("client", client);
	    List<OAuth2RefreshTokenEntity> refreshTokens = queryR.getResultList();
	    for (OAuth2RefreshTokenEntity refreshToken : refreshTokens) {
	        removeRefreshToken(refreshToken);
        }	    
    }

	/* (non-Javadoc)
     * @see org.mitre.pushee.oauth.repository.OAuth2TokenRepository#getAccessTokensForClient(org.mitre.pushee.oauth.model.ClientDetailsEntity)
     */
    @Override
    public List<OAuth2AccessTokenEntity> getAccessTokensForClient(ClientDetailsEntity client) {
		TypedQuery<OAuth2AccessTokenEntity> queryA = manager.createNamedQuery("OAuth2AccessTokenEntity.getByClient", OAuth2AccessTokenEntity.class);
		queryA.setParameter("client", client);
	    List<OAuth2AccessTokenEntity> accessTokens = queryA.getResultList();
	    return accessTokens;
    }

	/* (non-Javadoc)
     * @see org.mitre.pushee.oauth.repository.OAuth2TokenRepository#getRefreshTokensForClient(org.mitre.pushee.oauth.model.ClientDetailsEntity)
     */
    @Override
    public List<OAuth2RefreshTokenEntity> getRefreshTokensForClient(ClientDetailsEntity client) {
		TypedQuery<OAuth2RefreshTokenEntity> queryR = manager.createNamedQuery("OAuth2RefreshTokenEntity.getByClient", OAuth2RefreshTokenEntity.class);
		queryR.setParameter("client", client);
	    List<OAuth2RefreshTokenEntity> refreshTokens = queryR.getResultList();
	    return refreshTokens;
    }

	/* (non-Javadoc)
     * @see org.mitre.pushee.oauth.repository.OAuth2TokenRepository#getExpiredAccessTokens()
     */
    @Override
    public List<OAuth2AccessTokenEntity> getExpiredAccessTokens() {
		TypedQuery<OAuth2AccessTokenEntity> queryA = manager.createNamedQuery("OAuth2AccessTokenEntity.getExpired", OAuth2AccessTokenEntity.class);
	    List<OAuth2AccessTokenEntity> accessTokens = queryA.getResultList();
	    return accessTokens;
    }

	/* (non-Javadoc)
     * @see org.mitre.pushee.oauth.repository.OAuth2TokenRepository#getExpiredRefreshTokens()
     */
    @Override
    public List<OAuth2RefreshTokenEntity> getExpiredRefreshTokens() {
		TypedQuery<OAuth2RefreshTokenEntity> queryR = manager.createNamedQuery("OAuth2RefreshTokenEntity.getExpired", OAuth2RefreshTokenEntity.class);
	    List<OAuth2RefreshTokenEntity> refreshTokens = queryR.getResultList();
	    return refreshTokens;
    }	

}
