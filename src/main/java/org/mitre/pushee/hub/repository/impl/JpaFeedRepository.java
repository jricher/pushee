package org.mitre.pushee.hub.repository.impl;

import org.mitre.pushee.hub.model.Feed;
import org.mitre.pushee.hub.repository.FeedRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;

import static org.mitre.pushee.hub.repository.util.JpaUtil.getSingleResult;
import static org.mitre.pushee.hub.repository.util.JpaUtil.saveOrUpdate;

/**
 * {@inheritDoc}
 * @author mfranklin
 *         Date: 4/6/11
 *         Time: 12:28 PM
 */
@Repository
public class JpaFeedRepository implements FeedRepository {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Feed getById(long id) {
        return manager.find(Feed.class, id);
    }

    @Override
    public Feed getByUrl(String url) {
        TypedQuery<Feed> query = manager.createNamedQuery("Feed.getByUrl", Feed.class);
        query.setParameter("feedUrl", url);
        return getSingleResult(query.getResultList());
    }

    @Override
    public Feed save(Feed feed) {
        return saveOrUpdate(feed.getId(), manager, feed);
    }
}
