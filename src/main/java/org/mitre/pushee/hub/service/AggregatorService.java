package org.mitre.pushee.hub.service;

import java.util.Collection;

import org.mitre.pushee.hub.exception.AggregatorNotFoundException;
import org.mitre.pushee.hub.model.Aggregator;

/**
 * Interface for aggregator service
 * 
 * @author AANGANES
 *
 */
public interface AggregatorService {

	/**
	 * Create a new Aggregator, and initialize its Feed and Subscriber.
	 * 
	 * @param displayName the display name of the Aggregator
	 * @return the newly created, initialized and persisted Aggregator
	 */
	public Aggregator createNew(String displayName);
	
	/**
	 * Get an Aggregator by its unique id
	 * 
	 * @param id the ID of the Aggregator
	 * @return the Aggregator if found, null otherwise
	 */
	public Aggregator getById(Long id);
	
	/**
	 * Get an Aggregator by its unique feed URL
	 * 
	 * @param feedUrl the URL of the Aggregator's feed
	 * @return the corresponding Aggregator if found, null otherwise
	 */
	public Aggregator getByFeedUrl(String feedUrl);
	
	/**
	 * Get an Aggregator by its unique subscriber URL
	 * 
	 * @param subscriberUrl the URL of the Aggregator's subscriber
	 * @return the corresponding Aggregator if found, null otherwise
	 */
	public Aggregator getBySubscriberUrl(String subscriberUrl);
	
	/**
	 * Get all of the Aggregators managed by this hub
	 * 
	 * @return the collection of all Aggregators
	 */
	public Collection<Aggregator> getAll();
	
	/**
	 * Save an Aggregator
	 * 
	 * @param aggregator the Aggregator to save
	 * @return the saved Aggregator
	 */
	public Aggregator save(Aggregator aggregator);
	
	/**
	 * Remove an Aggregator by its ID
	 * 
	 * @param id the ID of the Aggregator to remove
	 */
	public void removeById(Long id);
	
	/**
	 * Remove an Aggregator by reference
	 * 
	 * @param aggregator the Aggregator to remove
	 */
	public void remove(Aggregator aggregator);
	
	/**
	 * Get an existing Aggregator by ID
	 * 
	 * @param id the ID of the Aggregator to find
	 * @return the Aggregator if found, otherwise throws AggregatorNotFoundException
	 * @throws AggregatorNotFoundException
	 */
	public Aggregator getExistingAggregator(Long id) throws AggregatorNotFoundException;
	
}
