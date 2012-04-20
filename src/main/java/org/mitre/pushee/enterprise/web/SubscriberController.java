package org.mitre.pushee.enterprise.web;

import org.mitre.pushee.hub.model.Subscriber;
import org.mitre.pushee.hub.service.HubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Subscriber manager controller.
 * 
 * @author AANGANES
 *
 */
@Controller
@RequestMapping("/manager/subscribers")
public class SubscriberController {

	@Autowired
	private HubService hubService;

	public SubscriberController() {
		
	}
	
	public SubscriberController(HubService hubService) {
		this.hubService = hubService;
	}
	
	/**
	 * Redirect to the "/" version of the root
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("")
	public ModelAndView redirectRoot(ModelAndView modelAndView) {
		modelAndView.setViewName("redirect:/manager/subscribers/");
		return modelAndView;
	}
	
	/**
	 * Root page. Displays list of all subscribers.
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("/")
	public ModelAndView viewAllSubscribers(ModelAndView modelAndView) {
		
		modelAndView.addObject("subscribers", hubService.getAllSubscribers());
		modelAndView.setViewName("management/subscriber/subscriberIndex");
		return modelAndView;
	}
	
	/**
	 * View a single subscribers details
	 * 
	 * @param subId
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("/view/{subscriberId}")
	public ModelAndView viewSubscriber(@PathVariable Long subscriberId, ModelAndView modelAndView) {
		
		Subscriber subscriber = hubService.getExistingSubscriber(subscriberId);
		
		modelAndView.addObject("subscriber", subscriber);
		modelAndView.setViewName("management/subscriber/viewSubscriber");
		
		return modelAndView;
	}
	
	/**
	 * Add a new subscriber
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("/add/")
	public ModelAndView addSubscriber(ModelAndView modelAndView) {

		modelAndView.addObject("mode", "add");
		modelAndView.addObject("feeds", hubService.getAllFeeds());
		modelAndView.addObject("subscriber", new Subscriber());
		modelAndView.setViewName("management/subscriber/editSubscriber");
		
		return modelAndView;
	}
	
	/**
	 * Edit an existing subscriber
	 * 
	 * @param subId
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("/edit/{subscriberId}")
	public ModelAndView editSubscriber(@PathVariable Long subscriberId, ModelAndView modelAndView) {
		
		Subscriber subscriber = hubService.getExistingSubscriber(subscriberId);
		
		modelAndView.addObject("subscriber", subscriber);
		modelAndView.addObject("feeds", hubService.getAllFeeds());
		modelAndView.addObject("mode", "edit");
		modelAndView.setViewName("management/subscriber/editSubscriber");
		
		return modelAndView;
	}
	
	/**
	 * Request confirmation from the user before committing the deletion
	 * 
	 * @param subId
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("/delete/{subscriberId}")
	public ModelAndView deleteSubscriberConfirmation(@PathVariable Long subscriberId, ModelAndView modelAndView) {
		
		Subscriber subscriber = hubService.getExistingSubscriber(subscriberId);
		
		modelAndView.addObject("subscriber", subscriber);
		modelAndView.setViewName("management/subscriber/deleteSubscriberConfirm");
		
		return modelAndView;
	}	
}
