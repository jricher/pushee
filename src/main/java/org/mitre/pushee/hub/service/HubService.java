package org.mitre.pushee.hub.service;

import java.util.Collection;

import org.mitre.pushee.hub.exception.FeedNotFoundException;
import org.mitre.pushee.hub.exception.PublisherNotFoundException;
import org.mitre.pushee.hub.exception.SubscriberNotFoundException;
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
	 * Get a collection of all subscribers managed by this HubService.
	 * 
	 * @return the collection of all subscribers, or null
	 */
	public Collection<Subscriber> getAllSubscribers();
	
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
	 * Get a subscriber by it's id
	 * 
	 * @param subscriberID the id of the subscriber to get
	 * @return the subscriber if found, or null
	 */
	public Subscriber getSubscriberById(Long subscriberID);
	
	/**
	 * Get a subscriber by its callback url, if we already have this subscriber
	 * saved to the database.
	 * 
	 * @param url the subscriber's callback url
	 * @return the found subscriber, or null
	 */
	public Subscriber getSubscriberByCallbackURL(String url);
	
	/**
	 * Get a collection of all feeds managed by this HubService.
	 * 
	 * @return the collection of all feeds, or null
	 */
	public Collection<Feed> getAllFeeds();
	
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
	 * Get a collection of all publishers managed by this HubService
	 * 
	 * @return the collection of all publishers, or null
	 */
	public Collection<Publisher> getAllPublishers();
	
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
	
	/**
	 * Get an existing publisher. If no publisher is found by this id, 
	 * throw a PublisherNotFoundException.
	 * 
	 * @param publisherId
	 * @return
	 * @throws PublisherNotFoundException
	 */
	public Publisher getExistingPublisher(Long publisherId) throws PublisherNotFoundException;
	
	/**
	 * Get an existing subscriber. If no subscriber is found by this id,
	 * throw a SubscriberNotFoundException.
	 * 
	 * @param subscriberId
	 * @return
	 * @throws SubscriberNotFoundException
	 */
	public Subscriber getExistingSubscriber(Long subscriberId) throws SubscriberNotFoundException;
	
	/**
	 * Get an existing feed. If no feed is found by this id, throw
	 * a FeedNotFoundException.
	 * 
	 * @param feedId
	 * @return
	 * @throws FeedNotFoundException
	 */
	public Feed getExistingFeed(Long feedId) throws FeedNotFoundException;
}
