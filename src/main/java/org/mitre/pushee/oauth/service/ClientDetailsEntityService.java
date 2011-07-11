package org.mitre.pushee.oauth.service;

import java.util.Collection;
import java.util.List;

import org.mitre.pushee.oauth.model.ClientDetailsEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.ClientDetailsService;

public interface ClientDetailsEntityService extends ClientDetailsService {

	public ClientDetailsEntity loadClientByClientId(String clientId) throws OAuth2Exception;

	public ClientDetailsEntity createClient(String clientId, String clientSecret, List<String> scope, List<String> grantTypes, String redirectUri, List<GrantedAuthority> authorities, String name, String description, boolean allowRefresh, Long accessTokenTimeout, Long refreshTokenTimeout, String owner);
	
	public void deleteClient(ClientDetailsEntity client);
	
	public ClientDetailsEntity updateClient(ClientDetailsEntity oldClient, ClientDetailsEntity newClient);

	public Collection<ClientDetailsEntity> getAllClients();
}
