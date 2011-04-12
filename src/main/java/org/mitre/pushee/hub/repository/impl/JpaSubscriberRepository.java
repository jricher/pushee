package org.mitre.pushee.hub.repository.impl;

import org.mitre.pushee.hub.model.Subscriber;
import org.mitre.pushee.hub.repository.SubscriberRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.Set;

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
    public Set<Subscriber> getSubscribers(Long feedId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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
