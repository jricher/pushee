package org.mitre.pushee.hub.repository;

import org.hamcrest.CoreMatchers;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mitre.pushee.hub.model.Subscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collection;

import static org.junit.Assert.assertThat;

/**
 * @author mfranklin
 *         Date: 4/12/11
 *         Time: 11:09 AM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/spring/application-context.xml", "classpath:test-context.xml"})
public class SubscriberRepositoryTest {

    @Autowired
    private SubscriberRepository repository;


    @Test
    public void getSubscribers_validFeed_validResult() {
        Collection<Subscriber> subscribers = repository.getSubscribers(1L);
        assertThat(subscribers, CoreMatchers.<Object>notNullValue());
        assertThat(subscribers.size(), CoreMatchers.equalTo(2));
    }

    @Test
    public void getSubscribers_invalidFeed_emptyResult() {
        Collection<Subscriber> subscribers = repository.getSubscribers(2L);
        assertThat(subscribers, CoreMatchers.<Object>notNullValue());
        assertThat(subscribers.isEmpty(), CoreMatchers.is(true));
    }
}
