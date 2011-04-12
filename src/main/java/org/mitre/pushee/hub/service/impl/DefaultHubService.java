package org.mitre.pushee.hub.service.impl;

import org.mitre.pushee.hub.model.Feed;
import org.mitre.pushee.hub.model.Publisher;
import org.mitre.pushee.hub.model.Subscriber;
import org.mitre.pushee.hub.service.HubService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author mfranklin
 *         Date: 4/12/11
 *         Time: 1:34 PM
 */
@Service
public class DefaultHubService implements HubService{
    @Override
    public List<Subscriber> getSubscribersByFeedId(String feedID) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Subscriber> getSubscribersByFeed(Feed f) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Feed getFeedByUrl(String feedURL) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Feed getFeedById(String feedID) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Publisher getPublisherById(String publisherID) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Publisher getPublisherByUrl(String publisherURL) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void savePublisher(Publisher publisher) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void saveFeed(Feed feed) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void saveSubscriber(Subscriber subscriber) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
