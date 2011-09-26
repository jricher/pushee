package org.mitre.pushee.hub.service.impl;

import java.util.Collection;

import org.mitre.pushee.hub.exception.FeedNotFoundException;
import org.mitre.pushee.hub.exception.PublisherNotFoundException;
import org.mitre.pushee.hub.exception.SubscriberNotFoundException;
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
	
	@Override
	public Collection<Subscriber> getAllSubscribers() {
		return subscriberRepository.getAll();
	}
	
    @Override
    public Collection<Subscriber> getSubscribersByFeedId(Long feedID) {
        return subscriberRepository.getSubscribers(feedID);
    }

    @Override
    public Subscriber getSubscriberById(Long subscriberID) {
    	return subscriberRepository.getById(subscriberID);
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
    public Collection<Publisher> getAllPublishers() {
    	return publisherRepository.getAll();
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
    
    @Override
    public Publisher getExistingPublisher(Long publisherId) throws PublisherNotFoundException {
    	Publisher publisher = getPublisherById(publisherId);
    	
    	if (publisher == null) {
    		throw new PublisherNotFoundException();
    	}
    	
    	return publisher;
    }
    
    @Override
    public Subscriber getExistingSubscriber(Long subscriberId) throws SubscriberNotFoundException {
    	Subscriber subscriber = getSubscriberById(subscriberId);
    	
    	if(subscriber == null) {
    		throw new SubscriberNotFoundException();
    	}
    	
    	return subscriber;
    }

    @Override
    public Feed getExistingFeed(Long feedId) throws FeedNotFoundException {
    	Feed feed = getFeedById(feedId);
    	
    	if (feed == null) {
    		throw new FeedNotFoundException();
    	}
    	
    	return feed;
    }
    
	/**
	 * Create a builder for use in test harnesses
	 * @return
	 */
	public static DefaultHubServiceBuilder makeBuilder() {
		return new DefaultHubServiceBuilder();
	}

	/**
	 * Builder class for use in test harnesses. The normal use of the 
	 * {@link DefaultHubService} class should make use of the Spring 
	 * {@link Autowired} capability.
	 * @author jricher
	 *
	 */
	public static class DefaultHubServiceBuilder {
		private DefaultHubService instance;
		
		private DefaultHubServiceBuilder() {
			instance = new DefaultHubService();
		}
		
		public DefaultHubServiceBuilder setFeedRepository(FeedRepository feedRepository) {
			instance.feedRepository = feedRepository;
			return this;
		}
		
		public DefaultHubServiceBuilder setPublisherRepository(PublisherRepository publisherRepository) {
			instance.publisherRepository = publisherRepository;
			return this;
		}
		
		public DefaultHubServiceBuilder setSubscriberRepository(SubscriberRepository subscriberRepository) {
			instance.subscriberRepository = subscriberRepository;
			return this;
		}
		
		public DefaultHubService finish() {
			return instance;
		}
	}
	
}
