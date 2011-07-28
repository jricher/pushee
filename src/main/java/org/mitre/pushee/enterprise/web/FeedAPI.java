package org.mitre.pushee.enterprise.web;

import java.util.Collection;
import java.util.List;

import org.mitre.pushee.hub.model.Feed;
import org.mitre.pushee.hub.model.Feed.FeedType;
import org.mitre.pushee.hub.model.Publisher;
import org.mitre.pushee.hub.model.Subscriber;
import org.mitre.pushee.hub.model.Subscription;
import org.mitre.pushee.hub.service.HubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Feed management controller.
 * 
 * @author AANGANES
 */
@Controller
@RequestMapping("/manager/feeds/api")
public class FeedAPI {

	@Autowired
	private HubService hubService;
	
	/**
	 * API access to get the list of all feeds.
	 * 
	 * @param  modelAndView  MAV object
	 * @return JSON representation of feed list
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value="/getAll")
	public ModelAndView apiGetAllFeeds() {	
		List<Feed> feeds = (List<Feed>)hubService.getAllFeeds();

		return new ModelAndView("jsonFeedView", "feeds", feeds);
	}

	/**
	 * API access to get a single feed by ID
	 * 
	 * @param  feedId        ID of the feed to get
	 * @param  modelAndView  MAV object
	 * @return JSON representation of the feed
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value="/get")
	public ModelAndView apiGetFeed(@RequestParam("feedId") Long feedId) {
		
		Feed feed = hubService.getExistingFeed(feedId);
		
		return new ModelAndView("jsonFeedView", "feed", feed);
	}

	/**
	 * API access to add a feed
	 * 
	 * @param  feedId
	 * @param  publisherId
	 * @param  type
	 * @param  url
	 * @param  modelAndView
	 * @return JSON representation of the newly created feed
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value="/add")
	public ModelAndView apiAddFeed(@RequestParam("publisherId") Long publisherId,
								@RequestParam("type") FeedType type, @RequestParam("url") String url) {
		
		Publisher publisher = hubService.getExistingPublisher(publisherId);
		
		Feed feed = new Feed();
		feed.setPublisher(publisher);
		feed.setType(type);
		feed.setUrl(url);
		
		hubService.saveFeed(feed);
		Feed newFeed = hubService.getFeedByUrl(url);
		
		publisher.addFeed(newFeed);
		hubService.savePublisher(publisher);
		
		return new ModelAndView("jsonFeedView", "newFeed", newFeed);
	}

	/**
	 * API access to edit a feed. 
	 * 
	 * @param  feedId
	 * @param  publisherId
	 * @param  type
	 * @param  url
	 * @param  modelAndView
	 * @return JSON representation of the feed, post-edit
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value="/edit")
	public ModelAndView apiEditFeed(@RequestParam("feedId") Long feedId, @RequestParam("publisherId") Long publisherId,
			@RequestParam("type") FeedType type, @RequestParam("url") String url) {
		
		Feed feed = hubService.getExistingFeed(feedId);
		Publisher publisher = hubService.getExistingPublisher(publisherId);
		
		feed.setPublisher(publisher);
		feed.setType(type);
		feed.setUrl(url);	
		
		hubService.saveFeed(feed);
		Feed retrieved = hubService.getFeedById(feedId);
		
		publisher.addFeed(retrieved);
		hubService.savePublisher(publisher);
		
		return new ModelAndView("jsonFeedView", "feed", retrieved);
	}
		
	/**
	 * API access to remove a feed.
	 * 
	 * @param  feedId        ID of the feed to delete
	 * @param  modelAndView  MAV object
	 * @return removeSuccess 
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value="/delete")
	public ModelAndView apiDeleteFeed(@RequestParam Long feedId, ModelAndView modelAndView) {
		
		//First verify that the feed exists
		Feed feed = hubService.getExistingFeed(feedId);
		
		Collection<Subscription> subscriptions= feed.getSubscriptions();
		for (Subscription subscription: subscriptions) {
			Subscriber subscriber = subscription.getSubscriber();
			subscriber.removeSubscription(feed);
		}
		
		hubService.removeFeedById(feedId);
		
		modelAndView.setViewName("management/successfullyRemoved");
		
		return modelAndView;
	}
}
