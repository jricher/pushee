package org.mitre.pushee.hub.service;


import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertThat;

import java.util.Collection;
import java.util.HashSet;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
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
	
	private long feedId;
	private long publisherId;
	private long subscriberId;
	private long subscriptionId;
	
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
    	
    	verify(hubService);
    }
    
    @Test
    public void getSubscribersByCallbackUrl_validUrl() {
    	expect(subscriberRepository.getByUrl(subscriberURL)).andReturn(subscriber).once();
    	
    	replay(subscriberRepository);
    	
    	assertThat(hubService.getSubscriberByCallbackURL(subscriberURL), CoreMatchers.equalTo(subscriber));
    	
    	verify(hubService);
    }
    
    @Test
    public void getFeedByUrl_validUrl() {
    	expect(feedRepository.getByUrl(feedURL)).andReturn(feed).once();
    	  	
    	replay(feedRepository);
    	
    	assertThat(hubService.getFeedByUrl(feedURL), CoreMatchers.equalTo(feed));
    	
    	verify(hubService);
    }
    
    @Test
    public void getFeedById_validId() {
    	expect(feedRepository.getById(feedId)).andReturn(feed).once();

    	replay(feedRepository);
    	
    	assertThat(hubService.getFeedById(feedId), CoreMatchers.equalTo(feed));
    	
    	verify(hubService);
    }

    @Test
	public void getPublisherById_validId() {
		expect(publisherRepository.getById(publisherId)).andReturn(publisher).once();
		
		replay(publisherRepository);
		
		assertThat(hubService.getPublisherById(publisherId), CoreMatchers.equalTo(publisher));
		
		verify(hubService);
	}

    @Test
	public void getPublisherByUrl_validUrl() {
		expect(publisherRepository.getByUrl(publisherURL)).andReturn(publisher).once();
		
		replay(publisherRepository);
		
		assertThat(hubService.getPublisherByUrl(publisherURL), CoreMatchers.equalTo(publisher));
		
		verify(hubService);
	}
    
}
