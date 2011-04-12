package org.mitre.pushee.hub.repository;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mitre.pushee.hub.model.Feed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
    public void getById() {
        Feed feed = repository.get(1);
        assertThat(feed, CoreMatchers.notNullValue());
        assertThat(feed.getUrl(), CoreMatchers.equalTo("http://example.com/1"));
        assertThat(feed.getType(), CoreMatchers.equalTo(Feed.FeedType.RSS));
    }

}
