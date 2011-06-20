package org.mitre.pushee.hub.repository;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hamcrest.CoreMatchers;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mitre.pushee.hub.model.Feed;
import org.mitre.pushee.hub.model.Subscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mfranklin
 *         Date: 4/12/11
 *         Time: 11:09 AM
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/spring/application-context.xml", "classpath:test-context.xml"})
public class SubscriberRepositoryTest {
    
    private static final Long ID = 1L;
    private static final String URL = "http://example.com/sub/1";

    @Autowired
    private SubscriberRepository repository;
    
    @PersistenceContext
    private EntityManager sharedManager;


    @Test
    @Rollback
    public void getAll() {
    	
    	Subscriber sub2 = new Subscriber();
    	sub2.setId(2L);
    	sub2.setPostbackURL("http://example.com/sub/2");
    	
    	Subscriber sub3 = new Subscriber();
    	sub3.setId(3L);
    	sub3.setPostbackURL("http://example.com/sub/3");
    	
    	List<Subscriber> beforeList = new ArrayList<Subscriber>();
    	beforeList.add(repository.getByUrl(URL)); //This one is already in the repository
    	beforeList.add(sub2);
    	beforeList.add(sub3);
    	
    	repository.save(sub2);
    	repository.save(sub3);
    	sharedManager.flush();
    	
    	List<Subscriber> retrievedList = (List<Subscriber>) repository.getAll();
    	
    	System.out.println("Before List : ");
        for (Subscriber s : beforeList) {
        	System.out.println(s.toString());
        }
        
        System.out.println("Retrieved List : ");
        for (Subscriber s : retrievedList) {
        	System.out.println(s.toString());
        }
    	
    	if (retrievedList.size() != beforeList.size()) {
    		fail("Retrieved list and before list are not the same size! Retrieved.size = " + retrievedList.size() + ", before.size = " + beforeList.size());
    	}
    	
    	for (Subscriber s : retrievedList) {
    		if (!beforeList.contains(s)) {
    			fail("Retrieved list and before list contain unequal items! Beforelist does not contain " + s.toString());
    		}
    	}
    	
    }
    
    
    @Test
    public void getSubscribers_validFeed_validResult() {
        Collection<Subscriber> subscribers = repository.getSubscribers(ID);
        assertThat(subscribers, is(not(nullValue())));
        assertThat(subscribers.size(), is(equalTo(2)));
    }

    @Test
    public void getSubscribers_invalidFeed_emptyResult() {
    	Long subID = 2L;
        Collection<Subscriber> subscribers = repository.getSubscribers(subID);
        assertThat(subscribers, CoreMatchers.<Object>notNullValue());
        assertThat(subscribers.isEmpty(), is(true));
    }
    
   
    @Test
    public void getByUrl_validUrl_validResult() {
        Subscriber subscriber = repository.getByUrl(URL);
        assertThat(subscriber, is(not(nullValue())));
        assertThat(subscriber.getId(), is(equalTo(ID)));
    }

    @Test
    public void getByUrl_invalidUrl_nullResult() {
        Subscriber subscriber = repository.getByUrl("http://bad.url/sub/1");
        assertThat(subscriber, is(nullValue()));
    }
    
    @Test
    @Rollback
    public void remove_existingSubscriber() {
    	
    	String theUrl = "http://example.com/pub/3";
    	
    	Subscriber newSubscriber = new Subscriber();
        newSubscriber.setPostbackURL(theUrl);
        Subscriber saved = repository.save(newSubscriber);
        sharedManager.flush();
        
        repository.remove(saved);
        Subscriber doesNotExist = repository.getByUrl(theUrl);
        
        assertThat(doesNotExist, is(nullValue()));
    }
    
    @Test(expected = IllegalArgumentException.class)
    @Rollback
    public void remove_nonexistingSubscriber() {
    	Subscriber newSubscriber = new Subscriber();
    	repository.remove(newSubscriber);
    }
    
    @Test
    @Rollback
    public void removeById_existingSubscriber() {
    	
    	Long theId = 22L;
    	String theUrl = "http://example.com/pub/3";
    	
    	Subscriber newSubscriber = new Subscriber();
        newSubscriber.setPostbackURL(theUrl);
        newSubscriber.setId(theId);
        repository.save(newSubscriber);
        sharedManager.flush();
        
        repository.removeById(theId);
        Subscriber doesNotExist = repository.getByUrl(theUrl);
        
        assertThat(doesNotExist, is(nullValue()));
    }
    
    @Test(expected = IllegalArgumentException.class)
    @Rollback
    public void removeById_nonexistingSubscriber() {
    	repository.removeById(42L);
    }

    @Test
    @Rollback
    public void save_validNew() {
        Subscriber newSubscriber = new Subscriber();
        newSubscriber.setPostbackURL("http://example.com/pub/3");
        Subscriber saved = repository.save(newSubscriber);
        sharedManager.flush();

        assertThat(saved, is(sameInstance(newSubscriber)));
        assertThat(saved.getId(), not(nullValue()));
    }

    @Test
    @Rollback
    public void save_validExisting() {
        Subscriber existing = new Subscriber();
        existing.setId(ID);
        Subscriber saved = repository.save(existing);
        sharedManager.flush();

        assertThat(saved, not(sameInstance(existing)));
        assertThat(saved.getId(), is(equalTo(ID)));
    }
}
