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
	
	public PublisherAPI() {
		
	}
	
	public PublisherAPI(HubService hubService) {
		this.hubService = hubService;
	}
	
	/**
	 * API access to get list of current Publishers
	 * 
	 * @return
	 */
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
	@RequestMapping("/get")
	public ModelAndView apiGetPublisher(@RequestParam("publisherId") Long publisherId) {
		
		Publisher publisher = hubService.getExistingPublisher(publisherId);
		
		return new ModelAndView("jsonPublisherView", "publisher", publisher);
	
	}
	
	/**
	 * API access to add a new publisher
	 * 
	 * @param url
	 * @return
	 */
	@RequestMapping("/add")
	public ModelAndView apiAddPublisher(@RequestParam("callbackUrl") String url) {

		Publisher publisher = new Publisher();
		publisher.setCallbackURL(url);
		hubService.savePublisher(publisher);
		
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
	@RequestMapping("/edit")
	public ModelAndView apiEditPublisher(@RequestParam("publisherId") Long publisherId, @RequestParam("callbackURL") String url) {
		
		Publisher publisher = hubService.getExistingPublisher(publisherId);
		publisher.setCallbackURL(url);
		hubService.savePublisher(publisher);
		
		Publisher retrieved =  hubService.getExistingPublisher(publisherId);
		
		return new ModelAndView("jsonPublisherView", "publisher", retrieved);
	}
	
	/**
	 * API access to remove the specified publisher.
	 * 
	 * @param pubId
	 * @return
	 */
	@RequestMapping("/remove")
	public ModelAndView apiRemovePublisher(@RequestParam("publisherId") Long pubId, ModelAndView modelAndView) {

		Publisher pub = hubService.getExistingPublisher(pubId);
		Set<Feed> feeds = pub.getFeeds();
		for (Feed f : feeds) {
			hubService.removeFeedById(f.getId());
		}
		
		hubService.removePublisherById(pubId);
		
		modelAndView.setViewName("management/successfullyRemoved");
		
		return modelAndView;
	}
	
}
