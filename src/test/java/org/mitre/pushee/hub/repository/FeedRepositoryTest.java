package org.mitre.pushee.hub.repository;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mitre.pushee.hub.model.Feed;
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
 */

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/spring/application-context.xml", "classpath:test-context.xml"})
public class FeedRepositoryTest  {


    private static final long ID = 1L;

    @Autowired
    private FeedRepository repository;

    //Inject the same entity manager that is injected into the repository
    @PersistenceContext
    private EntityManager sharedManager;


    @Test
    public void getById_validId_validResult() {
        Feed feed = repository.getById(ID);
        assertThat(feed, CoreMatchers.notNullValue());
        assertThat(feed.getUrl(), is(equalTo("http://example.com/1")));
        assertThat(feed.getType(), is(equalTo(Feed.FeedType.RSS)));
    }

    @Test
    public void getById_invalidId_nullResult() {
        Feed feed = repository.getById(1000L);
        assertThat(feed, is(nullValue()));
    }

    @Test
    public void getByUrl_validUrl_validResult() {
        Feed feed = repository.getByUrl("http://example.com/1");
        assertThat(feed, is(notNullValue()));
        assertThat(feed.getId(), is(equalTo(ID)));
        assertThat(feed.getType(), is(equalTo(Feed.FeedType.RSS)));
    }

    @Test
    public void getByUrl_invalidUrl_nullResult() {
        Feed feed = repository.getByUrl("http://bad.url/1");
        assertThat(feed, is(nullValue()));
    }

    @Test
    @Rollback
    public void save_validNew() {
        Feed newFeed = new Feed();
        newFeed.setUrl("http://example.com/3");
        newFeed.setType(Feed.FeedType.ATOM);
        Feed saved = repository.save(newFeed);
        sharedManager.flush();

        assertThat(saved, is(sameInstance(newFeed)));
        assertThat(saved.getId(), not(nullValue()));
    }

    @Test
    @Rollback
    public void save_validExisting() {
        Feed existing = new Feed();
        existing.setId(ID);
        existing.setType(Feed.FeedType.ATOM);
        Feed saved = repository.save(existing);
        sharedManager.flush();

        assertThat(saved, not(sameInstance(existing)));
        assertThat(saved.getId(), is(equalTo(ID)));
        assertThat(saved.getType(), is(equalTo(Feed.FeedType.ATOM)));
    }


}
