package org.mitre.pushee.hub.repository.impl;

import static org.mitre.pushee.hub.repository.util.JpaUtil.getSingleResult;
import static org.mitre.pushee.hub.repository.util.JpaUtil.saveOrUpdate;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.mitre.pushee.hub.model.Feed;
import org.mitre.pushee.hub.repository.FeedRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public Feed getById(Long id) {
        return manager.find(Feed.class, id);
    }

    @Override
    @Transactional
    public Feed getByUrl(String url) {
        TypedQuery<Feed> query = manager.createNamedQuery("Feed.getByUrl", Feed.class);
        query.setParameter("feedUrl", url);
        return getSingleResult(query.getResultList());
    }

    @Override
    @Transactional
    public Feed save(Feed feed) {
        return saveOrUpdate(feed.getId(), manager, feed);
    }
    
    @Override
    @Transactional
    public void removeById(Long id) {
    	Feed f = getById(id);
    	manager.remove(f);
    }
    
    @SuppressWarnings("unchecked")
	@Override
    @Transactional
    public Collection<Feed> getAll() {
    	TypedQuery<Feed> query = manager.createNamedQuery("Feed.getAll", Feed.class);
        return (List<Feed>) query.getResultList();
    }
}
