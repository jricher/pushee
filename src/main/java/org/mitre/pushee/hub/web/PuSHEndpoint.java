package org.mitre.pushee.hub.web;

import static org.mitre.pushee.hub.web.PuSHProtocolParameters.HUB_CALLBACK;
import static org.mitre.pushee.hub.web.PuSHProtocolParameters.HUB_LEASE_SECONDS;
import static org.mitre.pushee.hub.web.PuSHProtocolParameters.HUB_MODE;
import static org.mitre.pushee.hub.web.PuSHProtocolParameters.HUB_MODE_PUBLISH;
import static org.mitre.pushee.hub.web.PuSHProtocolParameters.HUB_MODE_SUBSCRIBE;
import static org.mitre.pushee.hub.web.PuSHProtocolParameters.HUB_MODE_UNSUBSCRIBE;
import static org.mitre.pushee.hub.web.PuSHProtocolParameters.HUB_SECRET;
import static org.mitre.pushee.hub.web.PuSHProtocolParameters.HUB_TOPIC;
import static org.mitre.pushee.hub.web.PuSHProtocolParameters.HUB_URL;
import static org.mitre.pushee.hub.web.PuSHProtocolParameters.HUB_VERIFY;
import static org.mitre.pushee.hub.web.PuSHProtocolParameters.HUB_VERIFY_TOKEN;

import java.util.Collection;
import java.util.List;

import org.mitre.pushee.hub.exception.FeedNotFoundException;
import org.mitre.pushee.hub.model.Feed;
import org.mitre.pushee.hub.model.Subscriber;
import org.mitre.pushee.hub.model.Subscription;
import org.mitre.pushee.hub.service.HubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/hub")
public class PuSHEndpoint {

	private final HubService hubService;

	@Autowired
	private HttpClientUtils http;
	
    @Autowired
    public PuSHEndpoint(HubService hubService) {
        this.hubService = hubService;
    }

	/**
	 * Process a "subscribe" request
	 * @param callback
	 * @param topic
	 * @param verify
	 * @param leaseSeconds
	 * @param secret
	 * @param verifyToken
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(params={HUB_MODE_SUBSCRIBE,
							HUB_CALLBACK,HUB_TOPIC,HUB_VERIFY}, 
							method=RequestMethod.POST)
	public ModelAndView subscribeRequest(
			@RequestParam(HUB_MODE) String mode,
			@RequestParam(HUB_CALLBACK) String callback,
			@RequestParam(HUB_TOPIC) String topic,
			@RequestParam(HUB_VERIFY) ClientVerify verify,
			@RequestParam(value=HUB_LEASE_SECONDS, required=false, defaultValue="0") long leaseSeconds,
			@RequestParam(value=HUB_SECRET, required=false) String secret,
			@RequestParam(value=HUB_VERIFY_TOKEN, required=false) String verifyToken,
			ModelAndView modelAndView) {

		// Load the subscriber from its callback url, if available
		Subscriber sub = hubService.getSubscriberByCallbackURL(callback);
		// create a subscriber {callback,verify} if not available, and save it
		if (sub == null) {
			sub = new Subscriber();
			sub.setPostbackURL(callback);
		}
		// get the feed object from the topic url
		Feed f = hubService.getFeedByUrl(topic);
		//   -- return 404 if not found, 403 if not allowed, etc
		if (f == null) {
			throw new FeedNotFoundException();
		}
		
		/*
		if (not allowed) {
			throw new PermissionDeniedException();
		}
		*/
		
		// (for now, only support sync verification)
		// do a sync post to the callback URL to verify
		if (http.verifyCallback(callback, mode, topic, leaseSeconds, verifyToken)) {
		
			// check verification contents, continue
			// create a subscription {subscriber,feed,timeout)
			Subscription subscript = new Subscription();
			subscript.setFeed(f);
			subscript.setSubscriber(sub);
			//etc
			// store the subscription details, overwriting old subscription if found
			sub.addSubscription(subscript);
			//hubService.saveSubscription?
			
			// return a 204 for valid subscript 
			modelAndView.setViewName("validSubscription");
		} else {
			// no verification, throw an error
			
		}
		// TODO: if we do the callback async, return 202 instead
		         //modelAndView.setViewName("accepted");
		return modelAndView;
	}
	
	/**
	 * process an "unsubscribe" request
	 * @param callback
	 * @param topic
	 * @param verify
	 * @param leaseSeconds
	 * @param secret
	 * @param verifyToken
	 * @param mav
	 * @return
	 */
	@RequestMapping(params={HUB_MODE_UNSUBSCRIBE,
			HUB_CALLBACK,HUB_TOPIC,HUB_VERIFY}, 
			method=RequestMethod.POST)
	public ModelAndView unsubscribeRequest(
			@RequestParam(HUB_MODE) String mode,
			@RequestParam(HUB_CALLBACK) String callback,
			@RequestParam(HUB_TOPIC) String topic,
			@RequestParam(HUB_VERIFY) ClientVerify verify,
			@RequestParam(value=HUB_LEASE_SECONDS, required=false, defaultValue="0") long leaseSeconds,
			@RequestParam(value=HUB_SECRET, required=false) String secret,
			@RequestParam(value=HUB_VERIFY_TOKEN, required=false) String verifyToken,			
			ModelAndView mav) {
		
		// Load the subscriber from its callback url, if available
		Subscriber sub = hubService.getSubscriberByCallbackURL(callback);
		
		// get the feed object from the topic url
		Feed f = hubService.getFeedByUrl(topic);
		//   -- return 404 if not found, 403 if not allowed, etc
		if (f == null) {
			throw new FeedNotFoundException();
		}
		
		/*
		if (not allowed) {
			throw new PermissionDeniedException();
		}
		*/
		
		// (for now, only support sync verification)
		// do a sync post to the callback URL to verify
		if (http.verifyCallback(callback, mode, topic, leaseSeconds, verifyToken)) {
			
			// check verification contents, continue
			// delete the subscription from our store
			sub.removeSubscription(f);
			// return a 204 for valid unsubscription
			mav.setViewName("unsubscribeSuccess");
			// TODO: if we do the callback async, return 202 instead
			         //mav.setViewName("accepted");
		} else {
			// no verification, throw an error
		}
		return mav;
	}
	
	/**
	 * Process a "publish" request
	 * @param url
	 * @param model
	 * @return
	 */
	@RequestMapping(params = {HUB_MODE_PUBLISH}, method=RequestMethod.POST)
	public ModelAndView publish(
			@RequestParam(HUB_URL) String url,
			ModelAndView mav) {

		// load feed for url
		Feed f = hubService.getFeedByUrl(url);
		
		if (f != null) {
			// if found: 
			//fetch feed from url, 
			//update cache, 
			//and alert subscribers
			Collection<Subscriber> subscribers = hubService.getSubscribersByFeed(f);
			http.fetchAndRepublishFeedToSubscribers(f, subscribers);
		} else {
			// if not found or not authorized, return 404,403
			throw new FeedNotFoundException();
			//throw new PermissionDeniedException();
		}
		// return a 204 for success
		mav.setViewName("publishSuccess");
		return mav;
	}
}
