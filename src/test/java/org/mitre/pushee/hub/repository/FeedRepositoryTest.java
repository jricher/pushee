package org.mitre.pushee.hub.repository;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mitre.pushee.hub.model.Feed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author mfranklin
 *         Date: 4/12/11
 *         Time: 11:08 AM
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/spring/application-context.xml", "classpath:test-context.xml"})
public class FeedRepositoryTest {

    @Autowired
    private FeedRepository repository;


    @Test
    public void getById_validId_validResult() {
        Feed feed = repository.getById(1L);
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
        assertThat(feed.getId(), is(equalTo(1L)));
        assertThat(feed.getType(), is(equalTo(Feed.FeedType.RSS)));
    }

    @Test
    public void getByUrl_invalidUrl_nullResult() {
        Feed feed = repository.getByUrl("http://bad.url/1");
        assertThat(feed, is(nullValue()));
    }


}
