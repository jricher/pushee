package org.mitre.pushee.hub.repository.impl;


import static org.mitre.pushee.hub.repository.util.JpaUtil.saveOrUpdate;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.mitre.pushee.hub.model.Aggregator;
import org.mitre.pushee.hub.repository.AggregatorRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * JPA Aggregator repository implementation
 * 
 * @author AANGANES
 *
 */
@Repository
public class JpaAggregatorRepository implements AggregatorRepository {

	@PersistenceContext
    private EntityManager manager;
	
	
	@Override
	@Transactional
	public Aggregator getById(Long id) {
		return manager.find(Aggregator.class, id);
	}

	@Override
	@Transactional
	public Aggregator save(Aggregator aggregator) {
		return saveOrUpdate(aggregator.getId(), manager, aggregator);
	}

	@Override
	@Transactional
	public void removeById(Long id) {
		Aggregator a = getById(id);
		manager.remove(a);
	}

	@Override
	@Transactional
	public void remove(Aggregator aggregator) {
		Aggregator found = manager.find(Aggregator.class, aggregator.getId());
		if (found != null) {
			manager.remove(aggregator);
		} else {
			throw new IllegalArgumentException();
		}
	}

	@Override
	@Transactional
	public Collection<Aggregator> getAll() {
		TypedQuery<Aggregator> query = manager.createNamedQuery("Aggregator.getAll", Aggregator.class);
		return query.getResultList();
	}

}
