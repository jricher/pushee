package org.mitre.pushee.hub.web;

import static org.mitre.pushee.hub.web.PuSHProtocolParameters.HUB_CALLBACK;
import static org.mitre.pushee.hub.web.PuSHProtocolParameters.HUB_CHALLENGE;
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

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.mitre.pushee.hub.model.Feed;
import org.mitre.pushee.hub.model.Subscriber;
import org.mitre.pushee.hub.model.Subscription;
import org.mitre.pushee.hub.service.HubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.base.Strings;
import com.google.common.io.CharStreams;

import org.mitre.pushee.hub.exception.FeedNotFoundException;

@Controller
@RequestMapping("/hub")
public class PuSHEndpoint {

	@Autowired
	private final HubService hubService;

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
	 * @param model
	 * @return
	 */
	@RequestMapping(params={HUB_MODE_SUBSCRIBE,
							HUB_CALLBACK,HUB_TOPIC,HUB_VERIFY}, 
							method=RequestMethod.POST)
	public Model subscribeRequest(
			@RequestParam(HUB_CALLBACK) String callback,
			@RequestParam(HUB_TOPIC) String topic,
			@RequestParam(HUB_VERIFY) ClientVerify verify,
			@RequestParam(value=HUB_LEASE_SECONDS, required=false, defaultValue="0") long leaseSeconds,
			@RequestParam(value=HUB_SECRET, required=false) String secret,
			@RequestParam(value=HUB_VERIFY_TOKEN, required=false) String verifyToken,
			Model model) {

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
		// (for now, only support sync verification)
		// do a sync post to the callback URL to verify
		// check verification contents, continue
		// create a subscription {subscriber,feed,timeout)
		Subscription subscript = new Subscription();
		subscript.setFeed(f);
		subscript.setSubscriber(sub);
		//etc
		// store the subscription details, overwriting old subscription if found
		sub.addSubscription(subscript);
		// return a 204 for valid subscript 
		// TODO: if we do the callback async, return 202 instead
		return model;
	}
	
	/**
	 * process an "unsubscribe" request
	 * @param callback
	 * @param topic
	 * @param verify
	 * @param leaseSeconds
	 * @param secret
	 * @param verifyToken
	 * @param model
	 * @return
	 */
	@RequestMapping(params={HUB_MODE_UNSUBSCRIBE,
			HUB_CALLBACK,HUB_TOPIC,HUB_VERIFY}, 
			method=RequestMethod.POST)
	public Model unsubscribeRequest(
			@RequestParam(HUB_CALLBACK) String callback,
			@RequestParam(HUB_TOPIC) String topic,
			@RequestParam(HUB_VERIFY) ClientVerify verify,
			@RequestParam(value=HUB_LEASE_SECONDS, required=false, defaultValue="0") long leaseSeconds,
			@RequestParam(value=HUB_SECRET, required=false) String secret,
			@RequestParam(value=HUB_VERIFY_TOKEN, required=false) String verifyToken,			
			Model model) {
		
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
	
	/**
	 * Process a "publish" request
	 * @param url
	 * @param model
	 * @return
	 */
	@RequestMapping(params = {HUB_MODE_PUBLISH}, method=RequestMethod.POST)
	public Model publish(
			@RequestParam(HUB_URL) String url,
			Model model) {

		// load feed for url
		// if found, fetch feed for url, update cache, and alert subscribers
		// if not found or not authorized, return 404,403
		
		// return a 204 for success
		
		return model;
	}
	
	// TODO: add in periodic refresh of subscription on verification
	
	// utility functions, to be moved to utility class
	private boolean verifyCallback(String callback, String mode, String topic, long leaseSeconds, String verifyToken) {
		// make a call to callback with the appropriate parameters

		HttpClient hc = new DefaultHttpClient();
		
		HttpPost post = new HttpPost(callback);
		HttpParams params = post.getParams();

		UUID challenge = UUID.randomUUID();
		
		params.setParameter(HUB_MODE, mode);
		params.setParameter(HUB_TOPIC, topic);
		params.setParameter(HUB_CHALLENGE, challenge.toString());
		params.setLongParameter(HUB_LEASE_SECONDS, leaseSeconds);
		if (!Strings.isNullOrEmpty(verifyToken)) {
			params.setParameter(HUB_VERIFY_TOKEN, verifyToken);
		}
		
		try {
			HttpResponse resp = hc.execute(post);
			
			int sc = resp.getStatusLine().getStatusCode();
			
			if (sc >= 200 && sc < 300) {
				// HTTP 2XX response code

				// parse out the body
				HttpEntity entity = resp.getEntity();
				String body = CharStreams.toString(new InputStreamReader(entity.getContent()));
				
				body = body.trim();
				if (body.equals(challenge.toString())) {
					// valid response with matching challenge
					return true;
				} else {
					// valid response, challenge doesn't match
					return false;
				}
				
			} else {
				
				// some other error code, handle it here
				
				return false;
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// if the call comes back 200 and the challenge matches, return true, else
		return false;
	}
	
	private void fetchAndRepublishFeedToSubscribers(Feed feed) {

		HttpClient hc = new DefaultHttpClient();

		HttpGet get = new HttpGet(feed.getUrl());

		try {
			HttpResponse resp = hc.execute(get);
			
			int sc = resp.getStatusLine().getStatusCode();
			
			if (sc >= 200 && sc < 300) {
				// HTTP 2XX response code

				// parse out the body
				HttpEntity entity = resp.getEntity();
				String body = CharStreams.toString(new InputStreamReader(entity.getContent()));

				Collection<Subscriber> subscribers = hubService.getSubscribersByFeed(feed);
				
				for (Subscriber subscriber : subscribers) {
					postToSubscriber(body, subscriber, entity.getContentType().getValue(), entity.getContentEncoding().getValue(), hc);
				}
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void postToSubscriber(String body, Subscriber subscriber, String mimeType, String charset, HttpClient hc)
			throws UnsupportedEncodingException {
		
		String url = subscriber.getPostbackURL();
		HttpPost post = new HttpPost(url);
		HttpParams params = post.getParams();

		// re-send the body as a string
		HttpEntity postBody = new StringEntity(body, mimeType, charset);
		post.setEntity(postBody);
		
		try {
			HttpResponse resp = hc.execute(post);
			
			// TODO: handle response
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
