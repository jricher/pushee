package org.mitre.pushee.hub.repository.impl;

import org.mitre.pushee.hub.model.Feed;
import org.mitre.pushee.hub.repository.FeedRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

/**
 * {@inheritDoc}
 * @author mfranklin
 *         Date: 4/6/11
 *         Time: 12:28 PM
 */
@Repository
public class JpaFeedRepository implements FeedRepository {

    @PersistenceContext(type = PersistenceContextType.TRANSACTION)
    private EntityManager manager;

    @Override
    public Feed get(long id) {
        return manager.find(Feed.class, id);
    }

    @Override
    public Feed get(String url) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void save(Feed feed) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
