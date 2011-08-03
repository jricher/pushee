package org.mitre.pushee.enterprise.web;

import java.util.Calendar;
import java.util.Collection;

import org.mitre.pushee.hub.model.Feed;
import org.mitre.pushee.hub.model.Subscriber;
import org.mitre.pushee.hub.model.Subscription;
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
@RequestMapping("/manager/subscribers/api")
public class SubscriberAPI {

	@Autowired
	private HubService hubService;
	
	public SubscriberAPI() {
		
	}
	
	public SubscriberAPI(HubService hubService) {
		this.hubService = hubService;
	}
	
	
	/**
	 * API access to get list of current Subscribers
	 * 
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping("/getAll")
	public ModelAndView apiGetAllSubscribers() {
		
		return new ModelAndView("jsonSubscriberView", "subscribers", hubService.getAllSubscribers());
	
	}
	
	/**
	 * API access to get a single subscriber
	 * 
	 * @param subId
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping("/get")
	public ModelAndView apiGetSubscriber(@RequestParam("subscriberId") Long subId) {
		
		Subscriber subscriber = hubService.getExistingSubscriber(subId);
		
		return new ModelAndView("jsonSubscriberView", "subscriber", subscriber);
	
	}
	
	/**
	 * API access to add a new subscriber
	 * 
	 * @param url
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping("/add")
	public ModelAndView apiAddSubscriber(@RequestParam("postbackUrl") String url) {
		
		Subscriber subscriber = new Subscriber();
		subscriber.setPostbackURL(url);
		hubService.saveSubscriber(subscriber);
		
		Subscriber retrieved = hubService.getSubscriberByCallbackURL(url);
		
		return new ModelAndView("jsonSubscriberView", "subscriber", retrieved);
		
	}
	
	/**
	 * API access to edit the postback URL of an existing subscriber
	 * 
	 * @param  subId
	 * @param  postbackURL
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping("/edit")
	public ModelAndView apiEditSubscriber(@RequestParam("subscriberId") Long subscriberId, @RequestParam("postbackUrl") String postbackURL) {
		
		Subscriber subscriber = hubService.getExistingSubscriber(subscriberId);
		subscriber.setPostbackURL(postbackURL);
		hubService.saveSubscriber(subscriber);
		
		Subscriber retrieved = hubService.getExistingSubscriber(subscriberId);
		
		return new ModelAndView("jsonSubscriberView", "subscriber", retrieved);
	}
	
	/**
	 * API access to remove a subscription from a given subscriber
	 * to a given feed.
	 * 
	 * @param  subId  the ID of the subscriber
	 * @param  feedId the ID of the feed to remove subscription from
	 * @return the modified subscriber
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping("/removeSubscription")
	public ModelAndView apiRemoveSubscription(@RequestParam("subscriberId") Long subscriberId, @RequestParam("feedId") Long feedId) {
		
		Subscriber subscriber = hubService.getExistingSubscriber(subscriberId);
		Feed feed = hubService.getExistingFeed(feedId);
		
		subscriber.removeSubscription(feed);
		hubService.saveSubscriber(subscriber);
		
		Subscriber retrieved = hubService.getExistingSubscriber(subscriberId);
		
		return new ModelAndView("jsonSubscriberView", "subscriber", retrieved);
	}
	
	/**
	 * API access to remove a subscriber. Also removes references to subscriptions
	 * this subscriber had in any associated Feed objects.
	 * 
	 * @param  subscriberId
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping("/delete")
	public ModelAndView apiDeleteSubscriber(@RequestParam("subscriberId") Long subscriberId, ModelAndView modelAndView) {
		
		Subscriber subscriber = hubService.getExistingSubscriber(subscriberId);
		Collection<Subscription> subscriptions = subscriber.getSubscriptions();
		
		for (Subscription sub : subscriptions) {
			
			Feed feed = sub.getFeed();
			feed.removeSubscription(sub);
			hubService.saveFeed(feed);
			
		}
		
		hubService.removeSubscriberById(subscriberId);
		
		modelAndView.setViewName("management/successfullyRemoved");
		
		return modelAndView;
	}
}
