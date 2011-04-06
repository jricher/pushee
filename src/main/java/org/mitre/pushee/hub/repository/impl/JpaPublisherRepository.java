package org.mitre.pushee.hub.repository.impl;

import org.mitre.pushee.hub.model.Publisher;
import org.mitre.pushee.hub.repository.PublisherRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

/**
 * {@inheritDoc}
 * @author mfranklin
 *         Date: 4/6/11
 *         Time: 12:52 PM
 */
@Repository
public class JpaPublisherRepository implements PublisherRepository{

    @PersistenceContext(type = PersistenceContextType.TRANSACTION)
    private EntityManager manager;

    @Override
    public Publisher get(long id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Publisher get(String url) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void save(Publisher publisher) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
