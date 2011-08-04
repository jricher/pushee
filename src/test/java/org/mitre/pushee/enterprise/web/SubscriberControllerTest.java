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
import org.mitre.pushee.hub.exception.SubscriberNotFoundException;
import org.mitre.pushee.hub.model.Feed;
import org.mitre.pushee.hub.model.Subscriber;
import org.mitre.pushee.hub.service.HubService;
import org.springframework.web.servlet.ModelAndView;

/**
 * Unit test for SubscriberController
 * 
 * @author AANGANES
 *
 */
public class SubscriberControllerTest {

	private HubService hubService;
	private SubscriberController subscriberController;
	
	@Before
	public void setup() {
		hubService = createNiceMock(HubService.class);
		subscriberController = new SubscriberController(hubService);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void viewAll() {
		
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
		
		ModelAndView result = subscriberController.viewAllSubscribers(new ModelAndView());
		
		verify(hubService);
		
		assertThat((List<Subscriber>)(result.getModel().get("subscribers")), CoreMatchers.equalTo(subscribers));
		assertThat(result.getViewName(), CoreMatchers.equalTo("management/subscriber/subscriberIndex"));
		
	}
	
	@Test
	public void viewSubscriber_valid() {
		
		Subscriber subscriber = new Subscriber();
		subscriber.setPostbackURL("http://example.com/sub");
		subscriber.setId(5L);
		
		expect(hubService.getExistingSubscriber(5L)).andReturn(subscriber).once();
		replay(hubService);
		
		ModelAndView result = subscriberController.viewSubscriber(5L, new ModelAndView());
		
		verify(hubService);
		
		assertThat((Subscriber)result.getModel().get("subscriber"), CoreMatchers.equalTo(subscriber));
		assertThat(result.getViewName(), CoreMatchers.equalTo("management/subscriber/viewSubscriber"));
		
	}
	
	@Test(expected = SubscriberNotFoundException.class)
	public void viewSubscriber_invalid() {
		
		expect(hubService.getExistingSubscriber(5L)).andThrow(new SubscriberNotFoundException()).once();
		replay(hubService);
		
		subscriberController.viewSubscriber(5L, new ModelAndView());
		
		verify(hubService);
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void addSubscriber() {
		
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
		
		ModelAndView result = subscriberController.addSubscriber(new ModelAndView());
		
		verify(hubService);
		
		assertThat((Subscriber)result.getModel().get("subscriber"), CoreMatchers.equalTo(new Subscriber()));
		assertThat((List<Feed>)result.getModel().get("feeds"), CoreMatchers.equalTo(feeds));
		assertThat((String)result.getModel().get("mode"), CoreMatchers.equalTo("add"));
		assertThat(result.getViewName(), CoreMatchers.equalTo("management/subscriber/editSubscriber"));
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void editSubscriber_valid() {
		
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
		
		Subscriber subscriber = new Subscriber();
		subscriber.setPostbackURL("http://example.com/sub");
		subscriber.setId(5L);
		
		expect(hubService.getExistingSubscriber(5L)).andReturn(subscriber).once();
		expect(hubService.getAllFeeds()).andReturn(feeds).once();
		replay(hubService);
		
		ModelAndView result = subscriberController.editSubscriber(5L, new ModelAndView());
		
		verify(hubService);
		
		assertThat((Subscriber)result.getModel().get("subscriber"), CoreMatchers.equalTo(subscriber));
		assertThat((List<Feed>)result.getModel().get("feeds"), CoreMatchers.equalTo(feeds));
		assertThat((String)result.getModel().get("mode"), CoreMatchers.equalTo("edit"));
		assertThat(result.getViewName(), CoreMatchers.equalTo("management/subscriber/editSubscriber"));
		
	}
	
	@Test(expected = SubscriberNotFoundException.class)
	public void editSubscriber_invalid() {
		
		expect(hubService.getExistingSubscriber(5L)).andThrow(new SubscriberNotFoundException()).once();
		replay(hubService);
		
		subscriberController.editSubscriber(5L, new ModelAndView());
		
		verify(hubService);
		
	}
	
	@Test
	public void deleteSubscriber_valid() {
		
		Subscriber subscriber = new Subscriber();
		subscriber.setPostbackURL("http://example.com/sub");
		subscriber.setId(5L);
		
		expect(hubService.getExistingSubscriber(5L)).andReturn(subscriber).once();
		replay(hubService);
		
		ModelAndView result = subscriberController.deleteSubscriberConfirmation(5L, new ModelAndView());
		
		verify(hubService);
		
		assertThat((Subscriber)result.getModel().get("subscriber"), CoreMatchers.equalTo(subscriber));
		assertThat(result.getViewName(), CoreMatchers.equalTo("management/subscriber/deleteSubscriberConfirm"));
		
	}
	
	@Test(expected = SubscriberNotFoundException.class)
	public void deleteSubscriber_invalid() {
		
		expect(hubService.getExistingSubscriber(5L)).andThrow(new SubscriberNotFoundException()).once();
		replay(hubService);
		
		subscriberController.deleteSubscriberConfirmation(5L, new ModelAndView());
		
		verify(hubService);
		
	}
	

	
}
