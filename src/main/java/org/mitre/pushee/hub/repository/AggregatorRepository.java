package org.mitre.pushee.hub.repository;

import java.util.Collection;
import org.mitre.pushee.hub.model.Aggregator;

/**
 * 
 * Provides query and persistence operations for {@link org.mitre.pushee.hub.model.Aggregator} instances
 * 
 * @author AANGANES
 *
 */
public interface AggregatorRepository {

	/**
	 * Get an aggregator by its unique id
	 * 
	 * @param id the id of the aggregator
	 * @return a valid Aggregator if it exists, null otherwise
	 */
	public Aggregator getById(Long id);
	
	/**
	 * Persists an Aggregator 
	 * 
	 * @param aggregator valid Aggregator instance
	 * @return the persisted entity
	 */
	public Aggregator save(Aggregator aggregator);
	
	/**
	 * Removes an Aggregator from the repository
	 * 
	 * @param id the id of the aggregator to remove
	 */
	public void removeById(Long id);
	
	/**
	 * Removes the given Aggregator from the repository
	 * 
	 * @param aggregator the Aggregator object to remove
	 */
	public void remove(Aggregator aggregator);
	
	/**
	 * Return a collection of all aggregators managed by this repository
	 * 
	 * @return the aggregator collection, or null
	 */
	public Collection<Aggregator> getAll();
}
