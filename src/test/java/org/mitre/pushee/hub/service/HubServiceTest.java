package org.mitre.pushee.hub.service;


import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collection;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mitre.pushee.hub.model.Feed;
import org.mitre.pushee.hub.model.Publisher;
import org.mitre.pushee.hub.model.Subscriber;
import org.mitre.pushee.hub.model.Subscription;
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
    	
    	hubService = createNiceMock(HubService.class);
    	
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
    @Ignore
    public void getSubscribersByFeedId_validId() {
    	logger.info("Starting get subscribers by feed ID test");
    	//Create subscribers list; the expected result of this shot
    	Collection<Subscriber> subscribers = new HashSet<Subscriber>();
    	subscribers.add(subscriber);
    	//Test the method call
        expect(hubService.getSubscribersByFeedId(feedId)).andReturn(subscribers).once();
        //Finalize the mock object and tell it to begin matching expectations
        replay(hubService);
        //verify service was called properly
        verify(hubService);
    }
    
    @Test
    @Ignore
    public void getSubscribersByFeed_validFeed() {
    	Collection<Subscriber> subscribers = new HashSet<Subscriber>();
    	subscribers.add(subscriber);
    	
    	expect(hubService.getSubscribersByFeed(feed)).andReturn(subscribers).once();
    	
    	replay(hubService);
    	verify(hubService);
    }
    
    @Test
    @Ignore
    public void getSubscribersByCallbackUrl_validUrl() {
    	
    	expect(hubService.getSubscriberByCallbackURL(subscriberURL)).andReturn(subscriber).once();
    	
    	replay(hubService);
    	verify(hubService);
    }
    
    @Test
    @Ignore
    public void getFeedByUrl_validUrl() {
    	
    	expect(hubService.getFeedByUrl(feedURL)).andReturn(feed).once();
    	
    	replay(hubService);
    	verify(hubService);
    }
    
    @Test
    @Ignore
    public void getFeedById_validId() {
    	
    	expect(hubService.getFeedById(feedId)).andReturn(feed).once();
    	
    	replay(hubService);
    	verify(hubService);
    }

    @Test
    @Ignore
	public void getPublisherById_validId() {
    	
		expect(hubService.getPublisherById(publisherId)).andReturn(publisher).once();
		
		replay(hubService);
		verify(hubService);
	}

    @Test
    @Ignore
	public void getPublisherByUrl_validUrl() {
    	
		expect(hubService.getPublisherByUrl(publisherURL)).andReturn(publisher).once();
		
		replay(hubService);
		verify(hubService);
	}

    //Question: do we need to test the save methods?
    
    @Test
    @Ignore
	public void savePublisher_validPublisher() {

	}

    @Test
    @Ignore
	public void saveFeed_validFeed() {
		
	}
	
    @Test
    @Ignore
	public void saveSubscriber_validSubscriber() {
		
	}
    
}
