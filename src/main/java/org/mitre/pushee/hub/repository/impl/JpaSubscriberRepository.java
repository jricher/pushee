package org.mitre.pushee.hub.repository.impl;

import org.mitre.pushee.hub.model.Publisher;
import org.mitre.pushee.hub.model.Subscriber;
import org.mitre.pushee.hub.model.Subscription;
import org.mitre.pushee.hub.repository.SubscriberRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mitre.pushee.hub.repository.util.JpaUtil.getSingleResult;
import static org.mitre.pushee.hub.repository.util.JpaUtil.saveOrUpdate;

/**
 * {@inheritDoc}
 * @author mfranklin
 *         Date: 4/6/11
 *         Time: 12:52 PM
 */
@Repository
public class JpaSubscriberRepository implements SubscriberRepository{

    @PersistenceContext
    private EntityManager manager;
	
    @Override
    @Transactional
    public Collection<Subscriber> getSubscribers(Long feedId) {
        TypedQuery<Subscriber> namedQuery = manager.createNamedQuery("Subscription.getSubscribersByFeedId", Subscriber.class);
        namedQuery.setParameter("feedId", feedId);
        return namedQuery.getResultList();
    }

    @Override
    @Transactional
    public Subscriber getByUrl(String callbackURL) {
        TypedQuery<Subscriber> query = manager.createNamedQuery("Subscriber.getByUrl", Subscriber.class);
        query.setParameter("url", callbackURL);
    	return getSingleResult(query.getResultList());
    }
    
    @Override
    @Transactional
    public Subscriber save(Subscriber subscriber) {
        return saveOrUpdate(subscriber.getId(), manager, subscriber);
    }
    
    @Override
    @Transactional
    public void removeById(Long id) {
    	Subscriber s = manager.find(Subscriber.class, id);
    	manager.remove(s);
    }
    
    @Override
    @Transactional
    public void remove(Subscriber s) {
    	Subscriber found = manager.find(Subscriber.class, s.getId());
    	if (found != null) {
    		manager.remove(s);
    	} else {
    		throw new IllegalArgumentException();
    	}
    }
    
    @Override
    @Transactional
    public Collection<Subscriber> getAll() {
    	TypedQuery<Subscriber> query = manager.createNamedQuery("Subscriber.getAll", Subscriber.class);
        return (List<Subscriber>) query.getResultList();
    }
    
}
