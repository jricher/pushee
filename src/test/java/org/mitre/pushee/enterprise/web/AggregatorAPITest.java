package org.mitre.pushee.enterprise.web;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mitre.pushee.hub.exception.AggregatorNotFoundException;
import org.mitre.pushee.hub.model.Aggregator;
import org.mitre.pushee.hub.model.Feed;
import org.mitre.pushee.hub.model.Subscriber;
import org.mitre.pushee.hub.model.Subscription;
import org.mitre.pushee.hub.model.processor.impl.BasicAggregatorProcessor;
import org.mitre.pushee.hub.service.AggregatorService;
import org.springframework.test.annotation.Rollback;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Lists;


/**
 * Unit test for AggregatorAPI controller
 * 
 * @author AANGANES
 *
 */
public class AggregatorAPITest {

	private AggregatorAPI aggregatorApi;
	private AggregatorService aggService;
	
	private Feed aggFeed1;
	private Feed aggFeed2;
	private Subscriber srcSub1;
	private Subscriber srcSub2;
	private Aggregator agg1;
	private Aggregator agg2;
	
	@Before
	public void setup() {
		aggService = createNiceMock(AggregatorService.class);
		aggregatorApi = new AggregatorAPI(aggService);
		
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
	
	@SuppressWarnings("unchecked")
	@Test
	public void viewAll() {
		
		List<Aggregator> allAggs = Lists.newArrayList(agg1, agg2);
		expect(aggService.getAll()).andReturn(allAggs).once();
		replay(aggService);
		
		ModelAndView result = aggregatorApi.apiGetAllAggregators();
		verify(aggService);
		
		assertThat((List<Aggregator>)(result.getModel().get("aggregators")), equalTo(allAggs));
	}
	
	@Test
	public void viewAggregator_valid() {
		expect(aggService.getExistingAggregator(1L)).andReturn(agg1).once();
		replay(aggService);
		
		ModelAndView result = aggregatorApi.apiGetAggregator(1L);
		
		verify(aggService);
		
		assertThat((Aggregator)result.getModel().get("aggregator"), equalTo(agg1));
	}
	
	@Test(expected = AggregatorNotFoundException.class)
	public void viewAggregator_invalid() {
		expect(aggService.getExistingAggregator(5L)).andThrow(new AggregatorNotFoundException()).once();
		replay(aggService);
		
		aggregatorApi.apiGetAggregator(5L);
		
		verify(aggService);
	}
	
	@Test
	public void addAggregator() {
			
		ModelAndView result;
		try {
			Aggregator newAgg = new Aggregator();
			newAgg.setDisplayName("Aggregator Name");
			newAgg.setAggregatorFeed(aggFeed1);
			newAgg.setSourceSubscriber(srcSub1);
			newAgg.setProcessor(new BasicAggregatorProcessor());
			
			expect(aggService.createNew("Aggregator Name")).andReturn(newAgg).once();
			replay(aggService);
			
			result = aggregatorApi.apiAddAggregator("Aggregator Name", "org.mitre.pushee.hub.model.processor.impl.BasicAggregatorProcessor");
			verify(aggService);
			
			assertThat((Aggregator)result.getModel().get("newAgg"), equalTo(newAgg));
		
		} catch (Exception e) {
			fail("Caught runtime exception");
			System.out.println(e.getStackTrace());
		} 
	}
	
	@Test
	@Rollback
	public void editAggregator_valid() {
		
		expect(aggService.getExistingAggregator(1L)).andReturn(agg1).once();
		agg1.setDisplayName("New Name");
		expect(aggService.getExistingAggregator(1L)).andReturn(agg1).once();
		replay(aggService);
		
		ModelAndView result = aggregatorApi.apiEditAggregator(1L, "New Name");
		
		verify(aggService);
		
		assertThat((Aggregator)result.getModel().get("aggregator"), equalTo(agg1));
	}
	
	@Test(expected = AggregatorNotFoundException.class)
	public void editAggregator_invalid() {
		expect(aggService.getExistingAggregator(5L)).andThrow(new AggregatorNotFoundException()).once();
		replay(aggService);
		
		aggregatorApi.apiEditAggregator(5L, "New Name");
		
		verify(aggService);
	}
	
	@Test
	@Rollback
	public void deleteAggregator_valid() {
		expect(aggService.getExistingAggregator(1L)).andReturn(agg1).once();
		replay(aggService);
		
		ModelAndView result = aggregatorApi.apiDeleteAggregator(1L, new ModelAndView());
		
		verify(aggService);
		
		assertThat(result.getViewName(), equalTo("management/successfullyRemoved"));
	}
	
	@Test(expected = AggregatorNotFoundException.class)
	public void deleteAggregator_invalid() {
		expect(aggService.getExistingAggregator(5L)).andThrow(new AggregatorNotFoundException()).once();
		replay(aggService);
		
		aggregatorApi.apiDeleteAggregator(5L, new ModelAndView());
		
		verify(aggService);
	}
	
}
