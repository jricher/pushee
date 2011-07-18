package org.mitre.pushee.enterprise.web;

import org.mitre.pushee.hub.exception.FeedNotFoundException;
import org.mitre.pushee.hub.exception.PublisherNotFoundException;
import org.mitre.pushee.hub.exception.SubscriberNotFoundException;
import org.mitre.pushee.hub.model.Feed;
import org.mitre.pushee.hub.model.Publisher;
import org.mitre.pushee.hub.model.Subscriber;
import org.mitre.pushee.hub.service.HubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnterpriseUtils {

	@Autowired
	private static HubService hubService;
	
	/**
	 * Utility function to get an existing publisher, or throw 
	 * PublisherNotFound exception
	 * 
	 * @param pubId ID of the publisher to find
	 * @return the publisher, if found
	 */
	public static Publisher getExistingPublisher(Long pubId) throws PublisherNotFoundException {
		
		Publisher thePublisher = hubService.getPublisherById(pubId);
		
		if (thePublisher == null) {
			throw new PublisherNotFoundException();
		}
		
		return thePublisher;
	}
	
	/**
	 * Get an existing feed or throw FeedNotFoundException
	 * 
	 * @param feedId the ID of the feed to find
	 * @return the feed, if found
	 */
	public static Feed getExistingFeed(Long feedId) throws FeedNotFoundException {
		
		Feed theFeed = hubService.getFeedById(feedId);
		
		if (theFeed == null) {
			throw new FeedNotFoundException();
		}
		
		return theFeed;
	}
	
	/**
	 * Get an existing subscriber or throw SubscriberNotFoundException
	 * 
	 * @param subId the ID of the subscriber to find
	 * @return the subscriber, if found
	 */
	public static Subscriber getExistingSubscriber(Long subId) throws SubscriberNotFoundException {
		
		Subscriber theSub = hubService.getSubscriberById(subId);
		
		if (theSub == null) {
			throw new SubscriberNotFoundException();
		}
		
		return theSub;
	}
	
}
