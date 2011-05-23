package org.mitre.pushee.hub.model;

import java.util.Calendar;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * Subscription representation. A single subscription links a single subscriber to
 * a single feed. Subscribers and feeds can have many subscriptions.
 * 
 * @author AANGANES
 * 		Created on 4/13/2011
 */
@Entity
@NamedQueries({
    @NamedQuery(name="Subscription.getByFeedId", query="SELECT s FROM Subscription s WHERE s.feed.id = :feedId"),
    @NamedQuery(name="Subscription.getSubscribersByFeedId", query="SELECT s FROM Subscription sub join sub.subscriber s WHERE sub.feed.id = :feedId")
    })
public class Subscription {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Temporal(TemporalType.DATE)
	private Calendar timeout;
	
	@Basic
	private String secret;
	
	@ManyToOne
    @JoinColumn(name = "feed_id")
	private Feed feed;
	
	@ManyToOne
    @JoinColumn(name = "subscriber_id")
	private Subscriber subscriber;
	
	public Subscription() {
		
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Subscription other = (Subscription) obj;
		if (feed == null) {
			if (other.feed != null) {
				return false;
			}
		} else if (!feed.equals(other.feed)) {
			return false;
		}
		if (subscriber == null) {
			if (other.subscriber != null) {
				return false;
			}
		} else if (!subscriber.equals(other.subscriber)) {
			return false;
		}
		return true;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((feed == null) ? 0 : feed.hashCode());
		result = prime * result
				+ ((subscriber == null) ? 0 : subscriber.hashCode());
		return result;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param timeout the timeout to set
	 */
	public void setTimeout(Calendar timeout) {
		this.timeout = timeout;
	}

	/**
	 * @return the timeout
	 */
	public Calendar getTimeout() {
		return timeout;
	}

	/**
	 * @param secret the secret to set
	 */
	public void setSecret(String secret) {
		this.secret = secret;
	}

	/**
	 * @return the secret
	 */
	public String getSecret() {
		return secret;
	}

	/**
	 * @param feed the feed to set
	 */
	public void setFeed(Feed feed) {
		this.feed = feed;
	}

	/**
	 * @return the feed
	 */
	public Feed getFeed() {
		return feed;
	}

	/**
	 * @param subscriber the subscriber to set
	 */
	public void setSubscriber(Subscriber subscriber) {
		this.subscriber = subscriber;
	}

	/**
	 * @return the subscriber
	 */
	public Subscriber getSubscriber() {
		return subscriber;
	}
	
	
}
