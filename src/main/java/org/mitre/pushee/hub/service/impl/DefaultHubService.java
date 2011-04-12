package org.mitre.pushee.hub.service.impl;

import org.mitre.pushee.hub.model.Feed;
import org.mitre.pushee.hub.model.Publisher;
import org.mitre.pushee.hub.model.Subscriber;
import org.mitre.pushee.hub.repository.FeedRepository;
import org.mitre.pushee.hub.repository.PublisherRepository;
import org.mitre.pushee.hub.repository.SubscriberRepository;
import org.mitre.pushee.hub.service.HubService;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author mfranklin
 *         Date: 4/12/11
 *         Time: 1:34 PM
 */
@Service
public class DefaultHubService implements HubService {
	
	private FeedRepository feedRepository;
	private PublisherRepository publisherRepository;
	private SubscriberRepository subscriberRepository;
	
    @Override
    public Set<Subscriber> getSubscribersByFeedId(Long feedID) {
        return subscriberRepository.getSubscribers(feedID);
    }

    @Override
    public Subscriber getSubscriberByCallbackURL(String url) {
    	return subscriberRepository.get(url);
    }
    
    @Override
    public Set<Subscriber> getSubscribersByFeed(Feed f) {
       return subscriberRepository.getSubscribers(f.getId());
    }

    @Override
    public Feed getFeedByUrl(String feedURL) {
      return feedRepository.get(feedURL);
    }

    @Override
    public Feed getFeedById(Long feedID) {
       return feedRepository.get(feedID);
    }

    @Override
    public Publisher getPublisherById(Long publisherID) {
       return publisherRepository.get(publisherID);
    }

    @Override
    public Publisher getPublisherByUrl(String publisherURL) {
        return publisherRepository.get(publisherURL);
    }

    @Override
    public void savePublisher(Publisher publisher) {
        publisherRepository.save(publisher);
    }

    @Override
    public void saveFeed(Feed feed) {
        feedRepository.save(feed);
    }

    @Override
    public void saveSubscriber(Subscriber subscriber) {
        subscriberRepository.save(subscriber);
    }
}
