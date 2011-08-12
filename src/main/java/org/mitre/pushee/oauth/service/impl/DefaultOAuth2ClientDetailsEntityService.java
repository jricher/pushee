package org.mitre.pushee.oauth.service.impl;

import java.util.Collection;
import java.util.List;

import org.mitre.pushee.oauth.model.ClientDetailsEntity;
import org.mitre.pushee.oauth.model.ClientDetailsEntityFactory;
import org.mitre.pushee.oauth.repository.OAuth2ClientRepository;
import org.mitre.pushee.oauth.repository.OAuth2TokenRepository;
import org.mitre.pushee.oauth.service.ClientDetailsEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
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
	
	public DefaultOAuth2ClientDetailsEntityService() {
		
	}
	
	public DefaultOAuth2ClientDetailsEntityService(OAuth2ClientRepository clientRepository, 
			OAuth2TokenRepository tokenRepository, ClientDetailsEntityFactory clientFactory) {
		this.clientRepository = clientRepository;
		this.tokenRepository = tokenRepository;
		this.clientFactory = clientFactory;
	}
	
	/**
	 * Get the client for the given ID
	 */
	@Override
	public ClientDetailsEntity loadClientByClientId(String clientId) throws OAuth2Exception, InvalidClientException, IllegalArgumentException {
		if (!Strings.isNullOrEmpty(clientId)) {
			ClientDetailsEntity client = clientRepository.getClientById(clientId);
			if (client == null) {
				throw new InvalidClientException("Client with id " + clientId + " was not found");
			}
			else {
				return client;
			}
		}
		
		throw new IllegalArgumentException("Client id must not be empty!");
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
    public void deleteClient(ClientDetailsEntity client) throws InvalidClientException {
		
		if (clientRepository.getClientById(client.getClientId()) == null) {
			throw new InvalidClientException("Client with id " + client.getClientId() + " was not found");
		}
		
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
    public ClientDetailsEntity updateClient(ClientDetailsEntity oldClient, ClientDetailsEntity newClient) throws IllegalArgumentException {
		if (oldClient != null && newClient != null) {
			return clientRepository.updateClient(oldClient.getClientId(), newClient);
		}
		throw new IllegalArgumentException("Neither old client or new client can be null!");
    }

	/**
	 * Get all clients in the system
	 */
	@Override
    public Collection<ClientDetailsEntity> getAllClients() {
		return clientRepository.getAllClients();
    }

}
