/**
 * 
 */
package org.mitre.pushee.oauth.model;

import java.util.Collections;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import com.google.common.collect.Sets;

/**
 * @author jricher
 * 
 */
@Entity
@Table(name="clientdetails")
public class ClientDetailsEntity implements ClientDetails {

	/**
	 * Create a blank ClientDetailsEntity
	 */
	public ClientDetailsEntity() {
		
	}
	
	/**
	 * Create a new ClientDetailsEntity with all of its fields filled in
     * @param clientId The OAuth2 client_id, must be unique to this client 
     * @param clientSecret the OAuth2 client_secret (optional)
     * @param scope the set of scopes allowed to be issued to this client
     * @param authorizedGrantTypes the OAuth2 grant types that this client is allowed to use  
     * @param webServerRedirectUri The pre-registered redirect URI if necessary
     * @param authorities the Spring Security authorities this client is given
     * @param clientName Human-readable name of the client (optional)
     * @param clientDescription Human-readable long description of the client (optional)
     * @param allowRefresh Whether to allow for issuance of refresh tokens or not (defaults to false)
     * @param accessTokenTimeout Lifetime of access tokens, in seconds (optional - leave null for no timeout)
     * @param refreshTokenTimeout Lifetime of refresh tokens, in seconds (optional - leave null for no timeout)
     * @param owner User ID of the person who registered this client (optional)
     */
    public ClientDetailsEntity(String clientId, String clientSecret, List<String> scope, 
    		List<String> authorizedGrantTypes, String webServerRedirectUri, 
    		List<GrantedAuthority> authorities, String clientName, String clientDescription, 
    		boolean allowRefresh, Long accessTokenTimeout, Long refreshTokenTimeout, String owner) {
	    this.clientId = clientId;
	    this.clientSecret = clientSecret;
	    this.scope = scope;
	    this.authorizedGrantTypes = authorizedGrantTypes;
	    this.webServerRedirectUri = webServerRedirectUri;
	    this.authorities = authorities;
	    this.clientName = clientName;
	    this.clientDescription = clientDescription;
	    this.allowRefresh = allowRefresh;
	    this.accessTokenTimeout = accessTokenTimeout;
	    this.refreshTokenTimeout = refreshTokenTimeout;
	    this.owner = owner;
    }

	@Id
	private String clientId;
	
	@Basic
	private String clientSecret;
	
	@ElementCollection
	@CollectionTable(
			name="scope",
			joinColumns=@JoinColumn(name="owner_id")
	)
	private List<String> scope;
	
	@ElementCollection
	@CollectionTable(
			name="granttypes",
			joinColumns=@JoinColumn(name="owner_id")
	)
	private List<String> authorizedGrantTypes;
	
	@Basic
	private String webServerRedirectUri;
	
	@ElementCollection
	@CollectionTable(
			name="authorities",
			joinColumns=@JoinColumn(name="owner_id")
	)
	private List<GrantedAuthority> authorities = Collections.emptyList();

	@Basic
	private String clientName;
	
	@Basic
	private String clientDescription;
	
	@Basic
	private boolean allowRefresh = false; // do we allow refresh tokens for this client?
	
	@Basic
	private Long accessTokenTimeout; // in seconds
	
	@Basic
	private Long refreshTokenTimeout; // in seconds
	
	@Basic
	private String owner; // userid of who registered it

	// TODO:
	/*
	private boolean allowMultipleAccessTokens; // do we allow multiple access tokens, or not?
	private boolean reuseRefreshToken; // do we let someone reuse a refresh token?
	*/
	
	/**
     * @return the clientId
     */
    public String getClientId() {
    	return clientId;
    }

	/**
     * @param clientId the clientId to set
     */
    public void setClientId(String clientId) {
    	this.clientId = clientId;
    }

	/**
     * @return the clientSecret
     */
    public String getClientSecret() {
    	return clientSecret;
    }

	/**
     * @param clientSecret the clientSecret to set
     */
    public void setClientSecret(String clientSecret) {
    	this.clientSecret = clientSecret;
    }

	/**
     * @return the scope
     */
    public List<String> getScope() {
    	return scope;
    }

	/**
     * @param scope the scope to set
     */
    public void setScope(List<String> scope) {
    	this.scope = scope;
    }

	/**
     * @return the authorizedGrantTypes
     */
    public List<String> getAuthorizedGrantTypes() {
    	return authorizedGrantTypes;
    }

	/**
     * @param authorizedGrantTypes the authorizedGrantTypes to set
     */
    public void setAuthorizedGrantTypes(List<String> authorizedGrantTypes) {
    	this.authorizedGrantTypes = authorizedGrantTypes;
    }

	/**
     * @return the webServerRedirectUri
     */
    public String getWebServerRedirectUri() {
    	return webServerRedirectUri;
    }

	/**
     * @param webServerRedirectUri the webServerRedirectUri to set
     */
    public void setWebServerRedirectUri(String webServerRedirectUri) {
    	this.webServerRedirectUri = webServerRedirectUri;
    }

	/**
     * @return the authorities
     */
    public List<GrantedAuthority> getAuthorities() {
    	return authorities;
    }

	/**
     * @param authorities the authorities to set
     */
    public void setAuthorities(List<GrantedAuthority> authorities) {
    	this.authorities = authorities;
    }

	/**
	 * If the clientSecret is not null, then it is always required.
     */
    @Override
    public boolean isSecretRequired() {
	    return getClientSecret() != null;
    }

	/**
	 * If the scope list is not null or empty, then this client has been scoped.
     */
    @Override
    public boolean isScoped() {
    	return getScope() != null && !getScope().isEmpty();
    }

	/**
     * @return the clientName
     */
    public String getClientName() {
    	return clientName;
    }

	/**
     * @param clientName the clientName to set
     */
    public void setClientName(String clientName) {
    	this.clientName = clientName;
    }

	/**
     * @return the clientDescription
     */
    public String getClientDescription() {
    	return clientDescription;
    }

	/**
     * @param clientDescription the clientDescription to set
     */
    public void setClientDescription(String clientDescription) {
    	this.clientDescription = clientDescription;
    }

	/**
     * @return the allowRefresh
     */
    public boolean isAllowRefresh() {
    	return allowRefresh;
    }

	/**
     * @param allowRefresh the allowRefresh to set
     */
    public void setAllowRefresh(boolean allowRefresh) {
    	this.allowRefresh = allowRefresh;
    }

	/**
     * @return the accessTokenTimeout
     */
    public Long getAccessTokenTimeout() {
    	return accessTokenTimeout;
    }

	/**
     * @param accessTokenTimeout the accessTokenTimeout to set
     */
    public void setAccessTokenTimeout(Long accessTokenTimeout) {
    	this.accessTokenTimeout = accessTokenTimeout;
    }

	/**
     * @return the refreshTokenTimeout
     */
    public Long getRefreshTokenTimeout() {
    	return refreshTokenTimeout;
    }

	/**
     * @param refreshTokenTimeout the refreshTokenTimeout to set
     */
    public void setRefreshTokenTimeout(Long refreshTokenTimeout) {
    	this.refreshTokenTimeout = refreshTokenTimeout;
    }

	/**
     * @return the owner
     */
    public String getOwner() {
    	return owner;
    }

	/**
     * @param owner the owner to set
     */
    public void setOwner(String owner) {
    	this.owner = owner;
    }

	/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	    return "ClientDetailsEntity [" + (clientId != null ? "clientId=" + clientId + ", " : "") + (scope != null ? "scope=" + scope + ", " : "") + (clientName != null ? "clientName=" + clientName + ", " : "") + (owner != null ? "owner=" + owner : "") + "]";
    }

	/* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((clientId == null) ? 0 : clientId.hashCode());
	    return result;
    }

	/* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	    if (this == obj) {
		    return true;
	    }
	    if (obj == null) {
		    return false;
	    }
	    if (getClass() != obj.getClass()) {
		    return false;
	    }
	    ClientDetailsEntity other = (ClientDetailsEntity) obj;
	    if (clientId == null) {
		    if (other.clientId != null) {
			    return false;
		    }
	    } else if (!clientId.equals(other.clientId)) {
		    return false;
	    }
	    return true;
    }

	
	
}
