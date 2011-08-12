package org.mitre.pushee.oauth.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mitre.pushee.oauth.model.ClientDetailsEntity;
import org.mitre.pushee.oauth.repository.impl.JpaOAuth2ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Unit test for JpaOauth2ClientRepository
 * 
 * @author AANGANES
 *
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/spring/application-context.xml", "classpath:test-context.xml"})
public class JpaOauth2ClientRepositoryTest {

	@Autowired
	private EntityManager manager;
	
	@PersistenceContext
	private JpaOAuth2ClientRepository repository;
	
	@Test
	@Ignore
	public void getClientById_valid() {
		
	}
	
	@Test
	@Ignore
	public void getClientById_invalid() {
		//should return null
	}
	
	@Test
	@Ignore
	public void saveClient() {
		
	}
	
	@Test
	@Ignore 
	public void deleteClient_valid() {
		
	}
	
	@Test(expected=IllegalArgumentException.class) 
	@Ignore
	public void deleteClient_invalid() {
		
	}
	
	@Test
	@Ignore
	public void updateClient() {
		
	}
	
	@Test
	@Ignore
	@Rollback
	public void getAll() {
		
		ClientDetailsEntity client1 = new ClientDetailsEntity();
		client1.setClientSecret("asdf");
		client1.setClientId("1");
		
		ClientDetailsEntity client2 = new ClientDetailsEntity();
		client2.setClientSecret("ghjkl");
		client2.setClientId("2");
		
		ClientDetailsEntity client3 = new ClientDetailsEntity();
		client3.setClientSecret("qwerty");
		client3.setClientId("3");
		
		
	}
	
}
