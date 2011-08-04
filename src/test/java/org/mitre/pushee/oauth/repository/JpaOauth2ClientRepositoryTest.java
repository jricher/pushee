package org.mitre.pushee.oauth.repository;

import static org.easymock.EasyMock.createNiceMock;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mitre.pushee.oauth.repository.impl.JpaOAuth2ClientRepository;

/**
 * Unit test for JpaOauth2ClientRepository
 * 
 * @author AANGANES
 *
 */
public class JpaOauth2ClientRepositoryTest {

	private EntityManager manager;
	private JpaOAuth2ClientRepository repository;
	
	@Before
	public void setup() {
		manager = createNiceMock(EntityManager.class);
		repository = new JpaOAuth2ClientRepository(manager);
	}
	
	@Test
	@Ignore
	public void getClientById_valid() {
		
	}
	
	@Test
	@Ignore
	public void getClientById_invalid() {
		//should return null?
	}
	
	@Test
	@Ignore
	public void saveClient() {
		
	}
	
	@Test
	@Ignore 
	public void deleteClient_valid() {
		
	}
	
	@Test
	@Ignore 
	public void deleteClient_invalid() {
		
	}
	
	@Test
	@Ignore
	public void updateClient_valid() {
		
	}
	
	@Test
	@Ignore
	public void updateClient_invalid() {
		
	}
	
	@Test
	@Ignore
	public void getAll() {
		
	}
	
}
