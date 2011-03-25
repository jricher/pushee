package org.mitre.pushee.hub.service;

/**
 * Interface for hub service layer.
 * 
 * @author AANGANES
 *
 */
public interface IHubRepository {

	/**
	 * Get a list of subscribers subscribed to the given feed ID.
	 * 
	 * @param feedID ID of the feed to get subscribers for
	 * @return the list of subscribers
	 */
	public List<Subscriber> getSubscribersByFeedID(String feedID);
	
	/**
	 * Get a list of subscribers subscribed to the given feed reference.
	 * 
	 * @param f the field to get subscribers for
	 * @return the list of subscribers
	 */
	public List<Subscriber> getSubscribersByFeedRef(Feed f);
	
	/**
	 * Get the feed with the given URL.
	 * 
	 * @param feedURL the URL of the feed to get
	 * @return the feed
	 */
	public Feed getFeedByURL(String feedURL);
	
	/**
	 * Get the feed with the given ID.
	 * 
	 * @param feedID the ID of the feed to get
	 * @return the feed
	 */
	public Feed getFeedByID(String feedID);
	
	/**
	 * Get the publisher with the given ID.
	 * 
	 * @param publisherID the ID of the publisher to get
	 * @return the publisher
	 */
	public Publisher getPublisherByID(String publisherID);
	
	/**
	 * Get the publisher with the given URL.
	 * 
	 * @param publisherURL the URL of the publisher to get
	 * @return the publisher
	 */
	public Publisher getPublisherByURL(String publisherURL);
	
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
	
}
