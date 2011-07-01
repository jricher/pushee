/**
 * 
 */
package org.mitre.pushee.oauth.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

/**
 * @author jricher
 *
 */
@Entity
@NamedQueries({
	@NamedQuery(name = "OAuth2AccessTokenEntity.getByRefreshToken", query = "select a from OAuth2AccessTokenEntity a where a.refreshToken = :refreshToken")
})
public class OAuth2AccessTokenEntity extends OAuth2AccessToken {

	@ManyToOne
	@JoinColumn(name = "client_id")
	private ClientDetailsEntity client;
	
	@Basic
	OAuth2Authentication authentication; // the authentication that made this access
	
	/**
     * @return the authentication
     */
    public OAuth2Authentication getAuthentication() {
    	return authentication;
    }


	/**
     * @param authentication the authentication to set
     */
    public void setAuthentication(OAuth2Authentication authentication) {
    	this.authentication = authentication;
    }


	/**
     * @return the client
     */
    public ClientDetailsEntity getClient() {
    	return client;
    }


	/**
     * @param client the client to set
     */
    public void setClient(ClientDetailsEntity client) {
    	this.client = client;
    }


	/**
	 * 
	 */
	public OAuth2AccessTokenEntity() {
		// TODO Auto-generated constructor stub
	}

	
	/* (non-Javadoc)
     * @see org.springframework.security.oauth2.common.OAuth2AccessToken#getValue()
     */
    @Override
    @Id
    public String getValue() {
	    // TODO Auto-generated method stub
	    return super.getValue();
    }

	/* (non-Javadoc)
     * @see org.springframework.security.oauth2.common.OAuth2AccessToken#setValue(java.lang.String)
     */
    @Override
    public void setValue(String value) {
	    // TODO Auto-generated method stub
	    super.setValue(value);
    }

	/* (non-Javadoc)
     * @see org.springframework.security.oauth2.common.OAuth2AccessToken#getExpiration()
     */
    @Override
    @Basic
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getExpiration() {
	    // TODO Auto-generated method stub
	    return super.getExpiration();
    }

	/* (non-Javadoc)
     * @see org.springframework.security.oauth2.common.OAuth2AccessToken#setExpiration(java.util.Date)
     */
    @Override
    public void setExpiration(Date expiration) {
	    // TODO Auto-generated method stub
	    super.setExpiration(expiration);
    }

	/* (non-Javadoc)
     * @see org.springframework.security.oauth2.common.OAuth2AccessToken#getTokenType()
     */
    @Override
    @Basic
    public String getTokenType() {
	    // TODO Auto-generated method stub
	    return super.getTokenType();
    }

	/* (non-Javadoc)
     * @see org.springframework.security.oauth2.common.OAuth2AccessToken#setTokenType(java.lang.String)
     */
    @Override
    public void setTokenType(String tokenType) {
	    // TODO Auto-generated method stub
	    super.setTokenType(tokenType);
    }

	/* (non-Javadoc)
     * @see org.springframework.security.oauth2.common.OAuth2AccessToken#getRefreshToken()
     */
    @Override
    @ManyToOne
    @JoinColumn(name="refresh_token_value")
    public OAuth2RefreshToken getRefreshToken() {
	    // TODO Auto-generated method stub
	    return super.getRefreshToken();
    }

	/* (non-Javadoc)
     * @see org.springframework.security.oauth2.common.OAuth2AccessToken#setRefreshToken(org.springframework.security.oauth2.common.OAuth2RefreshToken)
     */
    @Override
    public void setRefreshToken(OAuth2RefreshToken refreshToken) {
	    // TODO Auto-generated method stub
	    super.setRefreshToken(refreshToken);
    }

	/* (non-Javadoc)
     * @see org.springframework.security.oauth2.common.OAuth2AccessToken#getScope()
     */
    @Override
    @ElementCollection
    public Set<String> getScope() {
	    // TODO Auto-generated method stub
	    return super.getScope();
    }

	/* (non-Javadoc)
     * @see org.springframework.security.oauth2.common.OAuth2AccessToken#setScope(java.util.Set)
     */
    @Override
    public void setScope(Set<String> scope) {
	    // TODO Auto-generated method stub
	    super.setScope(scope);
    }

	public boolean isExpired() {
		return getExpiration() == null || System.currentTimeMillis() > getExpiration().getTime();
	}
	
}
