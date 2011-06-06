package org.mitre.pushee.hub.repository;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
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
