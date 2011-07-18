package org.mitre.pushee.enterprise.web;

import org.mitre.pushee.hub.service.HubService;
import org.springframework.beans.factory.annotation.Autowired;
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

	private HubService hubService;
	
	@Autowired
	public SubscriberController(HubService hubService) {
		this.hubService = hubService;
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
	@RequestMapping("/view")
	public ModelAndView viewSubscriber(@RequestParam("subscriberId") Long subId, ModelAndView modelAndView) {
		
		modelAndView.addObject("subscriber", EnterpriseUtils.getExistingSubscriber(subId));
		modelAndView.setViewName("management/viewSubscriber");
		
		return modelAndView;
	}
	
	/**
	 * Add a new subscriber
	 * 
	 * @param modelAndView
	 * @return
	 */
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
	@RequestMapping("/edit")
	public ModelAndView editSubscriber(@RequestParam("subscriberId") Long subId, ModelAndView modelAndView) {
		
		modelAndView.addObject("subscriber", EnterpriseUtils.getExistingSubscriber(subId));
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
	@RequestMapping("/remove")
	public ModelAndView removeSubscriber(@RequestParam("subscriberId") Long subId, ModelAndView modelAndView) {
		
		modelAndView.addObject("subscriber", EnterpriseUtils.getExistingSubscriber(subId));
		modelAndView.setViewName("/management/deleteSubscriberConfirm");
		
		return modelAndView;
	}	
}
