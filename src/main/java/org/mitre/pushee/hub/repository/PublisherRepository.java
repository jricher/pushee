package org.mitre.pushee.hub.repository;

import org.mitre.pushee.hub.model.Publisher;

/**
 * @author Matt Franklin
 *         <p/>
 *         Provides query and persistence operations for {@link org.mitre.pushee.hub.model.Publisher} instances
 */
public interface PublisherRepository {
    /**
     * Gets a Publisher by its unique id
     *
     * @param id the id of the feed
     * @return a valid Publisher if exists; null otherwise;
     */
    Publisher get(long id);

    /**
     * Gets a Publisher by its unique URL
     *
     * @param url the URL of the Publisher
     * @return a valid URL if exists; null otherwise;
     */
    Publisher get(String url);

    /**
     * Persists a Publisher
     *
     * @param publisher valid Publisher instance
     */
    void save(Publisher publisher);
}
