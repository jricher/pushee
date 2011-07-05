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
import javax.persistence.Temporal;

import org.springframework.security.oauth2.common.ExpiringOAuth2RefreshToken;

/**
 * @author jricher
 *
 */
@Entity
public class OAuth2RefreshTokenEntity extends ExpiringOAuth2RefreshToken {

	private ClientDetailsEntity client;

	private  Set<String> scope; // we save the scope issued to the refresh token so that we can reissue a new access token	
	
	/**
	 * 
	 */
	public OAuth2RefreshTokenEntity() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
     * @see org.springframework.security.oauth2.common.OAuth2RefreshToken#getValue()
     */
    @Override
    @Id
    public String getValue() {
	    // TODO Auto-generated method stub
	    return super.getValue();
    }

	/* (non-Javadoc)
     * @see org.springframework.security.oauth2.common.OAuth2RefreshToken#setValue(java.lang.String)
     */
    @Override
    public void setValue(String value) {
	    // TODO Auto-generated method stub
	    super.setValue(value);
    }

	/* (non-Javadoc)
     * @see org.springframework.security.oauth2.common.ExpiringOAuth2RefreshToken#getExpiration()
     */
    @Override
    @Basic
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getExpiration() {
	    // TODO Auto-generated method stub
	    return super.getExpiration();
    }

	/* (non-Javadoc)
     * @see org.springframework.security.oauth2.common.ExpiringOAuth2RefreshToken#setExpiration(java.util.Date)
     */
    @Override
    public void setExpiration(Date expiration) {
	    // TODO Auto-generated method stub
	    super.setExpiration(expiration);
    }
	
	public boolean isExpired() {
		return getExpiration() == null || System.currentTimeMillis() > getExpiration().getTime();
	}
	
	/**
     * @return the client
     */
	@ManyToOne
	@JoinColumn(name = "client_id")
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
     * @return the scope
     */
	@ElementCollection
    public Set<String> getScope() {
    	return scope;
    }

	/**
     * @param scope the scope to set
     */
    public void setScope(Set<String> scope) {
    	this.scope = scope;
    }
    

    
}
