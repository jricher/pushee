package org.mitre.pushee.hub.web;

import java.io.UnsupportedEncodingException;
import java.util.Collection;

import org.apache.http.client.HttpClient;
import org.mitre.pushee.hub.model.Feed;
import org.mitre.pushee.hub.model.Subscriber;

public interface ClientConnection {

	public void fetchAndRepublishFeedToSubscribers(Feed feed,
			Collection<Subscriber> subscribers);

	public void postToSubscriber(String body, Subscriber subscriber,
			String mimeType, String charset, HttpClient hc)
			throws UnsupportedEncodingException;

	// utility functions, to be moved to utility class
	public boolean verifyCallback(String callback, String mode, String topic,
			long leaseSeconds, String verifyToken);

}