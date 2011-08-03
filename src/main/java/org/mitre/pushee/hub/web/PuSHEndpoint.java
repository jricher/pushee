package org.mitre.pushee.hub.web;

import static org.mitre.pushee.hub.web.PuSHProtocolParameters.HUB_CALLBACK;
import static org.mitre.pushee.hub.web.PuSHProtocolParameters.HUB_LEASE_SECONDS;
import static org.mitre.pushee.hub.web.PuSHProtocolParameters.HUB_MODE;
import static org.mitre.pushee.hub.web.PuSHProtocolParameters.HUB_SUBSCRIBE;
import static org.mitre.pushee.hub.web.PuSHProtocolParameters.HUB_UNSUBSCRIBE;
import static org.mitre.pushee.hub.web.PuSHProtocolParameters.HUB_MODE_PUBLISH;
import static org.mitre.pushee.hub.web.PuSHProtocolParameters.HUB_MODE_SUBSCRIBE;
import static org.mitre.pushee.hub.web.PuSHProtocolParameters.HUB_MODE_UNSUBSCRIBE;
import static org.mitre.pushee.hub.web.PuSHProtocolParameters.HUB_SECRET;
import static org.mitre.pushee.hub.web.PuSHProtocolParameters.HUB_TOPIC;
import static org.mitre.pushee.hub.web.PuSHProtocolParameters.HUB_URL;
import static org.mitre.pushee.hub.web.PuSHProtocolParameters.HUB_VERIFY;
import static org.mitre.pushee.hub.web.PuSHProtocolParameters.HUB_VERIFY_TOKEN;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.mitre.pushee.hub.exception.FeedNotFoundException;
import org.mitre.pushee.hub.exception.SubscriberNotFoundException;
import org.mitre.pushee.hub.model.Feed;
import org.mitre.pushee.hub.model.Subscriber;
import org.mitre.pushee.hub.model.Subscription;
import org.mitre.pushee.hub.service.HubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;


@Controller
@RequestMapping("/hub")
public class PuSHEndpoint {

	public static final String PUBLISH_SUCCESS_VIEW = "publishSuccess";

	public static final String UNSUBSCRIPTION_SUCCESS_VIEW = "unsubscribeSuccess";

	public static final String SUBSCRIPTION_SUCCESS_VIEW = "validSubscription";
	
	public static final String SUBSCRIPTION_ASYNC_VIEW = "accepted";

	public static final String UNSUBSCRIPTION_ASYNC_VIEW = "accepted";

	private HubService hubService;

	private ClientConnection http;

	// TODO: I don't like this being static, but I'm not seeing any other way to make the @Controller and @Scheduled instances line up...
	private static List<AsyncDataContainer> asyncRequests = Collections.synchronizedList(new ArrayList<AsyncDataContainer>());
	
    @Autowired
    public PuSHEndpoint(HubService hubService, ClientConnection http) {
        this.hubService = hubService;
        this.http = http;
    }

	/**
	 * Process a "subscribe" request
	 * @param callback
	 * @param mode
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
			@RequestParam(HUB_VERIFY) String verifyString,
			@RequestParam(value=HUB_LEASE_SECONDS, required=false, defaultValue="0") int leaseSeconds,
			@RequestParam(value=HUB_SECRET, required=false) String secret,
			@RequestParam(value=HUB_VERIFY_TOKEN, required=false) String verifyToken,
			ModelAndView modelAndView) {

		ClientVerify verify = Enum.valueOf(ClientVerify.class, verifyString.toUpperCase());		

		// Load the subscriber from its callback url, if available
		Subscriber sub = hubService.getSubscriberByCallbackURL(callback);
		// create a subscriber {callback,verify} if not available, and save it
		if (sub == null) {
			sub = new Subscriber();
			sub.setPostbackURL(callback);
			hubService.saveSubscriber(sub);
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

		// callback to the client to verify their intent
		///Future<Boolean> futureVerified = http.verifyCallback(callback, mode, topic, leaseSeconds, verifyToken);
		
		if (verify.equals(ClientVerify.ASYNC)) {
			// drop the job into an executioner to check later
			asyncRequests.add(new AsyncDataContainer(sub.getId(), f.getId(), leaseSeconds, callback, mode, verifyToken));
			
			modelAndView.setViewName(SUBSCRIPTION_ASYNC_VIEW);
		} else {
			// synchronous, take care of this right now
			boolean verified = http.verifyCallback(callback, mode, topic, leaseSeconds, verifyToken);
			
			if (verified) {
				
				createNewSubscription(sub, f, leaseSeconds);
				
				// return a 204 for valid subscript 
				modelAndView.setViewName(SUBSCRIPTION_SUCCESS_VIEW);
			} else {
				// no verification, throw an error
				throw new SubscriberNotFoundException("Subscriber failed to verify");
			}
		}
		return modelAndView;
	}

	/**
	 * Utility function to create a new subscription
	 * @param sub
	 * @param f
	 * @param leaseSeconds
	 */
	private void createNewSubscription(Subscriber sub, Feed f, int leaseSeconds) {
	    // create a subscription {subscriber,feed,timeout)
	    Subscription subscript = new Subscription();
	    subscript.setFeed(f);
	    subscript.setSubscriber(sub);
	    if (leaseSeconds > 0) {
	    	Calendar timeoutDate = Calendar.getInstance();
	    	//timeoutDate.setSeconds((int)(timeoutDate.getSeconds() + leaseSeconds));
	    	timeoutDate.add(Calendar.SECOND, leaseSeconds);
	    	subscript.setTimeout(timeoutDate);
	    }
	    // store the subscription details, overwriting old subscription if found
	    sub.addSubscription(subscript);
	    
	    hubService.saveSubscriber(sub);
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
			@RequestParam(HUB_VERIFY) String verifyString,
			@RequestParam(value=HUB_LEASE_SECONDS, required=false, defaultValue="0") int leaseSeconds,
			@RequestParam(value=HUB_SECRET, required=false) String secret,
			@RequestParam(value=HUB_VERIFY_TOKEN, required=false) String verifyToken,			
			ModelAndView modelAndView) {
		
		ClientVerify verify = Enum.valueOf(ClientVerify.class, verifyString.toUpperCase());
		
		// Load the subscriber from its callback url, if available
		Subscriber sub = hubService.getSubscriberByCallbackURL(callback);

		if (sub != null) {
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
			
			if (verify.equals(ClientVerify.ASYNC)) {
				// drop the job into an executioner to check later
				asyncRequests.add(new AsyncDataContainer(sub.getId(), f.getId(), leaseSeconds, callback, mode, verifyToken));
				
				modelAndView.setViewName(UNSUBSCRIPTION_ASYNC_VIEW);
			} else {
				// synchronous, take care of this right now
				boolean verified = http.verifyCallback(callback, mode, topic, leaseSeconds, verifyToken);
				
				if (verified) {
					// delete the subscription from our store
					removeSubscription(sub, f);
					// return a 204 for valid unsubscription
					modelAndView.setViewName(UNSUBSCRIPTION_SUCCESS_VIEW);
				} else {
					// no verification, throw an error
					throw new SubscriberNotFoundException("Subscriber failed to verify request");
				}
			}
			return modelAndView;
		} else {
			throw new SubscriberNotFoundException();
		}
	}

	/**
	 * Utility to actually remove a subscription
	 * @param sub
	 * @param f
	 */
	private void removeSubscription(Subscriber sub, Feed f) {
	    sub.removeSubscription(f);
	    
	    hubService.saveSubscriber(sub);
    }
	
	/**
	 * Process all of our asynchronous background requests
	 */
	@Scheduled(fixedRate = 60 * 1000) // every five minutes
	public void processAsyncRequests() {
		for (AsyncDataContainer data : asyncRequests) {
			// look up the subscriber and feed from the async request
    		Subscriber sub = hubService.getSubscriberById(data.subscriberId);
    		Feed f = hubService.getFeedById(data.feedId);
    		if (sub != null && f != null) {
	        	boolean verified = http.verifyCallback(sub.getPostbackURL(), data.mode, f.getUrl(), data.leaseSeconds, data.verifyToken);
	        	if (verified) {
	        		if (data.mode.equals(HUB_SUBSCRIBE)) {
	        			createNewSubscription(sub, f, data.leaseSeconds);
	        		} else if (data.mode.equals(HUB_UNSUBSCRIBE)) {
	        			removeSubscription(sub, f);
	        		}
	        	}
	        }
        }
		// we always clean out things -- clients get one chance to respond
		asyncRequests.clear();
	}
	
	/**
	 * Process a "publish" request
	 * @param url
	 * @param model
	 * @return
	 */
	@RequestMapping(params = {HUB_MODE_PUBLISH}, method=RequestMethod.POST)
	public ModelAndView publish(
			@RequestParam(HUB_URL) String url /*TODO This needs to accept a list of URLs */,
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
		mav.setViewName(PUBLISH_SUCCESS_VIEW);
		return mav;
	}
	
	/**
	 * Internal data shuttle for handling asynchronous data callbacks
	 * @author jricher
	 *
	 */
	private class AsyncDataContainer {
		public final Long subscriberId;
		public final Long feedId;
		public final int leaseSeconds;
		public final String callback;
		public final String mode;
		public final String verifyToken;

		public AsyncDataContainer(Long subscriberId, Long feedId, int leaseSeconds, String callback, String mode, String verifyToken) {
	        super();
	        this.subscriberId = subscriberId;
	        this.feedId = feedId;
	        this.leaseSeconds = leaseSeconds;
	        this.callback = callback;
	        this.mode = mode;
	        this.verifyToken = verifyToken;
        }
		
	}
}
