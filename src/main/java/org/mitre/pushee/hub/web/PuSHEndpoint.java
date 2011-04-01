package org.mitre.pushee.hub.web;

import org.apache.commons.httpclient.HttpClient;
import org.mitre.pushee.hub.model.Feed;
import org.mitre.pushee.hub.model.Subscriber;
import org.mitre.pushee.hub.service.HubService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/hub")
public class PuSHEndpoint {

	private HubService hubService;
	
	@RequestMapping(params={"hub.mode=subscribe",
							"hub.callback","hub.topic","hub.verify"}, 
							method=RequestMethod.POST)
	public ModelMap subscribeRequest(
			@RequestParam("hub.callback") String callback,
			@RequestParam("hub.topic") String topic,
			@RequestParam("hub.verify") ClientVerify verify,
			@RequestParam(value="hub.lease_seconds", required=false, defaultValue="0") long leaseSeconds,
			@RequestParam(value="hub.secret", required=false) String secret,
			@RequestParam(value="hub.verify_token", required=false) String verifyToken,
			ModelMap model) {
		
		// Load the subscriber from its callback url, if available
		// create a subscriber {callback,verify} if not available, and save it
		// get the feed object from the topic url
		//   -- return 404 if not found, 403 if not allowed, etc
		// (for now, only support sync verification)
		// do a sync post to the callback URL to verify
		// check verification contents, continue
		// create a subscription {subscriber,feed,timeout)
		// store the subscription details, overwriting old subscription if found
		// return a 204 for valid subscription
		// TODO: if we do the callback async, return 202 instead
		return model;
	}
	
	@RequestMapping(params={"hub.mode=unsubscribe",
			"hub.callback","hub.topic","hub.verify"}, 
			method=RequestMethod.POST)
	public ModelMap unsubscribeRequest(
			@RequestParam("hub.callback") String callback,
			@RequestParam("hub.topic") String topic,
			@RequestParam("hub.verify") ClientVerify verify,
			@RequestParam(value="hub.lease_seconds", required=false, defaultValue="0") long leaseSeconds,
			@RequestParam(value="hub.secret", required=false) String secret,
			@RequestParam(value="hub.verify_token", required=false) String verifyToken,			
			ModelMap model) {
		
		// Load the subscriber from its callback url, if available
		// get the feed object from the topic url
		// find the subscription {subscriber,feed,timeout)
		//   -- return 404 if not found, 403 if not allowed, etc
		// (for now, only support sync verification)
		// do a sync post to the callback URL to verify
		// check verification contents, continue
		// delete the subscription from our store
		// return a 204 for valid unsubscription

		// TODO: if we do the callback async, return 202 instead
		
		return model;
	}
	
	@RequestMapping(params = {"hub.mode=publish"}, method=RequestMethod.POST)
	public ModelMap publish(
			@RequestParam("hub.url") String url,
			ModelMap model) {
		
		// load feed for url
		// if found, fetch feed for url, update cache, and alert subscribers
		// if not found or not authorized, return 404,403
		
		// return a 204 for success
		
		return model;
	}
	
	// TODO: add in periodic refresh of subscription on verification
	
	// utility functions, to be moved to utility class
	private boolean verifyCallback(String callback, String mode, long leaseSeconds, String verifyToken) {
		// make a call to callback with the appropriate parameters
		HttpClient hc = new HttpClient(); // etc...
		// if the call comes back 200 return true, else
		return false;
	}
	
	private void fetchAndRepublishUrl(Feed feed) {

	}
}
