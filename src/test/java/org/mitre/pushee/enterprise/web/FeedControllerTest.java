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
import org.mitre.pushee.hub.model.Feed;
import org.mitre.pushee.hub.model.Publisher;
import org.mitre.pushee.hub.service.HubService;
import org.springframework.web.servlet.ModelAndView;

/**
 * Unit test for FeedController 
 * 
 * @author AANGANES
 *
 */
public class FeedControllerTest {

	private HubService hubService;
	private FeedController feedController;
	
	@Before
	public void setup() {
		hubService = createNiceMock(HubService.class);
		feedController = new FeedController(hubService);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void viewAll() {
		
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
		
		ModelAndView result = feedController.viewAllFeeds(new ModelAndView());
		
		verify(hubService);
		
		assertThat((List<Feed>)(result.getModel().get("feeds")), CoreMatchers.equalTo(feeds));
		assertThat(result.getViewName(), CoreMatchers.equalTo("management/feed/feedIndex"));
		
	}
	
	@Test
	public void viewFeed_valid() {
		
		Feed feed = new Feed();
		feed.setUrl("http://example.com/feed");
		feed.setType(Feed.FeedType.ATOM);
		feed.setId(5L);
		
		expect(hubService.getExistingFeed(5L)).andReturn(feed).once();
		replay(hubService);
		
		ModelAndView result = feedController.viewFeed(5L, new ModelAndView());
		
		verify(hubService);
		
		assertThat((Feed)(result.getModel().get("feed")), CoreMatchers.equalTo(feed));
		assertThat(result.getViewName(), CoreMatchers.equalTo("management/feed/viewFeed"));
	}
	
	@Test(expected = FeedNotFoundException.class)
	public void viewFeed_invalid() {
		
		expect(hubService.getExistingFeed(5L)).andThrow(new FeedNotFoundException()).once();
		replay(hubService);
		
		feedController.viewFeed(5L, new ModelAndView());
		
		verify(hubService);
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void addFeed() {
		
		Publisher pub1 = new Publisher();
		pub1.setCallbackURL("http://calbackURL/publisher1");
		pub1.setId(1L);
		
		Publisher pub3 = new Publisher();
		pub3.setCallbackURL("http://calbackURL/publisher3");
		pub3.setId(3L);
		
		Publisher pub2 = new Publisher();
		pub2.setCallbackURL("http://calbackURL/publisher2");
		pub2.setId(2L);
		
		List<Publisher> publishers = new ArrayList<Publisher>();
		publishers.add(pub1);
		publishers.add(pub2);
		publishers.add(pub3);
		
		expect(hubService.getAllPublishers()).andReturn(publishers).once();
		replay(hubService);
		
		ModelAndView result = feedController.addFeed(new ModelAndView());
		
		verify(hubService);
		
		assertThat(result.getViewName(), CoreMatchers.equalTo("management/feed/editFeed"));
		assertThat((String)result.getModel().get("mode"), CoreMatchers.equalTo("add"));
		assertThat((List<Publisher>)result.getModel().get("publishers"), CoreMatchers.equalTo(publishers));
		assertThat((Feed)result.getModel().get("feed"), CoreMatchers.equalTo(new Feed()));
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void editFeed_valid() {
		
		Publisher pub1 = new Publisher();
		pub1.setCallbackURL("http://calbackURL/publisher1");
		pub1.setId(1L);
		
		Publisher pub3 = new Publisher();
		pub3.setCallbackURL("http://calbackURL/publisher3");
		pub3.setId(3L);
		
		Publisher pub2 = new Publisher();
		pub2.setCallbackURL("http://calbackURL/publisher2");
		pub2.setId(2L);
		
		List<Publisher> publishers = new ArrayList<Publisher>();
		publishers.add(pub1);
		publishers.add(pub2);
		publishers.add(pub3);
		
		Feed feed = new Feed();
		feed.setId(5L);
		feed.setUrl("http://url.edu.org.com");
		feed.setType(Feed.FeedType.RSS);
		
		expect(hubService.getAllPublishers()).andReturn(publishers).once();
		expect(hubService.getExistingFeed(5L)).andReturn(feed).once();
		replay(hubService);
		
		ModelAndView result = feedController.editFeed(5L, new ModelAndView());
		
		verify(hubService);
		
		assertThat(result.getViewName(), CoreMatchers.equalTo("management/feed/editFeed"));
		assertThat((String)result.getModel().get("mode"), CoreMatchers.equalTo("edit"));
		assertThat((List<Publisher>)result.getModel().get("publishers"), CoreMatchers.equalTo(publishers));
		assertThat((Feed)result.getModel().get("feed"), CoreMatchers.equalTo(feed));
	}
	
	@Test(expected = FeedNotFoundException.class)
	public void editFeed_invalidFeed() {
		
		Publisher pub1 = new Publisher();
		pub1.setCallbackURL("http://calbackURL/publisher1");
		pub1.setId(1L);
		
		Publisher pub3 = new Publisher();
		pub3.setCallbackURL("http://calbackURL/publisher3");
		pub3.setId(3L);
		
		Publisher pub2 = new Publisher();
		pub2.setCallbackURL("http://calbackURL/publisher2");
		pub2.setId(2L);
		
		List<Publisher> publishers = new ArrayList<Publisher>();
		publishers.add(pub1);
		publishers.add(pub2);
		publishers.add(pub3);
		
		expect(hubService.getAllPublishers()).andReturn(publishers).once();
		expect(hubService.getExistingFeed(5L)).andThrow(new FeedNotFoundException()).once();
		replay(hubService);
		
		feedController.editFeed(5L, new ModelAndView());
		
		verify(hubService);
	}
	
	@Test
	public void deleteFeed_valid() {
		
		Feed feed = new Feed();
		feed.setId(5L);
		feed.setUrl("http://url.edu.org.com");
		feed.setType(Feed.FeedType.RSS);
		
		expect(hubService.getExistingFeed(5L)).andReturn(feed).once();
		replay(hubService);
		
		ModelAndView result = feedController.deleteFeedConfirmation(5L, new ModelAndView());
		
		verify(hubService);
		
		assertThat((Feed)result.getModel().get("feed"), CoreMatchers.equalTo(feed));
		assertThat(result.getViewName(), CoreMatchers.equalTo("management/feed/deleteFeedConfirm"));
	
	}
	
	@Test(expected = FeedNotFoundException.class)
	public void deleteFeed_invalidFeed() {
		
		expect(hubService.getExistingFeed(5L)).andThrow(new FeedNotFoundException()).once();
		replay(hubService);
		
		feedController.deleteFeedConfirmation(5L, new ModelAndView());
		
		verify(hubService);
		
	}
	
}
