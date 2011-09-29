package org.mitre.pushee.enterprise.web;

import org.mitre.pushee.hub.model.Aggregator;
import org.mitre.pushee.hub.model.Feed;
import org.mitre.pushee.hub.model.Subscriber;
import org.mitre.pushee.hub.service.AggregatorService;
import org.mitre.pushee.hub.service.HubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/aggregator")
public class AggregatorContentController {

	@Autowired
	private AggregatorService aggService;
	
	@Autowired
	private HubService hubService;
	
	/**
	 * Default constructor
	 */
	public AggregatorContentController() {
		
	}

	
	/**
	 * Handle callbacks to an Aggregator's Feed URL
	 * 
	 * @param feedId
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("/feed/{feedId}")
	public ModelAndView handleFeedCalls(@PathVariable Long feedId, ModelAndView modelAndView) {
		
		Feed aggFeed = hubService.getExistingFeed(feedId);
		Aggregator agg = aggService.getByFeedUrl(aggFeed.getUrl());
		
		
		
		return modelAndView;
	}
	
	/**
	 * Handle callbacks to an Aggregator's Source Subscriber URL
	 * 
	 * @param subscriberId
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("/subscriber/{subscriberId}")
	public ModelAndView handleSourceSubscriberCalls(@PathVariable Long subscriberId, ModelAndView modelAndView) {
		
		Subscriber aggSub = hubService.getExistingSubscriber(subscriberId);
		Aggregator agg = aggService.getBySubscriberUrl(aggSub.getPostbackURL());
		
		
		
		return modelAndView;
	}
	
	
	/**
	 * @return the service
	 */
	public AggregatorService getAggService() {
		return aggService;
	}

	/**
	 * @param service the service to set
	 */
	public void setAggService(AggregatorService aggService) {
		this.aggService = aggService;
	}


	/**
	 * @return the hubService
	 */
	public HubService getHubService() {
		return hubService;
	}


	/**
	 * @param hubService the hubService to set
	 */
	public void setHubService(HubService hubService) {
		this.hubService = hubService;
	}
	
	
	
	
}
