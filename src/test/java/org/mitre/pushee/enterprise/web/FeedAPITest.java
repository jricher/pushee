package org.mitre.pushee.enterprise.web;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.mitre.pushee.hub.exception.FeedNotFoundException;
import org.mitre.pushee.hub.exception.PublisherNotFoundException;
import org.mitre.pushee.hub.model.Feed;
import org.mitre.pushee.hub.model.Publisher;
import org.mitre.pushee.hub.model.Subscription;
import org.mitre.pushee.hub.service.HubService;
import org.springframework.web.servlet.ModelAndView;


/**
 * Unit test for FeedAPI class
 * 
 * @author AANGANES
 * 
 */
public class FeedAPITest {

	private HubService hubService;
	private FeedAPI feedApi;
	
	@Before
	public void setup() {
		hubService = createNiceMock(HubService.class);
		feedApi = new FeedAPI(hubService);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void getAllFeeds() {
		
		Feed feed1 = new Feed();
		feed1.setUrl("http://example.com/feed1");
		feed1.setType(Feed.FeedType.ATOM);
		
		Feed feed2 = new Feed();
		feed2.setUrl("http://example.com/feed3");
		feed2.setType(Feed.FeedType.RSS);
		
		Feed feed3 = new Feed();
		feed3.setUrl("http://example.com/feed3");
		feed3.setType(Feed.FeedType.ATOM);
		
		List<Feed> feeds = new ArrayList<Feed>();
		feeds.add(feed1);
		feeds.add(feed2);
		feeds.add(feed3);
		
		expect(hubService.getAllFeeds()).andReturn(feeds).once();
		replay(hubService);
		
		ModelAndView result = feedApi.apiGetAllFeeds();

		verify(hubService);
		
		assertThat((List<Feed>)(result.getModel().get("feeds")), CoreMatchers.equalTo(feeds));
		
	}
	
	@Test
	public void getFeedTest_valid() {
		Feed feed1 = new Feed();
		feed1.setUrl("http://example.com/feed1");
		feed1.setType(Feed.FeedType.ATOM);
		feed1.setId(5L);
		
		expect(hubService.getExistingFeed(5L)).andReturn(feed1).once();
		replay(hubService);
		
		ModelAndView result = feedApi.apiGetFeed(5L);
		
		verify(hubService);
		
		assertThat((Feed)(result.getModel().get("feed")), CoreMatchers.equalTo(feed1));
	}
	
	@Test(expected=FeedNotFoundException.class)
	public void getFeedTest_invalid() {
		Feed feed1 = new Feed();
		feed1.setUrl("http://example.com/feed1");
		feed1.setType(Feed.FeedType.ATOM);
		feed1.setId(5L);
		
		expect(hubService.getExistingFeed(5L)).andThrow(new FeedNotFoundException()).once();
		replay(hubService);
		
		feedApi.apiGetFeed(5L);
		
		verify(hubService);
	}
	
	@Test
	public void addFeed_valid() {
		Feed feed1 = new Feed();
		feed1.setUrl("http://example.com/feed1");
		feed1.setType(Feed.FeedType.ATOM);
		feed1.setId(5L);
		Publisher publisher = new Publisher();
		publisher.setId(2L);
		publisher.addFeed(feed1);
		feed1.setPublisher(publisher);
		
		expect(hubService.getExistingPublisher(2L)).andReturn(publisher).once();
		expect(hubService.getFeedByUrl("http://example.com/feed1")).andReturn(feed1).once();
		replay(hubService);
		
		ModelAndView result = feedApi.apiAddFeed(2L, Feed.FeedType.ATOM, "http://example.com/feed1");
		
		verify(hubService);
		
		assertThat((Feed)(result.getModel().get("newFeed")), CoreMatchers.equalTo(feed1));
	}
	
	@Test(expected=PublisherNotFoundException.class)
	public void addFeed_invalidPublisher() {
		Feed feed1 = new Feed();
		feed1.setUrl("http://example.com/feed1");
		feed1.setType(Feed.FeedType.ATOM);
		feed1.setId(5L);
		
		expect(hubService.getExistingPublisher(2L)).andThrow(new PublisherNotFoundException()).once();
		expect(hubService.getFeedByUrl("http://example.com/feed1")).andReturn(feed1).once();
		replay(hubService);
		
		feedApi.apiAddFeed(2L, Feed.FeedType.ATOM, "http://example.com/feed1");
		
		verify(hubService);
	}
	
	@Test
	public void editFeed_valid() {
		Feed feed = new Feed();
		feed.setUrl("http://example.com/feed1");
		feed.setType(Feed.FeedType.ATOM);
		feed.setId(5L);
		Publisher publisher = new Publisher();
		publisher.setId(2L);
		publisher.addFeed(feed);
		feed.setPublisher(publisher);
		
		Feed editedFeed = new Feed();
		editedFeed.setUrl("http://example.com/feed1/new");
		editedFeed.setType(Feed.FeedType.RSS);
		editedFeed.setId(5L);
		Publisher newPublisher = new Publisher();
		newPublisher.setId(3L);
		newPublisher.addFeed(editedFeed);
		editedFeed.setPublisher(newPublisher);
		
		expect(hubService.getExistingFeed(5L)).andReturn(feed).once();
		expect(hubService.getExistingPublisher(3L)).andReturn(newPublisher).once();
		expect(hubService.getExistingFeed(5L)).andReturn(editedFeed).once();
		replay(hubService);
		
		ModelAndView result = feedApi.apiEditFeed(5L, 3L, Feed.FeedType.RSS, "http://example.com/feed1/new");
		
		verify(hubService);
		
		assertThat((Feed)(result.getModel().get("feed")), CoreMatchers.equalTo(editedFeed));
	}
	
	@Test(expected=FeedNotFoundException.class)
	public void editFeed_invalidFeed() {
		
		expect(hubService.getExistingFeed(5L)).andThrow(new FeedNotFoundException()).once();

		replay(hubService);
		
		feedApi.apiEditFeed(5L, 3L, Feed.FeedType.RSS, "http://example.com/feed1/new");
		
		verify(hubService);
		
	}
	
	@Test(expected=PublisherNotFoundException.class)
	public void editFeed_invalidPublisher() {
		Feed feed = new Feed();
		feed.setUrl("http://example.com/feed1");
		feed.setType(Feed.FeedType.ATOM);
		feed.setId(5L);
		
		expect(hubService.getExistingFeed(5L)).andReturn(feed).once();
		expect(hubService.getExistingPublisher(3L)).andThrow(new PublisherNotFoundException()).once();
		replay(hubService);
		
		feedApi.apiEditFeed(5L, 3L, Feed.FeedType.RSS, "http://example.com/feed1/new");
		
		verify(hubService);
	
	}
	
	@Test
	public void deleteFeed_valid() {
		Feed feed = new Feed();
		feed.setUrl("http://example.com/feed1");
		feed.setType(Feed.FeedType.ATOM);
		feed.setId(5L);
		feed.setSubscriptions(new ArrayList<Subscription>());
		
		expect(hubService.getExistingFeed(5L)).andReturn(feed).once();
		replay(hubService);
		
		ModelAndView result = feedApi.apiDeleteFeed(5L, new ModelAndView());
		
		verify(hubService);
		
		assertThat(result.getViewName(), CoreMatchers.equalTo("management/successfullyRemoved"));
		
	}
	
	@Test(expected=FeedNotFoundException.class)
	public void deleteFeed_invalidFeed() {
		
		expect(hubService.getExistingFeed(5L)).andThrow(new FeedNotFoundException()).once();
		replay(hubService);
		
		feedApi.apiDeleteFeed(5L, new ModelAndView());
		
		verify(hubService);	
	}
	
}
