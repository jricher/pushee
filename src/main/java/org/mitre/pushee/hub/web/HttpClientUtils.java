package org.mitre.pushee.hub.web;

import static org.mitre.pushee.hub.web.PuSHProtocolParameters.HUB_CHALLENGE;
import static org.mitre.pushee.hub.web.PuSHProtocolParameters.HUB_LEASE_SECONDS;
import static org.mitre.pushee.hub.web.PuSHProtocolParameters.HUB_MODE;
import static org.mitre.pushee.hub.web.PuSHProtocolParameters.HUB_TOPIC;
import static org.mitre.pushee.hub.web.PuSHProtocolParameters.HUB_VERIFY_TOKEN;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
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

import com.google.common.base.Strings;
import com.google.common.io.CharStreams;

public class HttpClientUtils {

	public void fetchAndRepublishFeedToSubscribers(Feed feed, Collection<Subscriber> subscribers) {
	
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

	public void postToSubscriber(String body, Subscriber subscriber, String mimeType, String charset, HttpClient hc)
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

	// TODO: add in periodic refresh of subscription on verification
	
	// utility functions, to be moved to utility class
	public boolean verifyCallback(String callback, String mode, String topic, long leaseSeconds, String verifyToken) {
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

}
