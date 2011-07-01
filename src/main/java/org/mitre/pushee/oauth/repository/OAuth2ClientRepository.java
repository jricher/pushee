package org.mitre.pushee.oauth.repository;

import org.mitre.pushee.oauth.model.ClientDetailsEntity;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.stereotype.Repository;

public interface OAuth2ClientRepository {

	public ClientDetailsEntity getClientById(String clientId);

	public ClientDetailsEntity saveClient(ClientDetailsEntity client);

	public void deleteClient(ClientDetailsEntity client);

	public ClientDetailsEntity updateClient(String clientId, ClientDetailsEntity client);

}
