package org.mitre.pushee.enterprise.web;

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
		
		this.hubService = hubService;
		
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
	 * View a single publisher's details
	 * 
	 * @param pubId
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("/view")
	public ModelAndView viewPublisher(@RequestParam("publisherId") Long pubId, ModelAndView modelAndView) {
		
		Publisher p = hubService.getExistingPublisher(pubId);
		
		modelAndView.addObject("publisher", p);
		modelAndView.setViewName("management/viewPublisher");
		
		return modelAndView;
	}
	
	/**
	 * Add a new publisher 
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("add")
	public ModelAndView addPublisher(ModelAndView modelAndView) {

		modelAndView.setViewName("management/createPublisher");
		
		return modelAndView;
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
		
		modelAndView.addObject("publisher", hubService.getExistingPublisher(pubId));
		modelAndView.setViewName("management/editPublisher");
		
		return modelAndView;
	}
	
	/**
	 * Remove the specified publisher.
	 * 
	 * @param pubId
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("/delete")
	public ModelAndView deletePublisherConfirmation(@RequestParam("publisherId") Long pubId, ModelAndView modelAndView) {
		
		Publisher pub = hubService.getExistingPublisher(pubId);
		
		modelAndView.addObject("publisher", pub);
		modelAndView.setViewName("/management/deletePublisherConfirm");
		return modelAndView;
	}
	
}
