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

	public Aggregator getById(Long id);
	
	public Aggregator getByUrl(String url);
	
	public Collection<Aggregator> getAll();
	
	public void save(Aggregator aggregator);
	
	public void removeById(Long id);
	
	public void remove(Aggregator aggregator);
	
	public void getExistingAggregator(Long id) throws AggregatorNotFoundException;
	
}
