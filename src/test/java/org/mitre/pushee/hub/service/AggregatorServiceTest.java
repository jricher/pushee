package org.mitre.pushee.hub.service;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mitre.pushee.hub.exception.AggregatorNotFoundException;
import org.mitre.pushee.hub.model.Aggregator;
import org.mitre.pushee.hub.model.Feed;
import org.mitre.pushee.hub.model.Subscriber;
import org.mitre.pushee.hub.model.Subscription;
import org.mitre.pushee.hub.repository.AggregatorRepository;
import org.mitre.pushee.hub.service.impl.DefaultAggregatorService;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.Lists;

/**
 * Test of the DefaultAggregatorService class
 * 
 * @author AANGANES
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/spring/application-context.xml", "classpath:test-context.xml"})
public class AggregatorServiceTest {

	private AggregatorService aggService;
	private AggregatorRepository repository;
	
	private Feed aggFeed1;
	private Feed aggFeed2;
	private Subscriber srcSub1;
	private Subscriber srcSub2;
	private Aggregator agg1;
	private Aggregator agg2;
	
	@Before
	public void setup() {
		repository = createNiceMock(AggregatorRepository.class);
		aggService = new DefaultAggregatorService(repository);
		
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
	public void getAll() {
		List<Aggregator> allAggs = Lists.newArrayList(agg1, agg2);
		
		expect(repository.getAll()).andReturn(allAggs).once();
		replay(repository);
		
		List<Aggregator> retrieved = (List<Aggregator>) aggService.getAll();
		
		verify(repository);
		
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
		expect(repository.getById(1L)).andReturn(agg1).once();
		replay(repository);
		Aggregator retrieved = aggService.getById(1L);
		verify(repository);
		assertThat(retrieved, is(not(nullValue())));
		assertThat(retrieved.getAggregatorFeed().getId(), equalTo(aggFeed1.getId()));
		assertThat(retrieved.getSourceSubscriber().getId(), equalTo(srcSub1.getId()));
	}
	
	@Test
	public void getById_invalid() {
		expect(repository.getById(47L)).andReturn(null).once();
		replay(repository);
		Aggregator nullAgg = aggService.getById(47L);
		verify(repository);
		assertThat(nullAgg, is(nullValue()));
	}
	
	@Test
	public void getByFeedUrl_valid() {
		expect(repository.getByFeedUrl(aggFeed1.getUrl())).andReturn(agg1).once();
		replay(repository);
		Aggregator retrieved = aggService.getByFeedUrl(aggFeed1.getUrl());
		verify(repository);
		assertThat(retrieved, is(not(nullValue())));
		assertThat(retrieved.getAggregatorFeed().getId(), equalTo(aggFeed1.getId()));
		assertThat(retrieved.getSourceSubscriber().getId(), equalTo(srcSub1.getId()));
	}
	
	@Test
	public void getByFeedUrl_invalid() {
		expect(repository.getByFeedUrl("BadURl.com")).andReturn(null).once();
		replay(repository);
		Aggregator nullAgg = aggService.getByFeedUrl("BadURl.com");
		verify(repository);
		assertThat(nullAgg, is(nullValue()));
	}
	
	@Test
	public void getBySubscriberUrl_valid() {
		expect(repository.getBySubscriberUrl(srcSub1.getPostbackURL())).andReturn(agg1).once();
		replay(repository);
		Aggregator retrieved = aggService.getBySubscriberUrl(srcSub1.getPostbackURL());
		verify(repository);
		assertThat(retrieved, is(not(nullValue())));
		assertThat(retrieved.getAggregatorFeed().getId(), equalTo(aggFeed1.getId()));
		assertThat(retrieved.getSourceSubscriber().getId(), equalTo(srcSub1.getId()));
	}
	
	@Test
	public void getBySubscriberUrl_invalid() {
		expect(repository.getBySubscriberUrl("BadURl.com")).andReturn(null).once();
		replay(repository);
		Aggregator nullAgg = repository.getBySubscriberUrl("BadURl.com");
		verify(repository);
		assertThat(nullAgg, is(nullValue()));
	}
	
	@Test
	@Rollback
	public void save_validNew() {
		Aggregator newAgg = new Aggregator();
		newAgg.setDisplayName("new aggregator");
		Aggregator savedNewAgg = new Aggregator();
		savedNewAgg.setDisplayName("new aggregator");
		savedNewAgg.setId(5L);
		
		expect(repository.save(newAgg)).andReturn(savedNewAgg).once();
		replay(repository);
		
		Aggregator saved = aggService.save(newAgg);
		
		verify(repository);
		
		assertThat(saved, not(nullValue()));
		assertThat(saved.getDisplayName(), equalTo(newAgg.getDisplayName()));
		assertThat(saved.getId(), not(nullValue()));
	}
	
	@Test
	@Rollback
	public void save_validExisting() {
		agg1.setDisplayName("A new display name!");
		
		expect(repository.save(agg1)).andReturn(agg1).once();
		replay(repository);
		
		Aggregator saved = aggService.save(agg1);
		
		verify(repository);
		
		assertThat(saved, not(nullValue()));
		assertThat(saved.getId(), equalTo(agg1.getId()));
		assertThat(saved.getDisplayName(), equalTo(agg1.getDisplayName()));
	}
	
	@Test
	@Rollback
	public void removeById_valid() {
		repository.removeById(agg1.getId());
		expectLastCall();
		replay(repository);
		
		aggService.removeById(agg1.getId());
		verify(repository);
		
		Aggregator nullagg = repository.getById(agg1.getId());
		assertThat(nullagg, is(nullValue()));
	}
	
	@Test(expected = IllegalArgumentException.class)
	@Rollback
	public void removeById_invalid() {
		repository.removeById(42L);
		expectLastCall().andThrow(new IllegalArgumentException());
		replay(repository);
		
		aggService.removeById(42L);
		verify(repository);
	}
	
	@Test
	@Rollback
	public void remove_valid() {
		
		repository.remove(agg1);
		expectLastCall();
		replay(repository);
		
		aggService.remove(agg1);
		verify(repository);
		
		Aggregator nullagg = repository.getById(agg1.getId());
		
		assertThat(nullagg, is(nullValue()));
	}
	
	@Test(expected = IllegalArgumentException.class)
	@Rollback
	public void remove_invalid() {
		Aggregator doesNotExist = new Aggregator();
		doesNotExist.setId(42L);
		doesNotExist.setAggregatorFeed(aggFeed1);
		doesNotExist.setSourceSubscriber(srcSub1);
		
		repository.remove(doesNotExist);
		expectLastCall().andThrow(new IllegalArgumentException());
		replay(repository);
		
		aggService.remove(doesNotExist);
		verify(repository);
	}
	
	@Test
	public void getExistingAggregator_valid() {
		expect(repository.getById(1L)).andReturn(agg1).once();
		replay(repository);
		Aggregator retrieved = aggService.getExistingAggregator(1L);
		verify(repository);
		assertThat(retrieved, is(not(nullValue())));
		assertThat(retrieved.getAggregatorFeed().getId(), equalTo(aggFeed1.getId()));
		assertThat(retrieved.getSourceSubscriber().getId(), equalTo(srcSub1.getId()));
	
	}
	
	@Test(expected = AggregatorNotFoundException.class)
	public void getExistingAggregator_invalid() {
		expect(repository.getById(42L)).andReturn(null).once();
		replay(repository);
		Aggregator retrieved = aggService.getExistingAggregator(42L);
		verify(repository);
		assertThat(retrieved, is(not(nullValue())));
		assertThat(retrieved.getAggregatorFeed().getId(), equalTo(aggFeed1.getId()));
		assertThat(retrieved.getSourceSubscriber().getId(), equalTo(srcSub1.getId()));
	
	}
}


