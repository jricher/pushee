package org.mitre.pushee.oauth.service.impl;

import java.util.Collection;
import java.util.List;

import org.mitre.pushee.oauth.model.ClientDetailsEntity;
import org.mitre.pushee.oauth.model.ClientDetailsEntityFactory;
import org.mitre.pushee.oauth.repository.OAuth2ClientRepository;
import org.mitre.pushee.oauth.repository.OAuth2TokenRepository;
import org.mitre.pushee.oauth.service.ClientDetailsEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;

@Service
public class DefaultOAuth2ClientDetailsEntityService implements ClientDetailsEntityService {

	@Autowired
	private OAuth2ClientRepository clientRepository;
	
	@Autowired
	private OAuth2TokenRepository tokenRepository;
	
	@Autowired
	private ClientDetailsEntityFactory clientFactory;
	
	/**
	 * Get the client for the given ID
	 */
	@Override
	public ClientDetailsEntity loadClientByClientId(String clientId) throws OAuth2Exception {
		if (!Strings.isNullOrEmpty(clientId)) {
			return clientRepository.getClientById(clientId);
		}
		
		return null;
	}
	
	/**
	 * Create a new client with the appropriate fields filled in
	 */
	@Override
    public ClientDetailsEntity createClient(String clientId, String clientSecret, 
    		List<String> scope, List<String> grantTypes, String redirectUri, List<GrantedAuthority> authorities, 
    		String name, String description, boolean allowRefresh, Long accessTokenTimeout, 
    		Long refreshTokenTimeout, String owner) {
		
		// TODO: check "owner" locally?

		ClientDetailsEntity client = clientFactory.createClient(clientId, clientSecret);
		client.setScope(scope);
		client.setAuthorizedGrantTypes(grantTypes);
		client.setWebServerRedirectUri(redirectUri);
		client.setAuthorities(authorities);
		client.setClientName(name);
		client.setClientDescription(description);
		client.setAllowRefresh(allowRefresh);
		client.setAccessTokenTimeout(accessTokenTimeout);
		client.setRefreshTokenTimeout(refreshTokenTimeout);
		client.setOwner(owner);

		clientRepository.saveClient(client);
		
		return client;
		
	}
	
	/**
	 * Delete a client and all its associated tokens
	 */
	@Override
    public void deleteClient(ClientDetailsEntity client) {
		
		// clean out any tokens that this client had issued
		tokenRepository.clearTokensForClient(client);
		
		// take care of the client itself
		clientRepository.deleteClient(client);
		
	}

	/**
	 * Update the oldClient with information from the newClient. The 
	 * id from oldClient is retained.
	 */
	@Override
    public ClientDetailsEntity updateClient(ClientDetailsEntity oldClient, ClientDetailsEntity newClient) {
		if (oldClient != null && newClient != null) {
			return clientRepository.updateClient(oldClient.getClientId(), newClient);
		}
		return null;
    }

	/**
	 * Get all clients in the system
	 */
	@Override
    public Collection<ClientDetailsEntity> getAllClients() {
		return clientRepository.getAllClients();
    }

}
