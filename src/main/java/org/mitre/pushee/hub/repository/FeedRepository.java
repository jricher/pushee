package org.mitre.pushee.hub.repository;

import java.util.Collection;

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
     * @param id the id of the feed
     * @return a valid Feed if exists; null otherwise;
     */
    public Feed getById(Long id);

    /**
     * Gets a feed by its unique URL
     *
     * @param url the URL of the feed
     * @return a valid Feed if exists; null otherwise;
     */
    public Feed getByUrl(String url);

    /**
     * Persists a Feed
     * 
     * @param feed valid Feed instance
     * @return the persisted entity
     */
    public Feed save(Feed feed);
    
    /**
     * Removes a Feed from the repository
     * 
     * @param id the id of the feed to remove
     */
    public void removeById(Long id);
    
    /**
     * Return a collection of all feeds managed by this repository
     * 
     * @return the feed collection
     */
    public Collection<Feed> getAll();
}
