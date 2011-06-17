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
	
	@RequestMapping(value="")
	public ModelAndView viewAllFeeds(ModelAndView modelAndView) {
		
		List<Feed> listOfFeeds = apiGetAllFeeds();
		
		modelAndView.addObject("feeds", listOfFeeds);
		
		modelAndView.setViewName("/management/feedIndex");
		
		return modelAndView;
	}
	
	@RequestMapping(value="api/getAllFeeds")
	public List<Feed> apiGetAllFeeds() {
		
		return (List<Feed>)hubService.getAllFeeds();
		
	}
	
	@RequestMapping(value="/viewFeed")
	public ModelAndView viewFeed(@RequestParam("feedId") Long feedId, ModelAndView modelAndView) {
		
		Feed theFeed = apiGetFeed(feedId);
		
		modelAndView.addObject("feed", theFeed);
		
		modelAndView.setViewName("/management/viewFeed");
		return modelAndView;
	}
	
	@RequestMapping(value="/api/getFeed")
	public Feed apiGetFeed(@RequestParam("feedId") Long feedId) {
		
		Feed theFeed = hubService.getFeedById(feedId);
		
		if (theFeed == null) {
			throw new FeedNotFoundException();
		}
		
		return theFeed;
	}
	
	@RequestMapping(value="/add")
	public ModelAndView addFeed(@RequestParam("feedId") Long feedId, @RequestParam("publisherId") Long publisherId,
								@RequestParam("type") FeedType type, @RequestParam("url") String url, ModelAndView modelAndView) {
		
		Feed newFeed = apiAddFeed(feedId, publisherId, type, url);
		
		modelAndView.addObject("newFeed", newFeed);
		
		modelAndView.setViewName("management/createFeed");
		return modelAndView;
	}
	
	@RequestMapping(value="/api/add")
	public Feed apiAddFeed(@RequestParam("feedId") Long feedId, @RequestParam("publisherId") Long publisherId,
								@RequestParam("type") FeedType type, @RequestParam("url") String url) {
		
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
		
		return theFeed;
	}
	
	//TODO: wire things so that the editFeed view puts forth a form where the user can change info,
	//then the "submit" action on the form should make the api call api/edit with the new info
	//and then ... ?
	
	@RequestMapping(value="/edit")
	public ModelAndView editFeed(@RequestParam("feedId") Long feedId, ModelAndView modelAndView) {
		
		Feed theFeed = apiGetFeed(feedId);
		
		modelAndView.addObject("feed", theFeed);
		
		modelAndView.setViewName("/management/editFeed");
		return modelAndView;
	}

	@RequestMapping(value="/api/edit")
	public Feed apiEditFeed(@RequestParam("feedId") Long feedId, ModelAndView modelAndView, @RequestParam("publisherId") Long publisherId,
			@RequestParam("type") FeedType type, @RequestParam("url") String url) {
		
		Feed theFeed = hubService.getFeedById(feedId);
		
		if (theFeed == null) {
			throw new FeedNotFoundException();
		}
		
		theFeed.setPublisher(hubService.getPublisherById(publisherId));
		theFeed.setType(type);
		theFeed.setUrl(url);
		
		return theFeed;
	}
	
	@RequestMapping(value="/remove")
	public ModelAndView removeFeed(@RequestParam("feedId") Long feedId, ModelAndView modelAndView) {
		
		hubService.removeFeedById(feedId);
		
		modelAndView.setViewName("management/feedIndex");
		return modelAndView;
	}

	@RequestMapping(value="/api/remove")
	public ModelAndView apiRemoveFeed(@RequestParam("feedId") Long feedId, ModelAndView modelAndView) {
		
		hubService.removeFeedById(feedId);
		
		modelAndView.setViewName("removeSuccess");
		return modelAndView;
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
