package org.mitre.pushee.hub.service.impl;

import java.util.Collection;

import org.mitre.pushee.hub.exception.AggregatorNotFoundException;
import org.mitre.pushee.hub.model.Aggregator;
import org.mitre.pushee.hub.service.AggregatorService;

/**
 * Implementation of the Aggregator Service
 * 
 * @author AANGANES
 *
 */
public class DefaultAggregatorService implements AggregatorService {

	@Override
	public Aggregator getById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Aggregator getByUrl(String url) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Aggregator> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(Aggregator aggregator) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeById(Long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(Aggregator aggregator) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getExistingAggregator(Long id)
			throws AggregatorNotFoundException {
		// TODO Auto-generated method stub

	}

}
