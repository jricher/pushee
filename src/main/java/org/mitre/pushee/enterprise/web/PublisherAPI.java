package org.mitre.pushee.enterprise.web;

import java.util.Set;

import org.mitre.pushee.hub.model.Feed;
import org.mitre.pushee.hub.model.Publisher;
import org.mitre.pushee.hub.service.HubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/manager/publishers/api")
public class PublisherAPI {

	@Autowired
	private HubService hubService;
	
	/**
	 * API access to get list of current Publishers
	 * 
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping("/getAll")
	public ModelAndView apiGetAllPublishers() {
		
		return new ModelAndView("jsonPublisherView", "publishers", hubService.getAllPublishers());

	}
	
	/**
	 * API access to get a single publisher
	 * 
	 * @param pubId
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping("/get")
	public ModelAndView apiGetPublisher(@RequestParam("publisherId") Long pubId) {
		
		return new ModelAndView("jsonPublisherView", "publisher", hubService.getExistingPublisher(pubId));
	
	}
	
	/**
	 * API access to add a new publisher
	 * 
	 * @param url
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping("/add")
	public ModelAndView apiAddPublisher(@RequestParam("callbackUrl") String url) {

		Publisher p = new Publisher();
		p.setCallbackURL(url);
		hubService.savePublisher(p);
		
		Publisher newPublisher =  hubService.getPublisherByUrl(url);
		
		return new ModelAndView("jsonPublisherView", "publisher", newPublisher);
		
	}
	
	/**
	 * API access to edit the callback URL of an existing publisher
	 * 
	 * @param pubId
	 * @param url
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping("/editUrl")
	public ModelAndView apiEditPublisherUrl(@RequestParam("publisherId") Long pubId, @RequestParam("callbackURL") String url) {
		
		Publisher p = hubService.getExistingPublisher(pubId);
		p.setCallbackURL(url);
		
		return new ModelAndView("jsonPublisherView", "publisher", p);
	}
	
	/**
	 * API access to remove the specified publisher.
	 * 
	 * @param pubId
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping("/remove")
	public ModelAndView apiRemovePublisher(@RequestParam("publisherId") Long pubId, ModelAndView modelAndView) {

		Publisher pub = hubService.getPublisherById(pubId);
		Set<Feed> feeds = pub.getFeeds();
		for (Feed f : feeds) {
			hubService.removeFeedById(f.getId());
		}
		
		hubService.removePublisherById(pubId);
		
		modelAndView.setViewName("management/successfullyRemoved");
		
		return modelAndView;
	}
	
}