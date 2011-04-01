package org.mitre.pushee.hub.repository;

import org.mitre.pushee.hub.model.Subscriber;

import java.util.Set;

/**
 * @author Matt Franklin
 *         <p/>
 *         Provides query and persistence operations for {@link org.mitre.pushee.hub.model.Feed} instances
 */
public interface SubscriberRepository {

    /**
     * Gets a list of subscribers for the given feed
     *
     * @param feedId the id of the feed
     * @return a list of valid Subscribers if any are found; Empty list otherwise
     */
    Set<Subscriber> getSubscribers(String feedId);

    /**
     * Persists a Subscriber
     *
     * @param subscriber valid Subscriber instance
     */
    void save(Subscriber subscriber);
}
