package org.mitre.pushee.enterprise.web;

import java.util.List;

import org.mitre.pushee.hub.exception.FeedNotFoundException;
import org.mitre.pushee.hub.model.Feed;
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
		this.hubService = hubService;
	}
	
	/**
	 * Root page. This page should display the list of current feeds.
	 * 
	 * @param  modelAndView  MAV object
	 * @return feedIndex page
	 */
	@RequestMapping(value="/")
	public ModelAndView viewAllFeeds(ModelAndView modelAndView) {

		modelAndView.addObject("feeds", (List<Feed>)hubService.getAllFeeds());
		modelAndView.setViewName("/management/feedIndex");
		
		return modelAndView;
	}
	
	/**
	 * View details of a particular feed.
	 * 
	 * @param  feedId        ID of the feed to view
	 * @param  modelAndView  MAV object
	 * @return viewFeed page
	 */
	@RequestMapping(value="/view")
	public ModelAndView viewFeed(@RequestParam("feedId") Long feedId, ModelAndView modelAndView) {
		
		Feed theFeed = EnterpriseUtils.getExistingFeed(feedId);
		
		modelAndView.addObject("feed", theFeed);
		modelAndView.setViewName("/management/viewFeed");
		return modelAndView;
	}
	
	/**
	 * Add a feed. 
	 * 
	 * @param  modelAndView  MAV object
	 * @return createFeed page with the new feed's info displayed 
	 */
	@RequestMapping(value="/add")
	public ModelAndView addFeed(ModelAndView modelAndView) {
		
		modelAndView.setViewName("management/createFeed");
		
		return modelAndView;
	}
	
	/**
	 * Edit a feed. This page should display an editable set of fields 
	 * for the selected feed, and a "submit" button which will post to 
	 * the API editFeed url.
	 * 
	 * @param  feedId        ID of the feed to edit
	 * @param  modelAndView  MAV object
	 * @return the editFeed page
	 */
	@RequestMapping(value="/edit")
	public ModelAndView editFeed(@RequestParam("feedId") Long feedId, ModelAndView modelAndView) {
		
		Feed theFeed = EnterpriseUtils.getExistingFeed(feedId);
		
		modelAndView.addObject("feed", theFeed);
		modelAndView.setViewName("/management/editFeed");
		
		return modelAndView;
	}

	/**
	 * Prompt the user to confirm deletion before committing the action
	 * 
	 * @param  feedId
	 * @param  modelAndView
	 * @return delete confirmation page
	 */
	@RequestMapping(value="/delete")
	public Object deleteFeedConfirmation(ModelAndView modelAndView, @RequestParam Long feedId) {
		
		Feed feed = EnterpriseUtils.getExistingFeed(feedId);
		
		modelAndView.addObject("feed", feed);
		modelAndView.setViewName("/management/deleteFeedConfirm");
		
		return modelAndView;
	}
}
