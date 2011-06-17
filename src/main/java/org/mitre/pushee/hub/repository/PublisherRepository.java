package org.mitre.pushee.hub.repository;

import java.util.Collection;

import org.mitre.pushee.hub.model.Publisher;
import org.mitre.pushee.hub.model.Subscriber;

/**
 * @author Matt Franklin
 *         <p/>
 *         Provides query and persistence operations for {@link org.mitre.pushee.hub.model.Publisher} instances
 */
public interface PublisherRepository {
   
	/**
	 * Get the collection of all publishers in the repository
	 * 
	 * @return the collection of all publishers, or null
	 */
	public Collection<Publisher> getAll();
	
	/**
     * Gets a Publisher by its unique id
     *
     *
     * @param id the id of the feed
     * @return a valid Publisher if exists; null otherwise;
     */
    public Publisher getById(Long id);

    /**
     * Gets a Publisher by its unique URL
     *
     *
     * @param url the URL of the Publisher
     * @return a valid URL if exists; null otherwise;
     */
    public Publisher getByUrl(String url);

    /**
     * Persists a Publisher
     *
     * @param publisher valid Publisher instance
     * @return persisted Publisher
     */
    public Publisher save(Publisher publisher);
    
    /**
     * Removes a Publisher from the repository
     * @param id the id of the Publisher to remove
     */
    public void removeById(Long id);    
    
    /**
     * Removes the given Publisher from the repository
     * 
     * @param p the Publisher to remove
     */
    public void remove(Publisher p);
    
}
