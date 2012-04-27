package org.mitre.pushee.enterprise.web;

import org.mitre.pushee.hub.model.Publisher;
import org.mitre.pushee.hub.service.HubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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

	@Autowired
	private HubService hubService;
	
	public PublisherController() {
		
	}
	
	public PublisherController(HubService hubService) {
		this.hubService = hubService;
	}
	
	/**
	 * Redirect to the "/" version of the root
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("")
	public ModelAndView redirectRoot(ModelAndView modelAndView) {
		modelAndView.setViewName("redirect:/manager/publishers/");
		return modelAndView;
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
		modelAndView.setViewName("management/publisher/publisherIndex");
		
		return modelAndView;
	}
	
	/**
	 * View a single publisher's details
	 * 
	 * @param pubId
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("/view/{publisherId}")
	public ModelAndView viewPublisher(@PathVariable("publisherId") Long pubId, ModelAndView modelAndView) {
		
		Publisher publisher = hubService.getExistingPublisher(pubId);
		
		modelAndView.addObject("publisher", publisher);
		modelAndView.setViewName("management/publisher/viewPublisher");
		
		return modelAndView;
	}
	
	@RequestMapping("/add")
	public ModelAndView redirectAdd(ModelAndView modelAndView) {
		modelAndView.setViewName("redirect:/manager/publishers/add/");
		return modelAndView;
	}
	
	/**
	 * Add a new publisher 
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("/add/")
	public ModelAndView addPublisher(ModelAndView modelAndView) {

		modelAndView.addObject("publisher", new Publisher());
		modelAndView.addObject("mode", "add");
		modelAndView.setViewName("management/publisher/editPublisher");
		
		return modelAndView;
	}

	/**
	 * Edit an existing publisher
	 * 
	 * @param pubId
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("/edit/{publisherId}")
	public ModelAndView editPublisher(@PathVariable("publisherId") Long pubId, ModelAndView modelAndView) {
		
		Publisher publisher = hubService.getExistingPublisher(pubId);
		
		modelAndView.addObject("mode", "edit");
		modelAndView.addObject("publisher", publisher);
		modelAndView.setViewName("management/publisher/editPublisher");
		
		return modelAndView;
	}
	
	/**
	 * Remove the specified publisher.
	 * 
	 * @param pubId
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("/delete/{publisherId}")
	public ModelAndView deletePublisherConfirmation(@PathVariable("publisherId") Long pubId, ModelAndView modelAndView) {
		
		Publisher publisher = hubService.getExistingPublisher(pubId);
		
		modelAndView.addObject("publisher", publisher);
		modelAndView.setViewName("management/publisher/deletePublisherConfirm");
		return modelAndView;
	}
	
}
