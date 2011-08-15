package org.mitre.pushee.oauth.repository;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hamcrest.CoreMatchers;
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

	@PersistenceContext
	private EntityManager manager;
	
	//Inject the same entity manager that is injected into the repository
	@Autowired
	private JpaOAuth2ClientRepository repository;
	
	@Test
	public void getClientById_valid() {
		ClientDetailsEntity existingClient = repository.getClientById("1");
		assertThat(existingClient, CoreMatchers.notNullValue());
		assertThat(existingClient.getClientSecret(), CoreMatchers.equalTo("asdf"));
		assertThat(existingClient.getWebServerRedirectUri(), CoreMatchers.equalTo("http://example.com/client1"));
	}
	
	@Test
	public void getClientById_invalid() {
		//should return null
		ClientDetailsEntity nullClient = repository.getClientById("notAClientID");
		assertThat(nullClient, is(nullValue()));
	}
	
	@Test
	@Rollback
	public void saveClient() {
		ClientDetailsEntity newClient = new ClientDetailsEntity();
		newClient.setClientId("newId");
		newClient.setClientName("A new client");
		newClient.setClientSecret("a new secret");
		newClient.setScope(new ArrayList<String>());
		
		ClientDetailsEntity saved = repository.saveClient(newClient);
		manager.flush();
		
		assertThat(saved, equalTo(newClient));
	}
	
	@Test
	@Rollback
	public void deleteClient_valid() {
		ClientDetailsEntity existingClient = repository.getClientById("1");
		repository.deleteClient(existingClient);
		manager.flush();
		
		ClientDetailsEntity nullClient = repository.getClientById("1");
		assertThat(nullClient, is(nullValue()));
	}
	
	@Test(expected=IllegalArgumentException.class) 
	public void deleteClient_invalid() {
		ClientDetailsEntity newclient = new ClientDetailsEntity();
		newclient.setClientId("notInRepository");
		repository.deleteClient(newclient);
	}
	
	@Test
	@Rollback
	public void updateClient() {
		ClientDetailsEntity existing = new ClientDetailsEntity();
		existing.setClientId("1"); //This client is already in the test database
		existing.setClientSecret("newsecret"); //Set a new client secret
		
		ClientDetailsEntity saved = repository.saveClient(existing);
		manager.flush();
		
		assertThat(saved, not(sameInstance(existing)));
		assertThat(saved.getClientId(), is(equalTo("1")));
		assertThat(saved.getClientSecret(), is(equalTo("newsecret")));
	}
	
	@Test
	@Rollback
	public void getAll() {
		
		ClientDetailsEntity client3 = new ClientDetailsEntity();
		client3.setClientSecret("qwerty");
		client3.setClientId("3");
		repository.saveClient(client3);
		
		ClientDetailsEntity client4 = new ClientDetailsEntity();
		client4.setClientSecret("jabberwocky");
		client4.setClientId("4");
		repository.saveClient(client4);
		
		ArrayList<ClientDetailsEntity> allClients = new ArrayList<ClientDetailsEntity>();
		allClients.add(repository.getClientById("1")); //Test database already contains
		allClients.add(repository.getClientById("2")); //2 entries
		allClients.add(client3);
		allClients.add(client4);
		manager.flush();
		
		Collection<ClientDetailsEntity> retrieved = repository.getAllClients();
		
		if (retrieved.size() != allClients.size()) {
			fail("Saved list and retrieved list are of unequal sizes!");
		}
		
		for (ClientDetailsEntity client : retrieved) {
			if (!allClients.contains(client)) {
				fail("Saved list and retrieved list contain unequal items");
			}
		}

	}
	
}
