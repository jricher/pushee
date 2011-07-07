package org.mitre.pushee.hub.repository;

import org.mitre.pushee.hub.model.Subscriber;

import java.util.Collection;

/**
 * @author Matt Franklin
 *         <p/>
 *         Provides query and persistence operations for {@link org.mitre.pushee.hub.model.Subscriber} instances
 */
public interface SubscriberRepository {

	/**
	 * Get the collection of all subscribers in the repository
	 * 
	 * @return the collection of all subscribers, or null
	 */
	public Collection<Subscriber> getAll();
	
    /**
     * Gets a list of subscribers for the given feed
     *
     *
     *
     * @param feedId the id of the feed
     * @return a list of valid Subscribers if any are found; Empty list otherwise
     */
    public Collection<Subscriber> getSubscribers(Long feedId);

    /**
     * Get a single subscriber via its callback URL
     * 
     *
     * @param callbackURL the callbackURL of the subscriber
     * @return the found subscriber, or null
     */
    public Subscriber getByUrl(String callbackURL);
    
    /**
     * Persists a Subscriber
     *
     * @param subscriber valid Subscriber instance
     * @return persisted subscriber
     */
    public Subscriber save(Subscriber subscriber);
    
    /**
     * Removes a Subscriber from the repository
     * @param id the id of the Subscriber to remove
     */
    public void removeById(Long id);
    
    /**
     * Remove the given Subscriber from the repository
     * 
     * @param s the Subscriber object to remove
     */
    public void remove(Subscriber s);

    /**
     * Get a single subscriber via its id
     * 
     * @param subscriberID the ID of the subscriber
     * @return the subscriber if found, or null
     */
	public Subscriber getById(Long subscriberID);
    
}
