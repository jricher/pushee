package org.mitre.pushee.hub.repository.util;

import org.springframework.dao.IncorrectResultSizeDataAccessException;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author mfranklin
 *         Date: 4/28/11
 *         Time: 2:13 PM
 */
public class JpaUtil {
    public static <T> T getSingleResult(List<T> list) {
        switch(list.size()) {
            case 0:
                return null;
            case 1:
                return list.get(0);
            default:
                throw new IncorrectResultSizeDataAccessException(1);
        }
    }

    public static <T, I> T saveOrUpdate(I id, EntityManager entityManager, T entity) {
        if (id == null) {
            entityManager.persist(entity);
            return entity;
        } else {
            return entityManager.merge(entity);
        }
    }
}
