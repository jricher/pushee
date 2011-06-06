package org.mitre.pushee.hub.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mitre.pushee.hub.model.Feed;
import org.mitre.pushee.hub.model.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

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
