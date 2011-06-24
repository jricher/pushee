package org.mitre.pushee.hub.repository;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hamcrest.CoreMatchers;
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
 *         Time: 11:08 AM
 */

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/spring/application-context.xml", "classpath:test-context.xml"})
public class FeedRepositoryTest  {


    private static final Long ID = 1L;

    @Autowired
    private FeedRepository repository;

    //Inject the same entity manager that is injected into the repository
    @PersistenceContext
    private EntityManager sharedManager;

    @Test
    @Rollback
    public void getAll() {
    	
    	Feed feed3 = new Feed();
    	//feed3.setId(3L);
    	feed3.setUrl("http://example.com/3");
    	feed3.setType(Feed.FeedType.RSS);
    	
    	Feed feed4 = new Feed();
    	//feed4.setId(4L);
    	feed4.setUrl("http://example.com/4");
    	feed4.setType(Feed.FeedType.ATOM);
    	
    	List<Feed> beforeList = new ArrayList<Feed>();
    	beforeList.add(repository.getById(ID)); //The repository already contains 
    	beforeList.add(repository.getById(2L)); //2 entries
    	beforeList.add(feed3);
    	beforeList.add(feed4);
    	
    	List<Feed> preGetAll = (List<Feed>)repository.getAll();
    	System.out.println("Before adding anything, contents of repository are : ");
        for (Feed s : preGetAll) {
        	System.out.println(s.toString());
        }
    	
    	repository.save(feed3);
    	repository.save(feed4);
        sharedManager.flush();
        
        List<Feed> retrievedList = (List<Feed>) repository.getAll();
        
        System.out.println("After saving additional Feeds, contents of lists are: ");
        System.out.println("Before List : ");
        for (Feed f : beforeList) {
        	System.out.println(f.toString());
        }
        
        System.out.println("Retrieved list (from repository.getAll()): ");
        for (Feed f : retrievedList) {
        	System.out.println(f.toString());
        }
        
        if (retrievedList.size() != beforeList.size()) {
        	fail("beforeList and retrievedList are not of the same size! retreved.size = " + retrievedList.size() + ", before.size = " + beforeList.size());
        }
        
        for (Feed f : retrievedList) {
        	if (!beforeList.contains(f)) {
        	
        		fail("beforeList and retrievedList contain unequal items! beforeList does not contain item " + f.toString());
        	} 
        }
    	//If we get to this point w/o failing, it's good!
    }

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
    public void removeById_existingFeed() {
    	
    	Long feedId = 22L;
    	
    	//Create a feed 
    	Feed newFeed = new Feed();
        newFeed.setUrl("http://example.com/3");
        newFeed.setType(Feed.FeedType.ATOM);
        newFeed.setId(feedId);
        repository.save(newFeed);
        sharedManager.flush();
        
        //Remove the feed
        repository.removeById(feedId);
        
        //Assert that the feed is no longer in the repository
        Feed doesNotExist = repository.getById(feedId);
        assertThat(doesNotExist, is(nullValue()));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void removeById_nonexistingFeed() {
    	repository.removeById(42L);
    }
    
    @Test
    public void remove_existingFeed() {
    	Long feedId = 22L;
    	
    	//Create a feed 
    	Feed newFeed = new Feed();
        newFeed.setUrl("http://example.com/3");
        newFeed.setType(Feed.FeedType.ATOM);
        newFeed.setId(feedId);
        Feed saved = repository.save(newFeed);
        sharedManager.flush();
        
        //Remove the feed
        repository.remove(saved);
        
        //Assert that the feed is no longer in the repository
        Feed doesNotExist = repository.getById(feedId);
        assertThat(doesNotExist, is(nullValue()));	
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void remove_nonexistingFeed() {
    	Feed newFeed = new Feed();
    	repository.remove(newFeed);
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
