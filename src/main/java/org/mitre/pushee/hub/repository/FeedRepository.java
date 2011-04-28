package org.mitre.pushee.hub.repository;

import org.mitre.pushee.hub.model.Feed;

/**
 * @author Matt Franklin
 *         <p/>
 *         Provides query and persistence operations for {@link org.mitre.pushee.hub.model.Feed} instances
 */
public interface FeedRepository {
    /**
     * Gets a feed by its unique id
     *
     *
     * @param id the id of the feed
     * @return a valid Feed if exists; null otherwise;
     */
    public Feed getById(long id);

    /**
     * Gets a feed by its unique URL
     *
     *
     * @param url the URL of the feed
     * @return a valid Feed if exists; null otherwise;
     */
    public Feed getByUrl(String url);

    /**
     * Persists a Feed
     * @param feed valid Feed instance
     */
    public void save(Feed feed);
}
