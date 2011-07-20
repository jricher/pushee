package org.mitre.pushee.enterprise.web;

import java.util.List;

import org.mitre.pushee.hub.exception.FeedNotFoundException;
import org.mitre.pushee.hub.exception.PublisherNotFoundException;
import org.mitre.pushee.hub.model.Feed;
import org.mitre.pushee.hub.model.Feed.FeedType;
import org.mitre.pushee.hub.model.Publisher;
import org.mitre.pushee.hub.service.HubService;
import org.springframework.beans.factory.annotation.Autowired;
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

	private HubService hubService;
	
	@Autowired
	public FeedAPI(HubService hubService) {
		this.hubService = hubService;
	}
	
	/**
	 * API access to get the list of all feeds.
	 * 
	 * @param  modelAndView  MAV object
	 * @return JSON representation of feed list
	 */
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
	@RequestMapping(value="/get")
	public ModelAndView apiGetFeed(@RequestParam("feedId") Long feedId) {
		
		return new ModelAndView("jsonFeedView", "feed", hubService.getExistingFeed(feedId));
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
	@RequestMapping(value="/add")
	public ModelAndView apiAddFeed(@RequestParam("publisherId") Long publisherId,
								@RequestParam("type") FeedType type, @RequestParam("url") String url) {
		
		Publisher publisher = hubService.getExistingPublisher(publisherId);
		
		Feed theFeed = new Feed();
		theFeed.setPublisher(publisher);
		theFeed.setType(type);
		theFeed.setUrl(url);
		
		hubService.saveFeed(theFeed);
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
	@RequestMapping(value="/edit")
	public ModelAndView apiEditFeed(@RequestParam("feedId") Long feedId, @RequestParam("publisherId") Long publisherId,
			@RequestParam("type") FeedType type, @RequestParam("url") String url) {
		
		Feed theFeed = hubService.getExistingFeed(feedId);
		
		theFeed.setPublisher(hubService.getExistingPublisher(publisherId));
		theFeed.setType(type);
		theFeed.setUrl(url);	
		
		hubService.saveFeed(theFeed);
		
		return new ModelAndView("jsonFeedView", "feed", hubService.getFeedById(feedId));
	}
		
	/**
	 * API access to remove a feed.
	 * 
	 * @param  feedId        ID of the feed to delete
	 * @param  modelAndView  MAV object
	 * @return removeSuccess 
	 */
	@RequestMapping(value="/remove")
	public ModelAndView apiRemoveFeed(@RequestParam("feedId") Long feedId, ModelAndView modelAndView) {
		
		//First verify that the feed exists
		hubService.getExistingFeed(feedId);
		
		hubService.removeFeedById(feedId);
		
		modelAndView.setViewName("management/successfullyRemoved");
		
		return modelAndView;
	}
}
