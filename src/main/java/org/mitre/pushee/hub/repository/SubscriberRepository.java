package org.mitre.pushee.hub.repository;

import org.mitre.pushee.hub.model.Subscriber;

import java.util.Set;

/**
 * @author Matt Franklin
 *         <p/>
 *         Provides query and persistence operations for {@link org.mitre.pushee.hub.model.Subscriber} instances
 */
public interface SubscriberRepository {

    /**
     * Gets a list of subscribers for the given feed
     *
     * @param feedID the id of the feed
     * @return a list of valid Subscribers if any are found; Empty list otherwise
     */
    public Set<Subscriber> getSubscribers(Long feedID);

    /**
     * Get a single subscriber via its callback URL
     * 
     * @param callbackURL the callbackURL of the subscriber
     * @return the found subscriber, or null
     */
    public Subscriber get (String callbackURL);
    
    /**
     * Persists a Subscriber
     *
     * @param subscriber valid Subscriber instance
     */
    public void save(Subscriber subscriber);
}
