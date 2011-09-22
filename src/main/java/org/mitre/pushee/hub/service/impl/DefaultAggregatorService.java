package org.mitre.pushee.hub.service.impl;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.mitre.pushee.hub.exception.AggregatorNotFoundException;
import org.mitre.pushee.hub.model.Aggregator;
import org.mitre.pushee.hub.model.Feed;
import org.mitre.pushee.hub.model.Subscriber;
import org.mitre.pushee.hub.repository.AggregatorRepository;
import org.mitre.pushee.hub.service.AggregatorService;
import org.mitre.pushee.hub.service.HubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the Aggregator Service
 * 
 * @author AANGANES
 *
 */
@Service
@Transactional
public class DefaultAggregatorService implements AggregatorService {

	@Autowired
	private AggregatorRepository repository;
	
	@Autowired
	private HubService hubService;
	
	private String baseTomcatUrl;
	
	/**
	 * Default constructor
	 */
	public DefaultAggregatorService() {
		
	}
	
	/**
	 * Constructor for use in test harnesses. 
	 * 
	 * @param repository
	 */
	public DefaultAggregatorService(AggregatorRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public Aggregator createNew(String displayName) {

		Aggregator a = new Aggregator();
		a.setDisplayName(displayName);
		Aggregator saved = repository.save(a);
	
		String feedURL = baseTomcatUrl + "/aggregator/feed/" + saved.getId();
		String subURL = baseTomcatUrl + "/aggregator/subscriber/" + saved.getId();
		
		Feed feed = new Feed();
		feed.setUrl(feedURL);
		feed.setType(Feed.FeedType.ATOM);
		hubService.saveFeed(feed);
		feed = hubService.getFeedByUrl(feedURL);
		
		Subscriber sub = new Subscriber();
		sub.setPostbackURL(subURL);
		hubService.saveSubscriber(sub);
		sub = hubService.getSubscriberByCallbackURL(subURL);
		
		saved.setAggregatorFeed(feed);
		saved.setSourceSubscriber(sub);
		
		return repository.save(a);
	}
	
	@Override
	public Aggregator getById(Long id) {
		return repository.getById(id);
	}

	@Override
	public Aggregator getByFeedUrl(String feedUrl) {
		return repository.getByFeedUrl(feedUrl);
	}
	
	@Override
	public Aggregator getBySubscriberUrl(String subscriberUrl) {
		return repository.getBySubscriberUrl(subscriberUrl);
	}
	
	@Override
	public Collection<Aggregator> getAll() {
		return repository.getAll();
	}

	@Override
	public void save(Aggregator aggregator) {
		repository.save(aggregator);
	}

	@Override
	public void removeById(Long id) {
		repository.removeById(id);
	}

	@Override
	public void remove(Aggregator aggregator) {
		repository.remove(aggregator);
	}

	@Override
	public Aggregator getExistingAggregator(Long id)
			throws AggregatorNotFoundException {
		Aggregator aggregator = getById(id);
		
		if (aggregator == null) {
			throw new AggregatorNotFoundException();
		}
		
		return aggregator;
	}

	public String getBaseTomcatUrl() {
		return baseTomcatUrl;
	}

	public void setBaseTomcatUrl(String baseTomcatUrl) {
		this.baseTomcatUrl = baseTomcatUrl;
	}

}
