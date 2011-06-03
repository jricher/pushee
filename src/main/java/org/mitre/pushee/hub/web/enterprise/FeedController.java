package org.mitre.pushee.hub.web.enterprise;

import org.mitre.pushee.hub.model.Feed;
import org.mitre.pushee.hub.model.Feed.FeedType;
import org.mitre.pushee.hub.model.Publisher;
import org.mitre.pushee.hub.exception.PublisherNotFoundException;
import org.mitre.pushee.hub.exception.FeedNotFoundException;
import org.mitre.pushee.hub.service.HubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/manager/feeds")
public class FeedController {

	private HubService hubService;
	
	@Autowired
	public FeedController(HubService hubService) {
		this.setHubService(hubService);
	}
	
	@RequestMapping(value="/")
	public ModelAndView viewAllFeeds(ModelAndView modelAndView) {
		
		//TODO: Implement
		
		modelAndView.setViewName("/management/feedIndex");
		
		return modelAndView;
	}
	
	
	@RequestMapping(value="/viewFeed")
	public ModelAndView viewFeed(@RequestParam("feedId") Long feedId, ModelAndView modelAndView) {
		
		Feed theFeed = hubService.getFeedById(feedId);
		
		if (theFeed == null) {
			throw new FeedNotFoundException();
		}
		
		//TODO: Implement
		
		modelAndView.setViewName("/management/viewFeed");
		return modelAndView;
	}
	
	@RequestMapping(value="/add")
	public ModelAndView addFeed(@RequestParam("feedId") Long feedId, @RequestParam("publisherId") Long publisherId,
								@RequestParam("type") FeedType type, @RequestParam("url") String url, ModelAndView modelAndView) {
		
		Publisher publisher = hubService.getPublisherById(publisherId);
		
		if (publisher == null) {
			throw new PublisherNotFoundException();
		}
		
		Feed theFeed = new Feed();
		theFeed.setId(id);
		theFeed.setPublisher(publisher);
		theFeed.setType(type);
		theFeed.setUrl(url);
		
		hubService.saveFeed(theFeed);
		
		modelAndView.setViewName("management/createFeed");
		return modelAndView;
	}
	
	@RequestMapping(value="/edit")
	public ModelAndView editFeed(@RequestParam("feedId") Long feedId, ModelAndView modelAndView) {
		
		Feed theFeed = hubService.getFeedById(feedId);
		
		if (theFeed == null) {
			throw new FeedNotFoundException();
		}
		
		//TODO: Implement
		
		modelAndView.setViewName("/management/editFeed");
		return modelAndView;
	}
	
	@RequestMapping(value="/remove")
	public ModelAndView removeFeed(@RequestParam("feedId") Long feedId, ModelAndView modelAndView) {
		
		//TODO: backend does not support this yet
		hubService.removeFeedById(feedId);
		
		modelAndView.setViewName("management/feedIndex");
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
