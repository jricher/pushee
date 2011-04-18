package org.mitre.pushee.hub.service;


import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.awt.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mitre.pushee.hub.model.Feed;
import org.mitre.pushee.hub.model.Publisher;
import org.mitre.pushee.hub.model.Subscriber;
import org.mitre.pushee.hub.model.Subscription;

/**
 * @author mfranklin
 *         Date: 4/1/11
 *         Time: 4:57 PM
 */
public class HubServiceTest {
	
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
    }

    @Test
    @Ignore
    public void getSubscribersByFeedId_validId() {
    	
    	//Create subscribers list; the expected result of this shot
    	Set<Subscriber> subscribers = new HashSet<Subscriber>();
    	subscribers.add(subscriber);
    	
    	//Test the method call
        //expect(hubService.getSubscribersByFeedId(feedId).andReturn(subscribers).once());

        //Finalize the mock object and tell it to begin matching expectations
        //replay(hubService);

        //verify service was called properly
        //verify(hubService);
    }
    
    @Test
    @Ignore
    public void getSubscribersByFeed_validFeed() {
    	hubService.getSubscribersByFeed(feed);
    }
    
    @Test
    @Ignore
    public void getSubscribersByCallbackUrl_validUrl() {
    	hubService.getSubscriberByCallbackURL(subscriberURL);
    }
    
    @Test
    @Ignore
    public void getFeedByUrl_validUrl() {
    	hubService.getFeedByUrl(feedURL);
    }
    
    @Test
    @Ignore
    public void getFeedById_validId() {
    	hubService.getFeedById(feedId);
    }

    @Test
    @Ignore
	public void getPublisherById_validId() {
		hubService.getPublisherById(publisherId);
	}

    @Test
    @Ignore
	public void getPublisherByUrl_validUrl() {
		hubService.getPublisherByUrl(publisherURL);
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
