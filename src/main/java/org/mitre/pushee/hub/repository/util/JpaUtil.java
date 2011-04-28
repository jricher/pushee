package org.mitre.pushee.hub.repository.util;

import org.springframework.dao.IncorrectResultSizeDataAccessException;

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
}
