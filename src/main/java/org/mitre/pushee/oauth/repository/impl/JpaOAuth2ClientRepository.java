/**
 * 
 */
package org.mitre.pushee.oauth.repository.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.mitre.pushee.hub.repository.util.JpaUtil;
import org.mitre.pushee.oauth.model.ClientDetailsEntity;
import org.mitre.pushee.oauth.repository.OAuth2ClientRepository;
import org.springframework.stereotype.Repository;

/**
 * @author jricher
 *
 */
@Repository
public class JpaOAuth2ClientRepository implements OAuth2ClientRepository {

	@PersistenceContext
	private EntityManager manager;
	
	/* (non-Javadoc)
	 * @see org.mitre.pushee.oauth.repository.OAuth2ClientRepository#getClientById(java.lang.String)
	 */
	@Override
	public ClientDetailsEntity getClientById(String clientId) {
		return manager.find(ClientDetailsEntity.class, clientId);
	}

	/* (non-Javadoc)
	 * @see org.mitre.pushee.oauth.repository.OAuth2ClientRepository#saveClient(org.mitre.pushee.oauth.model.ClientDetailsEntity)
	 */
	@Override
	public ClientDetailsEntity saveClient(ClientDetailsEntity client) {
		return JpaUtil.saveOrUpdate(client.getClientId(), manager, client);
	}

	/* (non-Javadoc)
	 * @see org.mitre.pushee.oauth.repository.OAuth2ClientRepository#deleteClient(org.mitre.pushee.oauth.model.ClientDetailsEntity)
	 */
	@Override
	public void deleteClient(ClientDetailsEntity client) {
		ClientDetailsEntity found = getClientById(client.getClientId());
		if (found != null) {
			manager.remove(found);
		} else {
			throw new IllegalArgumentException("Client not found: " + client);
		}
	}

	@Override
    public ClientDetailsEntity updateClient(String clientId, ClientDetailsEntity client) {
	    return JpaUtil.saveOrUpdate(clientId, manager, client);
    }

}
