package org.mitre.pushee.hub.web.enterprise;

import org.mitre.pushee.hub.exception.SubscriberNotFoundException;
import org.mitre.pushee.hub.model.Subscriber;
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
		this.setHubService(hubService);
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
	 * API access to get list of current Subscribers
	 * 
	 * @return
	 */
	@RequestMapping("/api/getAll")
	public ModelAndView apiGetAllSubscribers() {
		
		return new ModelAndView("jsonSubscriberView", "subscribers", hubService.getAllSubscribers());
	
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
		
		Subscriber s = getExistingSubscriber(subId);
		
		modelAndView.addObject("subscriber", s);
		modelAndView.setViewName("management/viewSubscriber");
		
		return modelAndView;
	}
	
	/**
	 * API access to get a single subscriber
	 * 
	 * @param subId
	 * @return
	 */
	@RequestMapping("/api/get")
	public ModelAndView apiGetSubscriber(@RequestParam("subscriberId") Long subId) {
		
		return new ModelAndView("jsonSubscriberView", "subscriber", getExistingSubscriber(subId));
	
	}
	
	/**
	 * Add a new subscriber
	 * 
	 * @param url
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("add")
	public ModelAndView addSubscriber(@RequestParam("postbackUrl") String url, ModelAndView modelAndView) {
		
		Subscriber s = addSubscriber(url);
		
		modelAndView.addObject("subscriber", s);
		modelAndView.setViewName("management/viewSubscriber");
		
		return modelAndView;
	}
	
	/**
	 * API access to add a new subscriber
	 * 
	 * @param url
	 * @return
	 */
	@RequestMapping("api/add")
	public ModelAndView apiAddSubscriber(@RequestParam("postbackUrl") String url) {
		
		return new ModelAndView("jsonSubscriberView", "subscriber", addSubscriber(url));
		
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
		
		modelAndView.addObject("subscriber", getExistingSubscriber(subId));
		modelAndView.setViewName("management/editSubscriber");
		
		return modelAndView;
	}
	
	/**
	 * API access to edit the postback URL of an existing subscriber
	 * 
	 * @param subId
	 * @param postbackURL
	 * @return
	 */
	@RequestMapping("/api/editUrl")
	public ModelAndView apiEditSubscriber(@RequestParam("subscriberId") Long subId, @RequestParam("postbackUrl") String postbackURL) {
		
		Subscriber s = getExistingSubscriber(subId);
		s.setPostbackURL(postbackURL);
		
		return new ModelAndView("jsonSubscriberView", "subscriber", s);
	}
	
	public ModelAndView removeSubscriber(@RequestParam("subscriberId") Long subId, ModelAndView modelAndView) {
		
		return modelAndView;
	}
	
	/**
	 * Utility function to get an existing subscriber, or throw
	 * SubscriberNowFoundException
	 * 
	 * @param subId ID of the subscriber to find
	 * @return the subscriber, if found
	 */
	private Subscriber getExistingSubscriber(Long subId) {

		Subscriber s = hubService.getSubscriberById(subId);
		
		if (s == null) {
			throw new SubscriberNotFoundException();
		}
		
		return s;
	}
	
	/**
	 * Utility function to add a new subscriber and
	 * return it.
	 * 
	 * @param postbackURL
	 * @return the newly created subscriber
	 */
	private Subscriber addSubscriber(String postbackURL) {
		Subscriber s = new Subscriber();
		s.setPostbackURL(postbackURL);
		hubService.saveSubscriber(s);
		
		return hubService.getSubscriberByCallbackURL(postbackURL);
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
