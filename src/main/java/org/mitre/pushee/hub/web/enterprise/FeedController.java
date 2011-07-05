package org.mitre.pushee.hub.web.enterprise;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Feed management controller.
 * 
 * @author AANGANES
 */
@Controller
@RequestMapping("/manager/feeds")
public class FeedController {

	private HubService hubService;
	
	@Autowired
	public FeedController(HubService hubService) {
		this.setHubService(hubService);
	}
	
	/**
	 * Root page. This page should display the list of current feeds.
	 * 
	 * @param  modelAndView  MAV object
	 * @return               feedIndex page
	 */
	@RequestMapping(value="/")
	public ModelAndView viewAllFeeds(ModelAndView modelAndView) {

		modelAndView.addObject("feeds", (List<Feed>)hubService.getAllFeeds());
		modelAndView.setViewName("/management/feedIndex");
		
		return modelAndView;
	}
	
	/**
	 * API access to get the list of all feeds.
	 * 
	 * @param  modelAndView  MAV object
	 * @return 
	 * @return               JSON representation of feed list
	 */
	@RequestMapping(value="api/getAllFeeds")
	public ModelAndView apiGetAllFeeds() {	
		List<Feed> feeds = (List<Feed>)hubService.getAllFeeds();

		return new ModelAndView("jsonView", "feeds", feeds);
	}
	
	/**
	 * View details of a particular feed.
	 * 
	 * @param feedId        ID of the feed to view
	 * @param modelAndView  MAV object
	 * @return              viewFeed page
	 */
	@RequestMapping(value="/viewFeed")
	public ModelAndView viewFeed(@RequestParam("feedId") Long feedId, ModelAndView modelAndView) {
		
		Feed theFeed = getExistingFeed(feedId);
		
		modelAndView.addObject("feed", theFeed);
		modelAndView.setViewName("/management/viewFeed");
		return modelAndView;
	}
	
	/**
	 * API access to get a single feed by ID
	 * 
	 * @param feedId        ID of the feed to get
	 * @param modelAndView  MAV object
	 * @return              JSON representation of the feed
	 */
	@RequestMapping(value="/api/getFeed")
	public ModelAndView apiGetFeed(@RequestParam("feedId") Long feedId, ModelAndView modelAndView) {
		
		Feed f = getExistingFeed(feedId);
		modelAndView.addObject("feed", f);
		modelAndView.setViewName("json");
		return modelAndView;
	}
	
	/**
	 * Add a feed. 
	 * 
	 * @param feedId        ID of the new feed  
	 * @param publisherId   ID of this feed's publisher
	 * @param type          FeedType enum, RSS or ATOM
	 * @param url           url of this feed
	 * @param modelAndView  MAV object
	 * @return              createFeed page with the new feed's info displayed 
	 */
	@RequestMapping(value="/add")
	public ModelAndView addFeed(@RequestParam("feedId") Long feedId, @RequestParam("publisherId") Long publisherId,
								@RequestParam("type") FeedType type, @RequestParam("url") String url, ModelAndView modelAndView) {
		
		Feed newFeed = addFeed(feedId, publisherId, type, url);
		
		modelAndView.addObject("newFeed", newFeed);
		modelAndView.setViewName("management/createFeed");
		
		return modelAndView;
	}

	/**
	 * API access to add a feed
	 * @param feedId
	 * @param publisherId
	 * @param type
	 * @param url
	 * @param modelAndView
	 * @return              JSON representation of the newly created feed
	 */
	@RequestMapping(value="/api/add")
	public ModelAndView apiAddFeed(@RequestParam("feedId") Long feedId, @RequestParam("publisherId") Long publisherId,
								@RequestParam("type") FeedType type, @RequestParam("url") String url, ModelAndView modelAndView) {
		
		Feed theFeed = addFeed(feedId, publisherId, type, url);
		
		modelAndView.addObject("newFeed", theFeed);
		modelAndView.setViewName("json");
		
		return modelAndView;
	}
	
	/**
	 * Edit a feed. This page should display an editable set of fields 
	 * for the selected feed, and a "submit" button which will post to 
	 * the API editFeed url.
	 * 
	 * @param feedId        ID of the feed to edit
	 * @param modelAndView  MAV object
	 * @return              the editFeed page
	 */
	@RequestMapping(value="/edit")
	public ModelAndView editFeed(@RequestParam("feedId") Long feedId, ModelAndView modelAndView) {
		
		Feed theFeed = getExistingFeed(feedId);
		
		modelAndView.addObject("feed", theFeed);
		modelAndView.setViewName("/management/editFeed");
		
		return modelAndView;
	}

	/**
	 * API access to edit a feed. 
	 * 
	 * @param feedId
	 * @param publisherId
	 * @param type
	 * @param url
	 * @param modelAndView
	 * @return              JSON representation of the feed, post-edit
	 */
	@RequestMapping(value="/api/edit")
	public ModelAndView apiEditFeed(@RequestParam("feedId") Long feedId, @RequestParam("publisherId") Long publisherId,
			@RequestParam("type") FeedType type, @RequestParam("url") String url, ModelAndView modelAndView) {
		
		Feed theFeed = getExistingFeed(feedId);
		
		theFeed.setPublisher(hubService.getPublisherById(publisherId));
		theFeed.setType(type);
		theFeed.setUrl(url);
		
		modelAndView.addObject("feed", theFeed);
		modelAndView.setViewName("json");		
		
		return modelAndView;
	}
	
	/**
	 * This probably doesn't need a separate URL. There should be a 
	 * button on the "viewFeed" and "feedIndex" pages allowing you to 
	 * delete a feed. The button will post to the API remove URL.
	 * 
	 * @param feedId
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value="/remove")
	public ModelAndView removeFeed(@RequestParam("feedId") Long feedId, ModelAndView modelAndView) {
		
		hubService.removeFeedById(feedId);
		
		modelAndView.setViewName("management/feedIndex");
		return modelAndView;
	}

	/**
	 * API access to remove a feed.
	 * 
	 * @param feedId        ID of the feed to delete
	 * @param modelAndView  MAV object
	 * @return              removeSuccess 
	 */
	@RequestMapping(value="/api/remove")
	public ModelAndView apiRemoveFeed(@RequestParam("feedId") Long feedId, ModelAndView modelAndView) {
		
		hubService.removeFeedById(feedId);
		
		modelAndView.setViewName("removeSuccess");
		return modelAndView;
	}
	
	/**
	 * Utility function to get a feed by its id, or 
	 * throw FeedNotFoundException.
	 * 
	 * @param feedId the ID of the feed to find
	 * @return the found feed if it exists
	 */
	private Feed getExistingFeed(Long feedId) {
		
		Feed theFeed = hubService.getFeedById(feedId);
		
		if (theFeed == null) {
			throw new FeedNotFoundException();
		}
		
		return theFeed;
	}
	
	/**
	 * Utility function to add a feed with the specified parameters.
	 * 
	 * @param feedId       the id of the feed
	 * @param publisherId  the id of the feed's publisher (must be a valid, existing publisher)
	 * @param type         FeedType enum, RSS or ATOM
	 * @param url          the URL of this feed
	 * @return             the newly created feed
	 */
	private Feed addFeed(Long feedId, Long publisherId, FeedType type, String url) {
		
		Publisher publisher = hubService.getPublisherById(publisherId);
		
		if (publisher == null) {
			throw new PublisherNotFoundException();
		}
		
		Feed theFeed = new Feed();
		theFeed.setId(feedId);
		theFeed.setPublisher(publisher);
		theFeed.setType(type);
		theFeed.setUrl(url);
		
		hubService.saveFeed(theFeed);
		
		return hubService.getFeedByUrl(url);
	}
	
	/**
	 * @param hubService the hubService to set
	 */
	public void setHubService(HubService hubService) {
		this.hubService = hubService;
	}

	/**
	 * @return the hubService
	 */
	public HubService getHubService() {
		return hubService;
	}
	
}
