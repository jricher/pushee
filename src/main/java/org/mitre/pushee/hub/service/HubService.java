package org.mitre.pushee.hub.service;

import java.util.Collection;

import org.mitre.pushee.hub.model.Feed;
import org.mitre.pushee.hub.model.Publisher;
import org.mitre.pushee.hub.model.Subscriber;

/**
 * Provides support for standard Hub transactions
 * 
 * @author AANGANES
 *
 */
public interface HubService {

	/**
	 * Get a list of subscribers subscribed to the given feed ID.
	 * 
	 *
     * @param feedID ID of the feed to get subscribers for
     * @return the list of subscribers
	 */
	public Collection<Subscriber> getSubscribersByFeedId(Long feedID);
	
	/**
	 * Get a list of subscribers subscribed to the given feed reference.
	 * 
	 *
     * @param f the field to get subscribers for
     * @return the list of subscribers
	 */
	public Collection<Subscriber> getSubscribersByFeed(Feed f);
	
	/**
	 * Get a subscriber by its callback url, if we already have this subscriber
	 * saved to the database.
	 * 
	 * @param url the subscriber's callback url
	 * @return the found subscriber, or null
	 */
	public Subscriber getSubscriberByCallbackURL(String url);
	
	/**
	 * Get the feed with the given URL.
	 * 
	 * @param feedURL the URL of the feed to get
	 * @return the feed
	 */
	public Feed getFeedByUrl(String feedURL);
	
	/**
	 * Get the feed with the given ID.
	 * 
	 * @param feedID the ID of the feed to get
	 * @return the feed
	 */
	public Feed getFeedById(Long feedID);
	
	/**
	 * Get the publisher with the given ID.
	 * 
	 * @param publisherID the ID of the publisher to get
	 * @return the publisher
	 */
	public Publisher getPublisherById(Long publisherID);
	
	/**
	 * Get the publisher with the given URL.
	 * 
	 * @param publisherURL the URL of the publisher to get
	 * @return the publisher
	 */
	public Publisher getPublisherByUrl(String publisherURL);
	
	/**
	 * Save a new publisher to the database.
	 * 
	 * @param publisher the publisher to save
	 */
	public void savePublisher(Publisher publisher);
	
	/**
	 * Save a new feed to the database.
	 * 
	 * @param feed the feed to save
	 */
	public void saveFeed(Feed feed);
	
	/**
	 * Save a new subscriber to the database.
	 * 
	 * @param subscriber the subscriber to save
	 */
	public void saveSubscriber(Subscriber subscriber);
	
	/**
	 * Remove a feed from the database
	 * 
	 * @param feedId
	 */
	public void removeFeedById(Long feedId);
	
	/**
	 * Remove a publisher from the database
	 * 
	 * @param publisherId
	 */
	public void removePublisherById(Long publisherId);
	
	/**
	 * Remove a subscriber from the database
	 * 
	 * @param subscriberId
	 */
	public void removeSubscriberById(Long subscriberId);
	
}
