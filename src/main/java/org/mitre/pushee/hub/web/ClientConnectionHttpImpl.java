package org.mitre.pushee.hub.web;

import static org.mitre.pushee.hub.web.PuSHProtocolParameters.HUB_CHALLENGE;
import static org.mitre.pushee.hub.web.PuSHProtocolParameters.HUB_LEASE_SECONDS;
import static org.mitre.pushee.hub.web.PuSHProtocolParameters.HUB_MODE;
import static org.mitre.pushee.hub.web.PuSHProtocolParameters.HUB_TOPIC;
import static org.mitre.pushee.hub.web.PuSHProtocolParameters.HUB_VERIFY_TOKEN;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Future;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.mitre.pushee.hub.model.Feed;
import org.mitre.pushee.hub.model.Subscriber;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.google.common.io.CharStreams;

@Service
public class ClientConnectionHttpImpl implements ClientConnection {

	/* (non-Javadoc)
	 * @see org.mitre.pushee.hub.web.ClientConnection#fetchAndRepublishFeedToSubscribers(org.mitre.pushee.hub.model.Feed, java.util.Collection)
	 */
	@Async
	@Override
	public void fetchAndRepublishFeedToSubscribers(Feed feed, Collection<Subscriber> subscribers) {

		if (subscribers == null || subscribers.isEmpty()) {
			// no subscribers, don't bother fetching
			// TODO: do the fetch and throw it into cache? does anyone care?
			return;
		}
		
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
					String contentType = null;
					if (entity.getContentType() != null) {
						contentType = entity.getContentType().getValue();
					}
					String contentEncoding = null;
					if (entity.getContentEncoding() != null) {
						contentEncoding = entity.getContentEncoding().getValue();
					}
					postToSubscriber(body, subscriber, contentType, contentEncoding, hc);
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

	/* (non-Javadoc)
	 * @see org.mitre.pushee.hub.web.ClientConnection#postToSubscriber(java.lang.String, org.mitre.pushee.hub.model.Subscriber, java.lang.String, java.lang.String, org.apache.http.client.HttpClient)
	 */
	@Override
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
			
			// TODO: handle response, but for now we don't care
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// TODO: add in periodic refresh of subscription on verification
	

	
	/* (non-Javadoc)
     * @see org.mitre.pushee.hub.web.ClientConnection#verifyCallback(java.lang.String, java.lang.String, java.lang.String, int, java.lang.String)
     */
    @Override
    public boolean verifyCallback(String callback, String mode, String topic, int leaseSeconds, String verifyToken) {
		// make a call to callback with the appropriate parameters
		
		HttpClient hc = new DefaultHttpClient();
		
		HttpPost post = new HttpPost(callback);
		//HttpParams params = post.getParams();
	
		UUID challenge = UUID.randomUUID();
		
		List<NameValuePair> args = new ArrayList<NameValuePair>();
		args.add(new BasicNameValuePair(HUB_MODE, mode));
		args.add(new BasicNameValuePair(HUB_TOPIC, topic));
		args.add(new BasicNameValuePair(HUB_CHALLENGE, challenge.toString()));
		args.add(new BasicNameValuePair(HUB_LEASE_SECONDS, Integer.toString(leaseSeconds)));
		if (!Strings.isNullOrEmpty(verifyToken)) {
			args.add(new BasicNameValuePair(HUB_VERIFY_TOKEN, verifyToken));
		}
		
		try {
			post.setEntity(new UrlEncodedFormEntity(args));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
		
		// if the call comes back 200 and the challenge matches, we will have returned true above
		
		// if all else fails, 
		return false;
	}


}
