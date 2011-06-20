package org.mitre.pushee.hub.repository;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mitre.pushee.hub.model.Feed;
import org.mitre.pushee.hub.model.Publisher;
import org.mitre.pushee.hub.model.Subscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mfranklin
 *         Date: 4/12/11
 *         Time: 11:08 AM
 *         
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/spring/application-context.xml", "classpath:test-context.xml"})
public class PublisherRepositoryTest {

    private static final String URL = "http://example.com/pub/1";
    private static final Long ID = 1L;
    @Autowired
    private PublisherRepository repository;

    @PersistenceContext
    private EntityManager sharedManager;

    @Test
    @Rollback
    public void getAll() {
    	
    	Publisher pub2 = new Publisher();
    	pub2.setId(2L);
    	pub2.setCallbackURL("http://example.com/pub/2");
    	
    	Publisher pub3 = new Publisher();
    	pub3.setId(3L);
    	pub3.setCallbackURL("http://example.com/pub/3");
    	
    	List<Publisher> beforeList = new ArrayList<Publisher>();
    	beforeList.add(repository.getById(ID)); //Already in repository
    	beforeList.add(pub2);
    	beforeList.add(pub3);
    	
    	List<Publisher> preGetAll = (List<Publisher>)repository.getAll();
    	System.out.println("Before adding anything, contents of repository are : ");
        for (Publisher s : preGetAll) {
        	System.out.println(s.toString());
        	beforeList.add(s);
        }
    	
    	repository.save(pub2);
    	repository.save(pub3);
    	sharedManager.flush();
    	
    	List<Publisher> retrievedList = (List<Publisher>) repository.getAll();
    	
    	System.out.println("Before List : ");
        for (Publisher p : beforeList) {
        	System.out.println(p.toString());
        }
        
        System.out.println("Retrieved List : ");
        for (Publisher p : retrievedList) {
        	System.out.println(p.toString());
        }
    	
    	if (retrievedList.size() != beforeList.size()) {
    		fail("Retrieved list and before list are not the same size! Retrieved.size = " + retrievedList.size() + ", before.size = " + beforeList.size());
    	}
    	
    	for (Publisher p : retrievedList) {
    		if (!beforeList.contains(p)) {
    			fail("Retrieved list and before list contain unequal items! Before list does not contain " + p.toString());
    		}
    	}
    	
    }
    
    
    @Test
    public void getById_validId_validResult() {
        Publisher publisher = repository.getById(ID);
        assertThat(publisher, is(not(nullValue())));
        assertThat(publisher.getCallbackURL(), is(equalTo(URL)));
    }

    @Test
    public void getById_invalidId_nullResult() {
        Publisher publisher = repository.getById(10000L);
        assertThat(publisher, is(nullValue()));
    }
    @Test
    public void getByUrl_validUrl_validResult() {
        Publisher publisher = repository.getByUrl(URL);
        assertThat(publisher, is(not(nullValue())));
        assertThat(publisher.getId(), is(equalTo(ID)));
    }

    @Test
    public void getByUrl_invalidUrl_nullResult() {
        Publisher publisher = repository.getByUrl("http://bad.url/pub/1");
        assertThat(publisher, is(nullValue()));
    }  

    @Test
    public void remove_existingPublisher() {
    	Publisher newPublisher = new Publisher();
        newPublisher.setCallbackURL("http://example.com/pub/3");
        Publisher saved = repository.save(newPublisher);
        sharedManager.flush();
        
        repository.remove(saved);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void remove_nonexistingPublisher() {
    	Publisher notStored = new Publisher();
    	repository.remove(notStored);
    }
    
    @Test
    public void removeById_existingPublisher() {
    	
    	Long pubId = 22L;
    	
    	Publisher newPublisher = new Publisher();
        newPublisher.setCallbackURL("http://example.com/pub/3");
        newPublisher.setId(pubId);
        repository.save(newPublisher);
        sharedManager.flush();
        
        repository.removeById(pubId);
        Publisher doesNotExist = repository.getById(pubId);
        
        assertThat(doesNotExist, is(nullValue()));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void removeById_nonexistingPublisher() {
    	repository.removeById(42L);
    }
    
    @Test
    @Rollback
    public void save_validNew() {
        Publisher newPublisher = new Publisher();
        newPublisher.setCallbackURL("http://example.com/pub/3");
        Publisher saved = repository.save(newPublisher);
        sharedManager.flush();

        assertThat(saved, is(sameInstance(newPublisher)));
        assertThat(saved.getId(), not(nullValue()));
    }

    @Test
    @Rollback
    public void save_validExisting() {
        Publisher existing = new Publisher();
        existing.setId(ID);
        Publisher saved = repository.save(existing);
        sharedManager.flush();

        assertThat(saved, not(sameInstance(existing)));
        assertThat(saved.getId(), is(equalTo(ID)));
    }
}
