package org.mitre.pushee.hub.service;


import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Collection;
import java.util.HashSet;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mitre.pushee.hub.model.Feed;
import org.mitre.pushee.hub.model.Publisher;
import org.mitre.pushee.hub.model.Subscriber;
import org.mitre.pushee.hub.model.Subscription;
import org.mitre.pushee.hub.repository.FeedRepository;
import org.mitre.pushee.hub.repository.PublisherRepository;
import org.mitre.pushee.hub.repository.SubscriberRepository;
import org.mitre.pushee.hub.service.impl.DefaultHubService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author mfranklin
 *         Date: 4/1/11
 *         Time: 4:57 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/spring/application-context.xml", "classpath:test-context.xml"})
public class HubServiceTest {
	
	private Logger logger; 
	
	private HubService hubService;
	private FeedRepository feedRepository;
	private SubscriberRepository subscriberRepository;
	private PublisherRepository publisherRepository;
	
	private String subscriberURL;
	private String publisherURL;
	private String feedURL;
	
	private Long feedId;
	private Long publisherId;
	private Long subscriberId;
	private Long subscriptionId;
	
	//Some objects to manipulate
	Subscriber subscriber;
	Publisher publisher;
	Feed feed;
	Subscription subscription;
	
    @Before
    public void setup() {
    	logger = LoggerFactory.getLogger(this.getClass());
    	logger.info("Setting up HubServiceTest");
    	
    	//Create the hubService and initialize URL parameters
    	//and IDs
    	
    	feedRepository = createNiceMock(FeedRepository.class);
    	subscriberRepository = createNiceMock(SubscriberRepository.class);
    	publisherRepository = createNiceMock(PublisherRepository.class);
    	
    	hubService = new DefaultHubService(feedRepository, publisherRepository, subscriberRepository);
    	//hubService = new DefaultHubService();
    	
    	subscriberURL = "http://example.com/subscriber";
    	publisherURL = "http://example.com/publisher";
    	feedURL = "http://example.com/feed";
    	
    	feedId = 1L;
    	publisherId = 2L;
    	subscriberId = 3L;
    	subscriptionId = 4L;

    	//Set up a publisher and feed
    	
    	publisher = new Publisher();
    	publisher.setId(publisherId);
    	publisher.setCallbackURL(publisherURL);
    	
    	feed = new Feed();
    	feed.setId(feedId);
    	feed.setPublisher(publisher);
    	feed.setUrl(feedURL);
    	
    	publisher.addFeed(feed);
    	
    	//Set up a subscriber and give it a subscription
    	//to the feed
    	
    	subscriber = new Subscriber();
    	subscriber.setId(subscriberId);
    	subscriber.setPostbackURL(subscriberURL);
    	
    	subscription = new Subscription();
    	subscription.setId(subscriptionId);
    	subscription.setFeed(feed);
    	subscription.setSubscriber(subscriber);
    	
    	subscriber.addSubscription(subscription);
    	logger.info("Setup finished");
    }

    @Test
    public void getSubscribersByFeedId_validId() {
    	logger.info("Starting get subscribers by feed ID test");
    	//Create subscribers list; the expected result of this shot
    	Collection<Subscriber> subscribers = new HashSet<Subscriber>();
    	subscribers.add(subscriber);
    	
    	//Place expectations, replays and verifies on repository mocks (look 
    	//at internal calls in DefaultHubService)
    	expect(subscriberRepository.getSubscribers(feedId)).andReturn(subscribers).once();
    	
    	replay(subscriberRepository);
    	
    	assertThat(hubService.getSubscribersByFeedId(feedId), CoreMatchers.equalTo(subscribers));
    	
    	
    	verify(subscriberRepository);
    }
    
    @Test
    public void getSubscribersByFeed_validFeed() {
    	Collection<Subscriber> subscribers = new HashSet<Subscriber>();
    	subscribers.add(subscriber);
    	
    	expect(subscriberRepository.getSubscribers(feedId)).andReturn(subscribers).once();
    	
    	replay(subscriberRepository);
    	
    	assertThat(hubService.getSubscribersByFeed(feed), CoreMatchers.equalTo(subscribers));
    	
    	verify(subscriberRepository);
    }
    
    @Test
    public void getSubscribersByCallbackUrl_validUrl() {
    	expect(subscriberRepository.getByUrl(subscriberURL)).andReturn(subscriber).once();
    	
    	replay(subscriberRepository);
    	
    	assertThat(hubService.getSubscriberByCallbackURL(subscriberURL), CoreMatchers.equalTo(subscriber));
    	
    	verify(subscriberRepository);
    }
    
    @Test
    public void getFeedByUrl_validUrl() {
    	expect(feedRepository.getByUrl(feedURL)).andReturn(feed).once();
    	  	
    	replay(feedRepository);
    	
    	assertThat(hubService.getFeedByUrl(feedURL), CoreMatchers.equalTo(feed));
    	
    	verify(feedRepository);
    }
    
    @Test
    public void getFeedById_validId() {
    	expect(feedRepository.getById(feedId)).andReturn(feed).once();

    	replay(feedRepository);
    	
    	assertThat(hubService.getFeedById(feedId), CoreMatchers.equalTo(feed));
    	
    	verify(feedRepository);
    }

    @Test
	public void getPublisherById_validId() {
		expect(publisherRepository.getById(publisherId)).andReturn(publisher).once();
		
		replay(publisherRepository);
		
		assertThat(hubService.getPublisherById(publisherId), CoreMatchers.equalTo(publisher));
		
		verify(publisherRepository);
	}

    @Test
	public void getPublisherByUrl_validUrl() {
		expect(publisherRepository.getByUrl(publisherURL)).andReturn(publisher).once();
		
		replay(publisherRepository);
		
		assertThat(hubService.getPublisherByUrl(publisherURL), CoreMatchers.equalTo(publisher));
		
		verify(publisherRepository);
	}
    
    @Test
    @Rollback
    public void savePublisher() {

    	Publisher newpub = new Publisher();
    	newpub.setCallbackURL("http://example.com/newpub");
    	newpub.setId(22L);
    	
    	expect(publisherRepository.getById(22L)).andReturn(newpub).once();
    	replay(publisherRepository);
    	
    	hubService.savePublisher(newpub);
    	
    	assertThat(hubService.getPublisherById(22L), CoreMatchers.equalTo(newpub));
    	
    	verify(publisherRepository);
    }
    
    @Test
    @Rollback
    public void saveFeed() {
    	Feed newFeed = new Feed();
    	newFeed.setId(22L);
    	newFeed.setPublisher(publisher);
    	newFeed.setType(Feed.FeedType.RSS);
    	newFeed.setUrl("http://example.com/newFeed");
    	
    	expect(feedRepository.getById(22L)).andReturn(newFeed).once();
    	replay(feedRepository);
    	
    	hubService.saveFeed(newFeed);
    	
    	assertThat(hubService.getFeedById(22L), CoreMatchers.equalTo(newFeed));
    	
    	verify(feedRepository);
    }
    
    @Test
    @Rollback
    public void saveSubscriber() {
    	Subscriber newSub = new Subscriber();
    	newSub.setId(22L);
    	newSub.setPostbackURL("http://example.com/newSub");
    	
    	expect(subscriberRepository.getByUrl("http://example.com/newSub")).andReturn(newSub).once();
    	replay(subscriberRepository);
    	
    	hubService.saveSubscriber(newSub);
    	
    	assertThat(hubService.getSubscriberByCallbackURL("http://example.com/newSub"), CoreMatchers.equalTo(newSub));
    	
    	verify(subscriberRepository);
    }
    
    @Test
    @Rollback
    public void removeFeedById_validID() {
    	//Feed newFeed = new Feed();
    	//newFeed.setId(22L);
    	//newFeed.setPublisher(publisher);
    	//newFeed.setType(Feed.FeedType.RSS);
    	//newFeed.setUrl("http://example.com/newFeed");

    	//hubService.saveFeed(newFeed);
    	
    	expect(feedRepository.getById(22L)).andReturn(null).once();
    	replay(feedRepository);
    	hubService.removeFeedById(22L);
    	assertThat(hubService.getFeedById(22L), is(nullValue()));
    	
    	verify(feedRepository);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void removeFeedById_invalidID() {
    	IllegalArgumentException iae = new IllegalArgumentException();
    	
    	feedRepository.removeById(42L);
    	expectLastCall().andThrow(iae);
    	replay(feedRepository);
    	
    	hubService.removeFeedById(42L);
    	
    	verify(feedRepository);
    }
    
    @Test
    @Rollback
    public void removePublisherById_validID() {
    	//Publisher newpub = new Publisher();
    	//newpub.setCallbackURL("http://example.com/newpub");
    	//newpub.setId(22L);
    	
    	//hubService.savePublisher(newpub);
    	
    	expect(publisherRepository.getById(22L)).andReturn(null).once();
    	replay(publisherRepository);
    	hubService.removePublisherById(22L);
    	assertThat(hubService.getPublisherById(22L), is(nullValue()));
    	
    	verify(publisherRepository);
    }
    
    @Test(expected = IllegalArgumentException.class) 
    public void removePublisherById_invalidID() {
    	IllegalArgumentException iae = new IllegalArgumentException();
    	
    	publisherRepository.removeById(42L);
    	expectLastCall().andThrow(iae);
    	replay(publisherRepository);
    	
    	hubService.removePublisherById(42L);
    	
    	verify(publisherRepository);
    }
    
    @Test
    @Rollback
    public void removeSubscriberById_validID() {
    	//Subscriber newSub = new Subscriber();
    	//newSub.setId(22L);
    	//newSub.setPostbackURL("http://example.com/newSub");
    	
    	//hubService.saveSubscriber(newSub);
    	//subscriberRepository.save(newSub);
    	//expectLastCall();
    	
    	expect(subscriberRepository.getByUrl("http://example.com/newSub")).andReturn(null).once();
    	replay(subscriberRepository);
    	hubService.removeSubscriberById(22L);
    	assertThat(hubService.getSubscriberByCallbackURL("http://example.com/newSub"), is(nullValue()));
    	
    	verify(subscriberRepository);
    }
    
    @Test(expected = IllegalArgumentException.class) 
    public void removeSubscriberById_invalidID() {
    	IllegalArgumentException iae = new IllegalArgumentException();
    	
    	subscriberRepository.removeById(42L);
    	expectLastCall().andThrow(iae);
    	replay(subscriberRepository);
    	
    	hubService.removeSubscriberById(42L);
    	
    	verify(subscriberRepository);
    }
    
}
