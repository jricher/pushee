package org.mitre.pushee.hub.objectmodel;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;

@Entity
public class Subscription {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private String id;
	
	@Basic
	private String topicURL;
	
	@Basic
	private String subscriberCallbackURL;
	
	@Temporal(DATE)
	private Date expirationTime;
	
	public Subscription() {
		
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param topicURL the topicURL to set
	 */
	public void setTopicURL(String topicURL) {
		this.topicURL = topicURL;
	}

	/**
	 * @return the topicURL
	 */
	public String getTopicURL() {
		return topicURL;
	}

	/**
	 * @param subscriberCallbackURL the subscriberCallbackURL to set
	 */
	public void setSubscriberCallbackURL(String subscriberCallbackURL) {
		this.subscriberCallbackURL = subscriberCallbackURL;
	}

	/**
	 * @return the subscriberCallbackURL
	 */
	public String getSubscriberCallbackURL() {
		return subscriberCallbackURL;
	}
	
}
