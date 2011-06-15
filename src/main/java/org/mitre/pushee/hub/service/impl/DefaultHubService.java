package org.mitre.pushee.hub.service.impl;

import java.util.Collection;

import org.mitre.pushee.hub.model.Feed;
import org.mitre.pushee.hub.model.Publisher;
import org.mitre.pushee.hub.model.Subscriber;
import org.mitre.pushee.hub.repository.FeedRepository;
import org.mitre.pushee.hub.repository.PublisherRepository;
import org.mitre.pushee.hub.repository.SubscriberRepository;
import org.mitre.pushee.hub.service.HubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mfranklin
 *         Date: 4/12/11
 *         Time: 1:34 PM
 */
@Service
@Transactional
public class DefaultHubService implements HubService {
	
	@Autowired
	private FeedRepository feedRepository;
	
	@Autowired
	private PublisherRepository publisherRepository;
	
	@Autowired
	private SubscriberRepository subscriberRepository;
	
	public DefaultHubService() {
		
	}
	
	public DefaultHubService(FeedRepository fr, PublisherRepository pr, SubscriberRepository sr) {
		this.feedRepository = fr;
		this.publisherRepository = pr;
		this.subscriberRepository = sr;
	}
	
    @Override
    public Collection<Subscriber> getSubscribersByFeedId(Long feedID) {
        return subscriberRepository.getSubscribers(feedID);
    }

    @Override
    public Subscriber getSubscriberByCallbackURL(String url) {
    	return subscriberRepository.getByUrl(url);
    }
    
    @Override
    public Collection<Subscriber> getSubscribersByFeed(Feed f) {
       return subscriberRepository.getSubscribers(f.getId());
    }

    @Override
    public Collection<Feed> getAllFeeds() {
    	return feedRepository.getAll();
    }
    
    @Override
    public Feed getFeedByUrl(String feedURL) {
      return feedRepository.getByUrl(feedURL);
    }

    @Override
    public Feed getFeedById(Long feedID) {
       return feedRepository.getById(feedID);
    }

    @Override
    public Publisher getPublisherById(Long publisherID) {
       return publisherRepository.getById(publisherID);
    }

    @Override
    public Publisher getPublisherByUrl(String publisherURL) {
        return publisherRepository.getByUrl(publisherURL);
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
    @Transactional
    public void saveSubscriber(Subscriber subscriber) {
        subscriberRepository.save(subscriber);
    }
    
    @Override
    public void removeFeedById(Long feedId) {
    	feedRepository.removeById(feedId);
    }
    
    @Override
    public void removePublisherById(Long publisherId) {
    	publisherRepository.removeById(publisherId);
    }
    
    @Override
    public void removeSubscriberById(Long subscriberId) {
    	subscriberRepository.removeById(subscriberId);
    }
}
