package org.mitre.pushee.enterprise.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.mitre.pushee.hub.model.Feed;
import org.mitre.pushee.hub.model.Publisher;
import org.mitre.pushee.hub.service.HubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Feed management controller.
 * 
 * @author AANGANES
 */
@Controller
@RequestMapping("/manager/feeds")
public class FeedController {

	@Autowired
	private HubService hubService;
	
	public FeedController() {
		
	}
	
	public FeedController(HubService hubService) {
		this.hubService = hubService;
	}

	/**
	 * Redirect to the "/" version of the root
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("")
	public ModelAndView redirectRoot(ModelAndView modelAndView) {
		modelAndView.setViewName("redirect:/manager/feeds/");
		return modelAndView;
	}
	
	/**
	 * Root page. This page should display the list of current feeds.
	 * 
	 * @param  modelAndView  MAV object
	 * @return feedIndex page
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value="/")
	public ModelAndView viewAllFeeds(ModelAndView modelAndView) {

		Collection<Feed> feeds = hubService.getAllFeeds();
		
		modelAndView.addObject("feeds", feeds);
		modelAndView.setViewName("management/feed/feedIndex");
		
		return modelAndView;
	}
	
	/**
	 * View details of a particular feed.
	 * 
	 * @param  feedId        ID of the feed to view
	 * @param  modelAndView  MAV object
	 * @return viewFeed page
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value="/view/{feedId}")
	public ModelAndView viewFeed(@PathVariable Long feedId, ModelAndView modelAndView) {
		
		Feed feed = hubService.getExistingFeed(feedId);
		
		modelAndView.addObject("feed", feed);
		modelAndView.setViewName("management/feed/viewFeed");
		return modelAndView;
	}
	
	@RequestMapping("/add")
	public ModelAndView redirectAdd(ModelAndView modelAndView) {
		modelAndView.setViewName("redirect:/manager/feeds/add/");
		return modelAndView;
	}
	
	/**
	 * Add a feed. 
	 * 
	 * @param  modelAndView  MAV object
	 * @return editFeed page with mode set to "add"
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value="/add/")
	public ModelAndView addFeed(ModelAndView modelAndView) {
		
		Collection<Publisher> publishers = hubService.getAllPublishers();
		
		Feed feed = new Feed();
		
		modelAndView.addObject("mode", "add");
		modelAndView.addObject("publishers", publishers);
		modelAndView.addObject("feed", feed);
		modelAndView.setViewName("management/feed/editFeed");
		
		return modelAndView;
	}
	
	/**
	 * Edit a feed. This page should display an editable set of fields 
	 * for the selected feed, and a "submit" button which will post to 
	 * the API editFeed url.
	 * 
	 * @param  feedId        ID of the feed to edit
	 * @param  modelAndView  MAV object
	 * @return editFeed page with mode set to "edit"
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value="/edit/{feedId}")
	public ModelAndView editFeed(@PathVariable Long feedId, ModelAndView modelAndView) {
		
		Collection<Publisher> publishers = hubService.getAllPublishers();
		Feed feed = hubService.getExistingFeed(feedId);
		
		modelAndView.addObject("mode", "edit");
		modelAndView.addObject("publishers", publishers);
		modelAndView.addObject("feed", feed);
		modelAndView.setViewName("management/feed/editFeed");
		
		return modelAndView;
	}

	/**
	 * Prompt the user to confirm deletion before committing the action
	 * 
	 * @param  feedId
	 * @param  modelAndView
	 * @return delete confirmation page
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value="/delete/{feedId}")
	public ModelAndView deleteFeedConfirmation(@PathVariable Long feedId, ModelAndView modelAndView) {
		
		Feed feed = hubService.getExistingFeed(feedId);
		
		modelAndView.addObject("feed", feed);
		modelAndView.setViewName("management/feed/deleteFeedConfirm");
		
		return modelAndView;
	}
}
