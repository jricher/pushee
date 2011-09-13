package org.mitre.pushee.hub.repository;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mitre.pushee.hub.model.Aggregator;
import org.mitre.pushee.hub.model.Feed;
import org.mitre.pushee.hub.model.Subscriber;
import org.mitre.pushee.hub.model.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

/**
 * Test of the JpaAggregatorRepository class
 * 
 * @author AANGANES
 *
 */

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/spring/application-context.xml", "classpath:test-context.xml"})
public class AggregatorRepositoryTest {

	@Autowired
	private AggregatorRepository repository;
	
	@PersistenceContext
	private EntityManager sharedManager;
	
	private Feed aggFeed1;
	private Feed aggFeed2;
	private Subscriber srcSub1;
	private Subscriber srcSub2;
	private Aggregator agg1;
	private Aggregator agg2;
	
	
	@Before
	public void setup() {
		//Use existing feeds and subscribers from test-data.sql so we don't have
		//to re-write FeedRepositoryTest and SubscriberRepositoryTest, etc.
		aggFeed1 = new Feed();
		aggFeed1.setId(1L);
		aggFeed1.setType(Feed.FeedType.RSS);
		aggFeed1.setUrl("http://example.com/1");

		srcSub1 = new Subscriber();
		srcSub1.setId(2L);
		srcSub1.setPostbackURL("http://example.com/sub/2");
		
		agg1 = new Aggregator();
		agg1.setId(1L);
		agg1.setDisplayName("Aggregator One");
		agg1.setAggregatorFeed(aggFeed1);
		agg1.setSourceSubscriber(srcSub1);
		
		aggFeed2 = new Feed();
		aggFeed2.setId(2L);
		aggFeed2.setType(Feed.FeedType.ATOM);
		aggFeed2.setUrl("http://example.com/2");
		
		srcSub2 = new Subscriber();
		srcSub2.setId(1L);
		srcSub2.setPostbackURL("http://example.com/sub/1");
		
		agg2 = new Aggregator();
		agg2.setId(2L);
		agg2.setDisplayName("Aggregator Two");
		agg2.setAggregatorFeed(aggFeed2);
		agg2.setSourceSubscriber(srcSub2);
		
		//Handle subscriptions
		
		//id 1, feed 1 to subscriber 1
		Subscription feedsubscript1 = new Subscription();
		feedsubscript1.setFeed(aggFeed1);
		feedsubscript1.setSubscriber(srcSub2);
		feedsubscript1.setId(1L);

		//id 2, feed 1 to subscriber 2
		Subscription subscript = new Subscription();
		subscript.setFeed(aggFeed1);
		subscript.setSubscriber(srcSub1);
		subscript.setId(2L);
		
		aggFeed1.addSubscription(feedsubscript1);
		aggFeed1.addSubscription(subscript);
		srcSub1.addSubscription(subscript);
		srcSub2.addSubscription(feedsubscript1);
		
	}
	
	@Test
	@Rollback
	public void getAll() {

		List<Aggregator> allAggs = Lists.newArrayList(agg1, agg2);
		
		List<Aggregator> retrieved = (List<Aggregator>) repository.getAll();
		
		if (allAggs.size() != retrieved.size()) {
			fail("Retrieved and expected are not of equal size!");
		}
		
		for (Aggregator s : allAggs) {
			System.out.println("Aggregator in saved list: " + s.toString());
		}
		
		for (Aggregator b : retrieved) {
			System.out.println("Aggregator in retrieved list: " + b.toString());
		}
		
		for (Aggregator a : retrieved) {
			if (!allAggs.contains(a)) {
				System.out.println("Got this item which is not contained in retrieved list: " + a.toString());
				fail("Retrieved contains unequal items to expected!");
			}
			else {
				System.out.println("Item " + a.toString() + " is contained in retrieved");
			}
		}
	}
	
	@Test
	public void getById_valid() {
		Aggregator retrieved = repository.getById(1L);
		assertThat(retrieved, is(not(nullValue())));
		assertThat(retrieved.getAggregatorFeed().getId(), equalTo(aggFeed1.getId()));
		assertThat(retrieved.getSourceSubscriber().getId(), equalTo(srcSub1.getId()));
	}
	
	@Test
	public void getById_invalid() {
		Aggregator nullAgg = repository.getById(47L);
		assertThat(nullAgg, is(nullValue()));
	}
	
	@Test
	@Rollback
	public void save_validNew() {
		Aggregator newAgg = new Aggregator();
		newAgg.setDisplayName("new aggregator");
		
		Aggregator saved = repository.save(newAgg);
		
		assertThat(saved, not(nullValue()));
		assertThat(saved, is(sameInstance(newAgg)));
		assertThat(saved.getId(), not(nullValue()));

	}
	
	@Test
	public void save_validExisting() {
		
	}
	
	@Test
	@Ignore
	public void remove_valid() {
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	@Ignore
	public void remove_invalid() {
		
	}
	
}
