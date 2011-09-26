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
import org.mitre.pushee.hub.exception.SubscriberNotFoundException;
import org.mitre.pushee.hub.model.Feed;
import org.mitre.pushee.hub.model.Subscriber;
import org.mitre.pushee.hub.model.Subscription;
import org.mitre.pushee.hub.service.HubService;
import org.springframework.web.servlet.ModelAndView;

/**
 * Unit test for SubscriberAPI class
 * 
 * @author AANGANES
 *
 */
public class SubscriberAPITest {

	private HubService hubService;
	private SubscriberAPI subscriberApi;
	
	@Before
	public void setup() {
		hubService = createNiceMock(HubService.class);
		subscriberApi = new SubscriberAPI(hubService);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void getAll() {
		
		Subscriber sub1 = new Subscriber();
		sub1.setPostbackURL("http://example.com/sub1");
		sub1.setId(1L);
		
		Subscriber sub2 = new Subscriber();
		sub2.setPostbackURL("http://example.com/sub2");
		sub2.setId(2L);
		
		Subscriber sub3 = new Subscriber();
		sub3.setPostbackURL("http://example.com/sub3");
		sub3.setId(1L);
		
		List<Subscriber> subscribers = new ArrayList<Subscriber>();
		subscribers.add(sub1);
		subscribers.add(sub2);
		subscribers.add(sub3);
		
		expect(hubService.getAllSubscribers()).andReturn(subscribers).once();
		replay(hubService);
		
		ModelAndView result = subscriberApi.apiGetAllSubscribers();
		
		verify(hubService);
		
		assertThat((List<Subscriber>)(result.getModel().get("subscribers")), CoreMatchers.equalTo(subscribers));
		
	}
	
	@Test
	public void getSubscriber_valid() {
		
		Subscriber sub = new Subscriber();
		sub.setPostbackURL("http://example.com/subscriber");
		sub.setId(5L);
		
		expect(hubService.getExistingSubscriber(5L)).andReturn(sub).once();
		replay(hubService);
		
		ModelAndView result = subscriberApi.apiGetSubscriber(5L);
		
		verify(hubService);
		
		assertThat((Subscriber)(result.getModel().get("subscriber")), CoreMatchers.equalTo(sub));
		
	}
	
	@Test
	public void addSubscriber() {
		
		Subscriber sub = new Subscriber();
		sub.setPostbackURL("http://example.com/subscriber");
		sub.setDisplayName("Name");
		
		expect(hubService.getSubscriberByCallbackURL("http://example.com/subscriber")).andReturn(sub).once();
		replay(hubService);
		
		ModelAndView result = subscriberApi.apiAddSubscriber("http://example.com/subscriber", "Name");
		
		verify(hubService);
		
		assertThat((Subscriber)(result.getModel().get("subscriber")), CoreMatchers.equalTo(sub));
	}
	
	@Test(expected=SubscriberNotFoundException.class)
	public void getSubscriber_invalid() {
		
		expect(hubService.getExistingSubscriber(5L)).andThrow(new SubscriberNotFoundException()).once();
		replay(hubService);
		
		subscriberApi.apiGetSubscriber(5L);
		
		verify(hubService);
		
	}
	
	@Test
	public void editSubscriber_valid() {
		
		Subscriber sub = new Subscriber();
		sub.setPostbackURL("http://example.com/subscriber");
		sub.setId(5L);
		sub.setDisplayName("Name");
		
		Subscriber modified = new Subscriber();
		modified.setPostbackURL("http://example.com/subscriber/new");
		modified.setId(5L);
		modified.setDisplayName("New Name!");
		
		expect(hubService.getExistingSubscriber(5L)).andReturn(sub).once();
		expect(hubService.getExistingSubscriber(5L)).andReturn(modified).once();
		replay(hubService);
		
		ModelAndView result = subscriberApi.apiEditSubscriber(5L, "http://example.com/subscriber/new", "New Name!");
		
		verify(hubService);
		
		assertThat((Subscriber)(result.getModel().get("subscriber")), CoreMatchers.equalTo(modified));
		
	}
	
	@Test(expected=SubscriberNotFoundException.class)
	public void editSubscriber_invalidSubscriber() {
		
		expect(hubService.getExistingSubscriber(5L)).andThrow(new SubscriberNotFoundException()).once();
		replay(hubService);
		
		subscriberApi.apiEditSubscriber(5L, "http://example.com/subscriber/new", "New Name!");
		
		verify(hubService);
	}
	
	@Test
	public void removeSubscription_valid() {
		Subscriber sub = new Subscriber();
		sub.setPostbackURL("http://example.com/subscriber");
		sub.setId(5L);
		
		Feed feed = new Feed();
		feed.setId(2L);
		feed.setType(Feed.FeedType.RSS);
		feed.setUrl("http://example.com/feed");
		
		Subscription sn = new Subscription();
		sn.setFeed(feed);
		sn.setSubscriber(sub);
		
		sub.addSubscription(sn);
		
		Subscriber modified = new Subscriber();
		modified.setPostbackURL("http://example.com/subscriber");
		modified.setId(5L);
		
		expect(hubService.getExistingSubscriber(5L)).andReturn(sub).once();
		expect(hubService.getExistingFeed(2L)).andReturn(feed).once();
		expect(hubService.getExistingSubscriber(5L)).andReturn(modified).once();
		replay(hubService);
		
		ModelAndView result = subscriberApi.apiRemoveSubscription(5L, 2L);
		
		verify(hubService);
		
		assertThat((Subscriber)(result.getModel().get("subscriber")), CoreMatchers.equalTo(modified));
		
	}
	
	@Test(expected=SubscriberNotFoundException.class)
	public void removeSubscription_invalidSubscriber() {
		
		Subscriber sub = new Subscriber();
		sub.setPostbackURL("http://example.com/subscriber");
		sub.setId(5L);
		
		Feed feed = new Feed();
		feed.setId(2L);
		feed.setType(Feed.FeedType.RSS);
		feed.setUrl("http://example.com/feed");
		
		Subscription sn = new Subscription();
		sn.setFeed(feed);
		sn.setSubscriber(sub);
		
		sub.addSubscription(sn);
		
		Subscriber modified = new Subscriber();
		modified.setPostbackURL("http://example.com/subscriber");
		modified.setId(5L);
		
		expect(hubService.getExistingSubscriber(5L)).andThrow(new SubscriberNotFoundException()).once();
		expect(hubService.getExistingFeed(2L)).andReturn(feed).once();
		expect(hubService.getExistingSubscriber(5L)).andReturn(modified).once();
		replay(hubService);
		
		subscriberApi.apiRemoveSubscription(5L, 2L);
		
		verify(hubService);
		
	}
	
	@Test(expected=FeedNotFoundException.class)
	public void removeSubscription_invalidFeed() {
		
		Subscriber sub = new Subscriber();
		sub.setPostbackURL("http://example.com/subscriber");
		sub.setId(5L);
		
		Feed feed = new Feed();
		feed.setId(2L);
		feed.setType(Feed.FeedType.RSS);
		feed.setUrl("http://example.com/feed");
		
		Subscription sn = new Subscription();
		sn.setFeed(feed);
		sn.setSubscriber(sub);
		
		sub.addSubscription(sn);
		
		Subscriber modified = new Subscriber();
		modified.setPostbackURL("http://example.com/subscriber");
		modified.setId(5L);
		
		expect(hubService.getExistingSubscriber(5L)).andReturn(sub).once();
		expect(hubService.getExistingFeed(2L)).andThrow(new FeedNotFoundException()).once();
		expect(hubService.getExistingSubscriber(5L)).andReturn(modified).once();
		replay(hubService);
		
		subscriberApi.apiRemoveSubscription(5L, 2L);
		
		verify(hubService);
		
	}
	
	@Test
	public void deleteSubscriber_valid() {
		Subscriber sub = new Subscriber();
		sub.setPostbackURL("http://example.com/subscriber");
		sub.setId(5L);
		
		expect(hubService.getExistingSubscriber(5L)).andReturn(sub).once();
		replay(hubService);
		
		ModelAndView result = subscriberApi.apiDeleteSubscriber(5L, new ModelAndView());
		
		verify(hubService);
		
		assertThat(result.getViewName(), CoreMatchers.equalTo("management/successfullyRemoved"));
		
	}
	
	@Test(expected=SubscriberNotFoundException.class)
	public void deleteSubscriber_invalidSubscriber() {
		
		expect(hubService.getExistingSubscriber(5L)).andThrow(new SubscriberNotFoundException()).once();
		replay(hubService);
		
		subscriberApi.apiDeleteSubscriber(5L, new ModelAndView());
		
		verify(hubService);
		
	}
	
	
}
