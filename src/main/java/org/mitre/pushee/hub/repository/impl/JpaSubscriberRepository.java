package org.mitre.pushee.hub.repository.impl;

import org.mitre.pushee.hub.model.Subscriber;
import org.mitre.pushee.hub.model.Subscription;
import org.mitre.pushee.hub.repository.SubscriberRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * {@inheritDoc}
 * @author mfranklin
 *         Date: 4/6/11
 *         Time: 12:52 PM
 */
@Repository
public class JpaSubscriberRepository implements SubscriberRepository{

    @PersistenceContext(type = PersistenceContextType.TRANSACTION)
    private EntityManager manager;

    @Override
    @SuppressWarnings("unchecked")
    public Collection<Subscriber> getSubscribers(long feedId) {
    	
        Query namedQuery = manager.createNamedQuery("Subscription.getByFeedId");
        namedQuery.setParameter("feedId", feedId);
        List<Subscription> subscriptions = namedQuery.getResultList();
        List<Subscriber> subscribers = new ArrayList<Subscriber>();
       
        for (Subscription s : subscriptions) {
        	subscribers.add(s.getSubscriber());
        }
        
        return subscribers;
    }

    @Override
    public Subscriber get (String callbackURL) {
    	return null;
    }
    
    @Override
    public void save(Subscriber subscriber) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
