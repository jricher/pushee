package org.mitre.pushee.oauth.model;

import org.mitre.pushee.oauth.model.ClientDetailsEntity.ClientDetailsEntityBuilder;

public interface ClientDetailsEntityFactory {
	
	public ClientDetailsEntity createClient(String clientId, String clientSecret);

}
