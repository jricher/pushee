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

	public Aggregator createNew(String displayName);
	
	public Aggregator getById(Long id);
	
	public Collection<Aggregator> getAll();
	
	public void save(Aggregator aggregator);
	
	public void removeById(Long id);
	
	public void remove(Aggregator aggregator);
	
	public Aggregator getExistingAggregator(Long id) throws AggregatorNotFoundException;
	
}
