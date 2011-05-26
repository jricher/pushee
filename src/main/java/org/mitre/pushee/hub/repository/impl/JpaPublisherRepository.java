package org.mitre.pushee.hub.repository.impl;

import org.mitre.pushee.hub.model.Publisher;
import org.mitre.pushee.hub.repository.PublisherRepository;
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
 *         Time: 12:52 PM
 */
@Repository
public class JpaPublisherRepository implements PublisherRepository{

    @PersistenceContext
    private EntityManager manager;

    @Override
    @Transactional
    public Publisher getById(long id) {
        return manager.find(Publisher.class, id);
    }

    @Override
    @Transactional
    public Publisher getByUrl(String url) {
        TypedQuery<Publisher> query = manager.createNamedQuery("Publisher.getByUrl", Publisher.class);
        query.setParameter("url", url);
        return getSingleResult(query.getResultList());
    }

    @Override
    @Transactional
    public Publisher save(Publisher publisher) {
       return saveOrUpdate(publisher.getId(), manager, publisher);
    }
}
