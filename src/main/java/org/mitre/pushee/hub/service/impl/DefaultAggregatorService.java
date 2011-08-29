package org.mitre.pushee.hub.service.impl;

import java.util.Collection;

import org.mitre.pushee.hub.exception.AggregatorNotFoundException;
import org.mitre.pushee.hub.model.Aggregator;
import org.mitre.pushee.hub.repository.AggregatorRepository;
import org.mitre.pushee.hub.service.AggregatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the Aggregator Service
 * 
 * @author AANGANES
 *
 */
@Service
@Transactional
public class DefaultAggregatorService implements AggregatorService {

	@Autowired
	private AggregatorRepository repository;
	
	/**
	 * Default constructor
	 */
	public DefaultAggregatorService() {
		
	}
	
	/**
	 * Constructor for use in test harnesses. 
	 * 
	 * @param repository
	 */
	public DefaultAggregatorService(AggregatorRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public Aggregator getById(Long id) {
		return repository.getById(id);
	}

	@Override
	public Aggregator getByUrl(String url) {
		return repository.getByUrl(url);
	}

	@Override
	public Collection<Aggregator> getAll() {
		return repository.getAll();
	}

	@Override
	public void save(Aggregator aggregator) {
		repository.save(aggregator);
	}

	@Override
	public void removeById(Long id) {
		repository.removeById(id);
	}

	@Override
	public void remove(Aggregator aggregator) {
		repository.remove(aggregator);
	}

	@Override
	public Aggregator getExistingAggregator(Long id)
			throws AggregatorNotFoundException {
		Aggregator aggregator = getById(id);
		
		if (aggregator == null) {
			throw new AggregatorNotFoundException();
		}
		
		return aggregator;
	}

}
