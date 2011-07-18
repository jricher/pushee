package org.mitre.pushee.enterprise.web;

import java.util.Calendar;
import java.util.Collection;

import org.mitre.pushee.hub.model.Feed;
import org.mitre.pushee.hub.model.Subscriber;
import org.mitre.pushee.hub.model.Subscription;
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
@RequestMapping("/manager/subscribers/api")
public class SubscriberAPI {

	private HubService hubService;
	
	@Autowired
	public SubscriberAPI(HubService hubService) {
		this.hubService = hubService;
	}
	
	/**
	 * API access to get list of current Subscribers
	 * 
	 * @return
	 */
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
	@RequestMapping("/get")
	public ModelAndView apiGetSubscriber(@RequestParam("subscriberId") Long subId) {
		
		return new ModelAndView("jsonSubscriberView", "subscriber", EnterpriseUtils.getExistingSubscriber(subId));
	
	}
	
	/**
	 * API access to add a new subscriber
	 * 
	 * @param url
	 * @return
	 */
	@RequestMapping("/add")
	public ModelAndView apiAddSubscriber(@RequestParam("postbackUrl") String url) {
		
		Subscriber s = new Subscriber();
		s.setPostbackURL(url);
		hubService.saveSubscriber(s);
		
		return new ModelAndView("jsonSubscriberView", "subscriber", hubService.getSubscriberByCallbackURL(url));
		
	}
	
	/**
	 * API access to edit the postback URL of an existing subscriber
	 * 
	 * @param  subId
	 * @param  postbackURL
	 * @return
	 */
	@RequestMapping("/editUrl")
	public ModelAndView apiEditSubscriber(@RequestParam("subscriberId") Long subId, @RequestParam("postbackUrl") String postbackURL) {
		
		Subscriber s = EnterpriseUtils.getExistingSubscriber(subId);
		s.setPostbackURL(postbackURL);
		hubService.saveSubscriber(s);
		
		return new ModelAndView("jsonSubscriberView", "subscriber", s);
	}
	
	/**
	 * API access to add a subscription from a given subscriber
	 * to a given feed.
	 * 
	 * @param  subId  the ID of the subscriber
	 * @param  feedId the ID of the feed to subscribe to
	 * @return the modified subscriber
	 */
	@RequestMapping("/addSubscription")
	public ModelAndView apiAddSubscription(@RequestParam("subscriberId") Long subId, @RequestParam("feedId") Long feedId, 
			@RequestParam(required=false, value="secret") String secret, @RequestParam(value="leaseSeconds", required=false) Integer leaseSeconds) {
		
		Subscriber s = EnterpriseUtils.getExistingSubscriber(subId);
		Feed f = EnterpriseUtils.getExistingFeed(feedId);
		Subscription sub = new Subscription();
		
		sub.setFeed(f);
		sub.setSubscriber(s);
		
		if (secret != null) {
			sub.setSecret(secret);
		}
		if (leaseSeconds != null) {
			Calendar now = Calendar.getInstance();
			now.add(Calendar.SECOND, leaseSeconds);
			sub.setTimeout(now);
		}
		
		s.addSubscription(sub);
		hubService.saveSubscriber(s);
		
		Subscriber retrieved = hubService.getSubscriberById(subId);
		
		return new ModelAndView("jsonSubscriberView", "subscriber", retrieved);
	}
	
	/**
	 * API access to remove a subscriber. Also removes references to subscriptions
	 * this subscriber had in any associated Feed objects.
	 * 
	 * @param  subId
	 * @return
	 */
	@RequestMapping("/remove")
	public ModelAndView apiRemoveSubscriber(@RequestParam("subscriberId") Long subId, ModelAndView modelAndView) {
		
		Subscriber s = EnterpriseUtils.getExistingSubscriber(subId);
		Collection<Subscription> subscriptions = s.getSubscriptions();
		
		for (Subscription sub : subscriptions) {
			
			Feed f = sub.getFeed();
			f.removeSubscription(sub);
			hubService.saveFeed(f);
			
		}
		
		hubService.removeSubscriberById(subId);
		
		modelAndView.setViewName("management/successfullyRemoved");
		
		return modelAndView;
	}
}
