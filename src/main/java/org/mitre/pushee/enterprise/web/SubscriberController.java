package org.mitre.pushee.enterprise.web;

import org.mitre.pushee.hub.service.HubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
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

	/**
	 * Root page. Displays list of all subscribers.
	 * 
	 * @param modelAndView
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping("/")
	public ModelAndView viewAllSubscribers(ModelAndView modelAndView) {
		
		modelAndView.addObject("subscribers", hubService.getAllSubscribers());
		modelAndView.setViewName("management/subscriberIndex");
		return modelAndView;
	}
	
	/**
	 * View a single subscribers details
	 * 
	 * @param subId
	 * @param modelAndView
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping("/view")
	public ModelAndView viewSubscriber(@RequestParam("subscriberId") Long subId, ModelAndView modelAndView) {
		
		modelAndView.addObject("subscriber", hubService.getExistingSubscriber(subId));
		modelAndView.setViewName("management/viewSubscriber");
		
		return modelAndView;
	}
	
	/**
	 * Add a new subscriber
	 * 
	 * @param modelAndView
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping("add")
	public ModelAndView addSubscriber(ModelAndView modelAndView) {

		modelAndView.setViewName("management/createSubscriber");
		
		return modelAndView;
	}
	
	/**
	 * Edit an existing subscriber
	 * 
	 * @param subId
	 * @param modelAndView
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping("/edit")
	public ModelAndView editSubscriber(@RequestParam("subscriberId") Long subId, ModelAndView modelAndView) {
		
		modelAndView.addObject("subscriber", hubService.getExistingSubscriber(subId));
		modelAndView.setViewName("management/editSubscriber");
		
		return modelAndView;
	}
	
	/**
	 * Request confirmation from the user before committing the deletion
	 * 
	 * @param subId
	 * @param modelAndView
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping("/remove")
	public ModelAndView removeSubscriber(@RequestParam("subscriberId") Long subId, ModelAndView modelAndView) {
		
		modelAndView.addObject("subscriber", hubService.getExistingSubscriber(subId));
		modelAndView.setViewName("/management/deleteSubscriberConfirm");
		
		return modelAndView;
	}	
}
