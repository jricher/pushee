package org.mitre.pushee.hub.web.enterprise;

import java.util.Set;

import org.mitre.pushee.hub.exception.PublisherNotFoundException;
import org.mitre.pushee.hub.model.Feed;
import org.mitre.pushee.hub.model.Publisher;
import org.mitre.pushee.hub.service.HubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Publisher manager controller
 * 
 * @author AANGANES
 *
 */
@Controller
@RequestMapping("/manager/publishers")
public class PublisherController {

	private HubService hubService;
	
	@Autowired
	public PublisherController(HubService hubService) {
		
		this.setHubService(hubService);
		
	}
	
	/**
	 * Root page. Displays list of all Publishers
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("/")
	public ModelAndView viewAllPublishers(ModelAndView modelAndView) {
		
		modelAndView.addObject("publishers", hubService.getAllPublishers());
		modelAndView.setViewName("management/publisherIndex");
		
		return modelAndView;
	}
	
	/**
	 * API access to get list of current Publishers
	 * 
	 * @return
	 */
	@RequestMapping("/api/getAll")
	public ModelAndView apiGetAllPublishers() {
		
		return new ModelAndView("jsonPublisherView", "publishers", hubService.getAllPublishers());

	}
	
	/**
	 * View a single publisher's details
	 * 
	 * @param pubId
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("/view")
	public ModelAndView viewPublisher(@RequestParam("publisherId") Long pubId, ModelAndView modelAndView) {
		
		Publisher p = getExistingPublisher(pubId);
		
		modelAndView.addObject("publisher", p);
		modelAndView.setViewName("management/viewPublisher");
		
		return modelAndView;
	}
	
	/**
	 * API access to get a single publisher
	 * 
	 * @param pubId
	 * @return
	 */
	@RequestMapping("/api/get")
	public ModelAndView apiGetPublisher(@RequestParam("publisherId") Long pubId) {
		
		return new ModelAndView("jsonPublisherView", "publisher", getExistingPublisher(pubId));
	
	}
	
	/**
	 * Add a new publisher 
	 * 
	 * @param url
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("add")
	public ModelAndView addPublisher(@RequestParam("callbackUrl") String url, ModelAndView modelAndView) {
		
		Publisher p = addPublisher(url);
		
		modelAndView.addObject("publisher", p);
		modelAndView.setViewName("management/viewPublisher");
		
		return modelAndView;
	}
	
	/**
	 * API access to add a new publisher
	 * 
	 * @param url
	 * @return
	 */
	@RequestMapping("api/add")
	public ModelAndView apiAddPublisher(@RequestParam("callbackUrl") String url) {

		return new ModelAndView("jsonPublisherView", "publisher", addPublisher(url));
		
	}

	/**
	 * Edit an existing publisher
	 * 
	 * @param pubId
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("/edit")
	public ModelAndView editPublisher(@RequestParam("publisherId") Long pubId, ModelAndView modelAndView) {
		
		modelAndView.addObject("publisher", getExistingPublisher(pubId));
		modelAndView.setViewName("management/editPublisher");
		
		return modelAndView;
	}
	
	/**
	 * API access to edit the callback URL of an existing publisher
	 * 
	 * @param pubId
	 * @param url
	 * @return
	 */
	@RequestMapping("/api/editUrl")
	public ModelAndView apiEditPublisherUrl(@RequestParam("publisherId") Long pubId, @RequestParam("callbackURL") String url) {
		
		Publisher p = getExistingPublisher(pubId);
		p.setCallbackURL(url);
		
		return new ModelAndView("jsonPublisherView", "publisher", p);
	}
	
	/**
	 * Remove the specified publisher.
	 * 
	 * @param pubId
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("/remove")
	public Object removePublisher(@RequestParam("publisherId") Long pubId) {
		
		deletePublisherAndAssociatedFeeds(pubId);
		
		return "redirect:/manager/publishers/";
	}
	
	/**
	 * API access to remove the specified publisher.
	 * 
	 * @param pubId
	 * @return
	 */
	@RequestMapping("/api/remove")
	public ModelAndView apiRemovePublisher(@RequestParam("publisherId") Long pubId, ModelAndView modelAndView) {

		deletePublisherAndAssociatedFeeds(pubId);
		modelAndView.setViewName("management/successfullyRemoved");
		
		return modelAndView;
	}
	
	/**
	 * Utility function to get an existing publisher, or throw 
	 * PublisherNotFound exception
	 * 
	 * @param pubId ID of the publisher to find
	 * @return the publisher, if found
	 */
	private Publisher getExistingPublisher(Long pubId) {
		Publisher thePublisher = hubService.getPublisherById(pubId);
		
		if (thePublisher == null) {;
			throw new PublisherNotFoundException();
		}
		
		return thePublisher;
	}
	
	/**
	 * Utility function to add a publisher and return it
	 * 
	 * @param callbackURL
	 * @return the newly created publisher
	 */
	private Publisher addPublisher(String callbackURL) 
	{
		Publisher p = new Publisher();
		p.setCallbackURL(callbackURL);
		hubService.savePublisher(p);
		
		return hubService.getPublisherByUrl(callbackURL);
		
	}
	
	/**
	 * Utility function to delete a publisher and also
	 * remove all associated feeds (feeds cannot exist without 
	 * a publisher to publish them!)
	 * 
	 * @param pubId the ID of the publisher to delete
	 */
	private void deletePublisherAndAssociatedFeeds(Long pubId) {
		Publisher pub = hubService.getPublisherById(pubId);
		Set<Feed> feeds = pub.getFeeds();
		for (Feed f : feeds) {
			hubService.removeFeedById(f.getId());
		}
		
		hubService.removePublisherById(pubId);
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
